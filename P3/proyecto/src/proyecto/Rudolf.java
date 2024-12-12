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
        coordenadasRenos.add("90, 10");
        coordenadasRenos.add("90, 20");
        coordenadasRenos.add("90, 30");
        coordenadasRenos.add("90, 40");
        coordenadasRenos.add("90, 50");
    }
    public Rudolf(ArrayList<String> coordenadasRenos) {
        this.coordenadasRenos = coordenadasRenos;
    }

    @Override
    protected void setup() {
        
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.blockingReceive();  // Recibir mensaje
                
                if (msg != null) {
                    String contenido = msg.getContent();
                    System.out.println("Mensaje recibido: " + contenido);
                    
                    ACLMessage reply = msg.createReply();  // Preparar la respuesta

                    // Verificar el código recibido
                    if (contenido.equals(codigoValido)) {
                        if (indiceRenoActual < coordenadasRenos.size()) {
                            reply.setContent(coordenadasRenos.get(indiceRenoActual));
                            System.out.println("Enviando coordenadas: " + coordenadasRenos.get(indiceRenoActual));
                            indiceRenoActual++;  // Avanzar al siguiente reno
                        } else {
                            reply.setContent("No quedan más renos disponibles.");
                            System.out.println("Mensaje final: No quedan más renos.");
                        }
                    } else {
                        reply.setContent("Código incorrecto. Acceso denegado.");
                        System.out.println("Código inválido recibido.");
                    }
                    myAgent.send(reply);  // Enviar la respuesta
                } else {
                    System.out.println("No se recibió ningún mensaje.");
                    
                }
            }
        });
    }


}
