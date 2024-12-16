package proyecto;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;
/*
 * Clase que representa a Santa.
 * Sus funciones son: decidir si permitir o no al agente generando una contraseña (recibir mensaje y generar respuesta), devolver coordenadas de su casa y mensaje de "jojojo" cuando llegue el agente.
 */

public class Santa extends Agent {
    private int [] coordenadas_casa = {90, 0};
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.blockingReceive(); // Recibe el mensaje
                if (msg != null && msg.getPerformative() == ACLMessage.PROPOSE) {
                    String contenido = msg.getContent();
                    String tipoMensaje = msg.getUserDefinedParameter("tipo"); // Obtener el tipo de solicitud
                    
                    System.out.println("Mensaje recibido desde Santa: " + contenido);
                    System.out.println("Tipo de mensaje: " + tipoMensaje);
                    
                    // Procesar el mensaje según el tipo de solicitud
                    ACLMessage reply = msg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent("Hyvää joulua " + generarRespuestaPermiso(contenido)+" Nähdään pian");
                    
                    myAgent.send(reply);
                    System.out.println("Respuesta enviada: " + reply.getContent());
                    
                } else if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    String contenido = msg.getContent();
                    String tipoMensaje = msg.getUserDefinedParameter("tipo"); // Obtener el tipo de solicitud
                    
                    System.out.println("Mensaje recibido desde Santa: " + contenido);
                    System.out.println("Tipo de mensaje: " + tipoMensaje);
                    
                    // Procesar el mensaje según el tipo de solicitud
                    ACLMessage reply = msg.createReply(ACLMessage.INFORM);
                    switch (tipoMensaje) {
                        case "solicitudCoordenadas":
                            reply.setContent("Hyvää joulua " + coordenadas_casa[0] + "," + coordenadas_casa[1] + " Nähdään pian");
                            break;
                        case "llegada":
                            reply.setContent("¡Jojojo!");
                            break;
                        default:
                            reply.setContent("Hyvää joulua " + "Tipo de solicitud desconocido" + " Nähdään pian");
                    }
                    
                    myAgent.send(reply);
                    System.out.println("Respuesta enviada: " + reply.getContent());
                } else {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("Hyvää joulua " + "Error en la solicitud" + " Nähdään pian");
                    myAgent.send(reply);
                }
            }

            private String generarRespuestaPermiso(String contenido) {
                // Simulación de generación de contraseña o permiso
                String contrasena = "1234";
                Random random = new Random();
                int probabilidad = random.nextInt(100);
                int resultado = (probabilidad < 80) ? 1 : 0;

                if (resultado == 0) {
                    return "No";
                }else{
                    return contrasena;
                }
            }

        });
    }
}
