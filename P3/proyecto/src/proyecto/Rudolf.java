package proyecto;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Random;

/*
 * Clase que representa a Rudolf.
 * Sus funciones son: Recibir mensaje con codigo, comprobar el codigo para saber si es valido, enviar mensaje con coordenadas de los otros renos y enviar mensaje final si no quedan renos.
 * Tiene un array con las coordenadas de todos los renos.
 */

public class Rudolf extends Agent {
    private ArrayList<String> coordenadasRenos;  // Lista dinámica de coordenadas
    private int indiceRenoActual = 0;  // Controla el reno a enviar
    private final String codigoValido = "1234";  // Código de validación

    public Rudolf() {
        coordenadasRenos = new ArrayList<>();
//        coordenadasRenos.add("20,10");
//        coordenadasRenos.add("90,20");
//        coordenadasRenos.add("90,30");
//        coordenadasRenos.add("90,40");
//        coordenadasRenos.add("90,50");
    }
    public Rudolf(ArrayList<String> coordenadasRenos) {
        this.coordenadasRenos = coordenadasRenos;
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 1) {
            this.coordenadasRenos = (ArrayList<String>) args[0];
            
            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    ACLMessage msg = myAgent.blockingReceive();  // Recibir mensaje

                    if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                        String contenido = msg.getContent();
                        System.out.println("Mensaje recibido desde Rudolf: " + contenido);

                        ACLMessage reply = msg.createReply();  // Preparar la respuesta

                        // Verificar el código recibido
                        if (contenido.equals(codigoValido)) {
                            if (indiceRenoActual < coordenadasRenos.size()) {
                                reply.setContent(coordenadasRenos.get(indiceRenoActual));
                                System.out.println("Enviando coordenadas: " + coordenadasRenos.get(indiceRenoActual));
                                indiceRenoActual++;  // Avanzar al siguiente reno
                            } else {
                                reply.setContent("No quedan mas renos disponibles");
                                System.out.println("Mensaje final: No quedan más renos.");
                            }
                        } else {
                            reply.setContent("Denegado");
                            System.out.println("Código inválido recibido.");
                        }
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);  // Enviar la respuesta
                    } else {
                        System.out.println("No se recibió ningún mensaje.");

                    }
                }
            });
        }
    }


}
