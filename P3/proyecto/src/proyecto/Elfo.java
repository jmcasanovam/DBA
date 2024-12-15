package proyecto;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Elfo extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.blockingReceive(); // Recibe el mensaje
                
                if (msg != null) {
                    String contenido = msg.getContent();
                    String idioma = msg.getUserDefinedParameter("idioma"); // Obtener el idioma
                    
                    System.out.println("Mensaje recibido: " + contenido);
                    System.out.println("Idioma especificado: " + idioma);// Puede ser finlandes o español (fi, es)
                    
                    if("Bro podemos hablar En Plan".equals(contenido)){ // Recibir solicitud de hablar
                        // Crear y enviar la respuesta
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.AGREE);
                        myAgent.send(reply);
                    }
                    else{
                        // Traducir el mensaje según el idioma
                        String traduccion = traducir(contenido, idioma);

                        // Crear y enviar la respuesta
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(traduccion);
                        myAgent.send(reply);

                        System.out.println("Traducción enviada: " + traduccion);
                    }
                } else {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Error en la traducción");
                    myAgent.send(reply);
                }
            }

            private String traducir(String texto, String idioma) {
                String translatedContent = "Traducción no disponible.";
                if ("es".equals(idioma)) {
                    // Traducir de español a finlandes
                    translatedContent = "Rakas Joulupukki " + texto.replace("Bro ", "").replace(" En Plan", "") + " Kiitos";
                    
                } else if ("fi".equals(idioma)) {
                    translatedContent = "Bro " + texto.replace("Hyvää joulua ", "").replace(" Nähdään pian", "") + " En Plan";
                }
                return translatedContent;
            }
        });
    }
}
