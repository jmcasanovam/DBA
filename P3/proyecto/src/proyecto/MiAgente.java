package proyecto;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MiAgente extends Agent {

    @Override
    protected void setup() {
        // Paso 1: Enviar mensaje a Santa para pedir contraseña
        enviarMensaje("Hola Santa, ¿puedo entrar?", "solicitudPermiso", "santaClaus");

        // Paso 2: Recibir respuesta de Santa
        ACLMessage respuestaSanta = blockingReceive(MessageTemplate.MatchSender(new AID("santaClaus", AID.ISLOCALNAME)));
        if (respuestaSanta != null) {
            String contenidoSanta = respuestaSanta.getContent();
            System.out.println("Respuesta recibida de Santa: " + contenidoSanta);

            // Paso 3: Enviar mensaje al Elfo con la respuesta de Santa
            enviarMensajeElfo(contenidoSanta, "fi", "elfo");

            // Paso 4: Recibir traducción del Elfo
            ACLMessage respuestaElfo = blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
            String contenidoElfo = respuestaElfo.getContent();
            if (respuestaElfo != null) {
                
                System.out.println("Traducción recibida del Elfo: " + contenidoElfo);
                // Verificar si se ha dado acceso
                if (contenidoSanta.contains("No se permite el acceso")) {
                    System.out.println("Acceso denegado por Santa. Terminando agente.");
                    doDelete();
                    return;
                }
            }
            
            //Paso 5: Mando la peticion a Rudolf
            
            
        }
    }

    private void enviarMensaje(String contenido, String tipo, String receptor) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(receptor, AID.ISLOCALNAME)); // Especifica el receptor
        msg.setContent(contenido); // Contenido del mensaje
        msg.addUserDefinedParameter("tipo", tipo); // Tipo de solicitud
        send(msg);
        System.out.println("Mensaje enviado a " + receptor + ": " + contenido + " | Tipo: " + tipo);
    }
    
    private void enviarMensajeElfo(String contenido, String idioma, String receptor){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(receptor, AID.ISLOCALNAME)); // Especifica el receptor
        msg.setContent(contenido); // Contenido del mensaje
        msg.addUserDefinedParameter("idioma", idioma); // Tipo de solicitud
        send(msg);
        System.out.println("Mensaje enviado a " + receptor + ": " + contenido + " | Idioma: " + idioma);
    }
}
