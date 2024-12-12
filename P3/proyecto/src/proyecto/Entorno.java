package proyecto;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Entorno extends Agent {

    private ArrayList<Casilla> mapa;
    private int alto;
    private int ancho;
    private String nombreArchivo;

    private int filaObjetivo;
    private int columnaObjetivo;
    private Agente jugador;

    private MapaVisual panelMapa;
    private JFrame frame;


// Constructor sin parámetros
    public Entorno() {
        this.mapa = new ArrayList<>();
        this.alto = 0;
        this.ancho = 0;
        this.nombreArchivo = "";
    }

    public Entorno(String nombreArchivo, int filaJ, int columnaJ) {

        this.mapa = new ArrayList<>();
        this.alto = 0;
        this.ancho = 0;
        this.nombreArchivo = nombreArchivo;

        leerMapa();

        this.jugador = new Agente(filaJ, columnaJ);
        configurarInterfaz();
        mostrarMapa();
    }

    private void configurarInterfaz() {
        frame = new JFrame("Mapa Visual");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el mapa inicial para el panel
        int[][] mapaInicial = convertirMapa();

        panelMapa = new MapaVisual(mapaInicial);
        frame.add(panelMapa);

        frame.setSize(ancho * 50 + 20, alto * 50 + 40); // Tamaño dinámico
        frame.setVisible(true);
    }

    private int[][] convertirMapa() {
        int[][] matriz = new int[alto][ancho];
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                Casilla casilla = mapa.get(i * ancho + j);
                if (i == jugador.getFilaActual() && j == jugador.getColumnaActual()) {
                    matriz[i][j] = 1; // Jugador
                } else if (i == filaObjetivo && j == columnaObjetivo) {
                    matriz[i][j] = 2; // Objetivo
                } else {
                    matriz[i][j] = casilla.getValor();
                }
            }
        }
        return matriz;
    }

    private void leerMapa() {
        int fila = 0;
        int columna = 0;
        Casilla temp;

        try (BufferedReader br = new BufferedReader(new FileReader(this.nombreArchivo))) {
            String linea = br.readLine();
            if (linea != null) {
                this.alto = Integer.parseInt(linea);
                linea = br.readLine();
                if (linea != null) {
                    this.ancho = Integer.parseInt(linea);
                    while ((linea = br.readLine()) != null) {
                        String[] lista = linea.split("\t");
                        for (String valor : lista) {
                            temp = new Casilla(fila, columna++, Integer.parseInt(valor));
                            this.mapa.add(temp);
                        }
                    }
                }
                columna = 0;
                ++fila;
            }

        } catch (IOException e) {
            e.printStackTrace();  // Manejo de excepción si el archivo no existe o hay un error
        }
    }

    public ArrayList<Casilla> getMapa() {
        return mapa;
    }

    public void mostrarMapa() {
        int[][] mapaVisual = convertirMapa();
        panelMapa.actualizarMapa(mapaVisual); // Actualiza la visualización gráfica

    }

    public void mostrarMapaTerminal() {
        System.out.println("Mapa:");
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (i == jugador.getFilaActual() && j == jugador.getColumnaActual()) {
                    System.out.print("1" + "\t");
                } else if (i == filaObjetivo && j == columnaObjetivo) {
                    System.out.print("2" + "\t");
                } else {
                    System.out.print(mapa.get(i * ancho + j).getValor() + "\t");  // Imprimir cada valor con tabulador
                }
            }
            System.out.println();  // Nueva linea despues de cada fila
        }
    }

    private void esperarUnSegundo() {
        try {
            Thread.sleep(1000); // Pausa de 1 segundo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción del hilo
            System.err.println("Error en el delay: " + e.getMessage());
        }
    }

    public void ejecucion() {
        while (jugador.getFilaActual() != filaObjetivo || jugador.getColumnaActual() != columnaObjetivo) {
            mapa.get(jugador.getFilaActual() * ancho + jugador.getColumnaActual()).sumarPaso();

            cargarVision();
            jugador.moverse();
            mostrarMapa();
            esperarUnSegundo();
        }
        // jugador.finalizar();
    }

    public void cargarVision() {
        //Cargar la vision. Si esta en los bordes, se carga como -1 (muro)
        ArrayList<Casilla> vision = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            vision.add(new Casilla());
        }

        if (jugador.getFilaActual() == 0) {
            for (int i = 0; i < 3; i++) {
                vision.get(i).setValor(-1);
            }
        }
        if (jugador.getColumnaActual() == 0) {
            for (int i = 0; i < 3; i++) {
                vision.get((i + 6) % 8).setValor(-1);
            }
        }

        if (jugador.getFilaActual() == alto - 1) {
            for (int i = 4; i < 7; i++) {
                vision.get(i).setValor(-1);
            }
        }
        if (jugador.getColumnaActual() == ancho - 1) {
            for (int i = 2; i < 5; i++) {
                vision.get(i).setValor(-1);
            }
        }

        if (vision.get(0).getValor() == -3) {
            vision.get(0).setValor(getValorMapa(jugador.getFilaActual() - 1, jugador.getColumnaActual() - 1));
            vision.get(0).setPasos(mapa.get((jugador.getFilaActual() - 1) * ancho + jugador.getColumnaActual() - 1).getPasos());
        }
        if (vision.get(1).getValor() == -3) {
            vision.get(1).setValor(getValorMapa(jugador.getFilaActual() - 1, jugador.getColumnaActual()));
            vision.get(1).setPasos(mapa.get((jugador.getFilaActual() - 1) * ancho + jugador.getColumnaActual()).getPasos());

        }
        if (vision.get(2).getValor() == -3) {
            vision.get(2).setValor(getValorMapa(jugador.getFilaActual() - 1, jugador.getColumnaActual() + 1));
            vision.get(2).setPasos(mapa.get((jugador.getFilaActual() - 1) * ancho + jugador.getColumnaActual() + 1).getPasos());
        }

        if (vision.get(3).getValor() == -3) {
            vision.get(3).setValor(getValorMapa(jugador.getFilaActual(), jugador.getColumnaActual() + 1));
            vision.get(3).setPasos(mapa.get((jugador.getFilaActual()) * ancho + jugador.getColumnaActual() + 1).getPasos());
        }
        if (vision.get(4).getValor() == -3) {
            vision.get(4).setValor(getValorMapa(jugador.getFilaActual() + 1, jugador.getColumnaActual() + 1));
            vision.get(4).setPasos(mapa.get((jugador.getFilaActual() + 1) * ancho + jugador.getColumnaActual() + 1).getPasos());
        }
        if (vision.get(5).getValor() == -3) {
            vision.get(5).setValor(getValorMapa(jugador.getFilaActual() + 1, jugador.getColumnaActual()));
            vision.get(5).setPasos(mapa.get((jugador.getFilaActual() + 1) * ancho + jugador.getColumnaActual()).getPasos());
        }
        if (vision.get(6).getValor() == -3) {
            vision.get(6).setValor(getValorMapa(jugador.getFilaActual() + 1, jugador.getColumnaActual() - 1));
            vision.get(6).setPasos(mapa.get((jugador.getFilaActual() + 1) * ancho + jugador.getColumnaActual() - 1).getPasos());
        }
        if (vision.get(7).getValor() == -3) {
            vision.get(7).setValor(getValorMapa(jugador.getFilaActual(), jugador.getColumnaActual() - 1));
            vision.get(7).setPasos(mapa.get((jugador.getFilaActual()) * ancho + jugador.getColumnaActual() - 1).getPasos());
        }

        this.jugador.actualizarVision(vision);

    }

    private int getValorMapa(int fila, int columna) {
        return mapa.get(fila * ancho + columna).getValor();
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            this.nombreArchivo = (String) args[0];
            int filaJ = (int) args[1];
            int columnaJ = (int) args[2];

            this.mapa = new ArrayList<>();
            leerMapa();

            if (filaJ < 0 || columnaJ < 0 || filaJ >= this.alto || columnaJ >= this.ancho) {
                throw new IllegalArgumentException("Las coordenadas están fuera de los límites del mapa.");
            }

            this.jugador = new Agente(filaJ, columnaJ);
            configurarInterfaz();

            // Añadir el comportamiento de pedir contraseña
            addBehaviour(new CyclicBehaviour() {
                int step = 0;
                @Override
                public void action() {
                    switch (step) {
                        case 0 -> {
                            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                            msg.setConversationId("elfo-channel");
                            msg.setContent("Bro podemos hablar en plan");
                            myAgent.send(msg);
                            step = 1;
                        }
                        case 1 -> {
                            ACLMessage msg = myAgent.blockingReceive();
                            if (msg.getConversationId().equals("elfo-channel") && msg.getPerformative() == ACLMessage.AGREE) {
                                ACLMessage replay = msg.createReply(ACLMessage.INFORM);
                                replay.setContent("Bro como le pido la contraseña en plan");
                                this.myAgent.send(replay);
                                step = 2;
                            }
                            else {
                                System.out.println("Error in the coversation protocol - step" + 1);
                                myAgent.doDelete();
                            }
                        }
                        case 2 ->{
                            ACLMessage msg = myAgent.blockingReceive();
                            if (msg.getConversationId().equals("elfo-channel") &&msg.getPerformative() == ACLMessage.INFORM) {
                                System.out.println("Agent receive: " + msg.getContent());
                                ACLMessage msg2 = new ACLMessage(ACLMessage.PROPOSE);
                                msg.addReceiver(new AID("mjcobo-receiver", AID.ISLOCALNAME));
                                msg2.setContent(msg.getContent());
                                myAgent.send(msg2);
                                step = 3;
                            } else {
                                System.out.println("Error in the coversation protocol - step" + 2);
                                myAgent.doDelete();
                            }
                        }
                        case 3 ->{
                            ACLMessage response = myAgent.blockingReceive();
                            if (response != null && response.getConversationId().equals("santa-channel") && response.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                                // Mandar la respuesta al canal "elfo-channel"
                                ACLMessage replyToElfo = new ACLMessage(ACLMessage.INFORM);
                                replyToElfo.setConversationId("elfo-channel");
                                replyToElfo.setContent(response.getContent());
                                myAgent.send(replyToElfo);

                                // Recibir la respuesta del canal "elfo-channel"
                                ACLMessage elfoResponse = myAgent.blockingReceive();
                                if (elfoResponse != null && elfoResponse.getConversationId().equals("elfo-channel")) {
                                    String[] trustArray = elfoResponse.getContent().split(",");
                                    boolean confio = true;
                                    for (String trust : trustArray) {
                                        if (trust.equalsIgnoreCase("no confio")) {
                                            confio = false;
                                            break;
                                        }
                                    }
                                    if (!confio) {
                                        step = 0; // Reinicia el comportamiento
                                        return;
                                    }

                                    // Crear nuevo canal "rudolph-channel"
                                    ACLMessage rudolphRequest = new ACLMessage(ACLMessage.REQUEST);
                                    rudolphRequest.setConversationId("rudolph-channel");
                                    rudolphRequest.setContent("Dame coordenadas");
                                    myAgent.send(rudolphRequest);
                                    step = 4;
                                } else {
                                    System.out.println("Error en la respuesta del elfo.");
                                    myAgent.doDelete();
                                }
                            } else {
                                System.out.println("Error in the conversation protocol - step " + 3);
                                myAgent.doDelete();
                            }
                        }
                        case 4 -> {
                            ACLMessage rudolphResponse = myAgent.blockingReceive();
                            if (rudolphResponse != null && rudolphResponse.getConversationId().equals("rudolph-channel")) {
                                String content = rudolphResponse.getContent();
                                if (content.equalsIgnoreCase("No hay enemigos")) {
                                    System.out.println("No hay enemigos restantes.");
                                    step = -1; // Termina el comportamiento
                                } else {
                                    System.out.println("Recibidas coordenadas: " + content);
                                    // Añadir el comportamiento de movimiento
                                    filaObjetivo=0;
                                    columnaObjetivo=0;
                                    this.myAgent.addBehaviour(new TickerBehaviour(this.myAgent, 500) {
                                        @Override
                                        protected void onTick() {
                                            if (jugador.getFilaActual() != filaObjetivo || jugador.getColumnaActual() != columnaObjetivo) {
                                                mapa.get(jugador.getFilaActual() * ancho + jugador.getColumnaActual()).sumarPaso();

                                                cargarVision();
                                                jugador.moverse();
                                                mostrarMapa();
                                            } else {
                                                jugador.finalizar();
                                                stop(); // Detener el comportamiento
                                            }
                                        }
                                    });

                                    // Pedir nuevas coordenadas
                                    ACLMessage rudolphRequest = new ACLMessage(ACLMessage.REQUEST);
                                    rudolphRequest.setConversationId("rudolph-channel");
                                    rudolphRequest.setContent("Dame coordenadas");
                                    myAgent.send(rudolphRequest);
                                }
                            } else {
                                System.out.println("Error en la respuesta de Rudolph.");
                                myAgent.doDelete();
                            }
                        }
                        case -1 -> {
                            System.out.println("El comportamiento cíclico ha terminado.");
                            myAgent.removeBehaviour(this);
                        }
                        default -> {
                            System.out.println("Error in the coversation protocol - step " + 2);
                            myAgent.doDelete();
                        }
                    }
                }
            });
        }
    }

}