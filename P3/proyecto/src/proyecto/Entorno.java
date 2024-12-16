package proyecto;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String contrasena = "";


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
        System.out.println("Este es el valor"+ mapaVisual[90][0]);
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
    
    private void moverJugador(boolean termina) throws InterruptedException{                                 
        System.out.println("Voy a mover el jugador!");
        while(jugador.getFilaActual() != filaObjetivo || jugador.getColumnaActual() != columnaObjetivo){
            Thread.sleep(100);
            mapa.get(jugador.getFilaActual() * ancho + jugador.getColumnaActual()).sumarPaso();
           cargarVision();
           jugador.moverse();
           mostrarMapa();
        }
        
        if(termina){
            jugador.finalizar();
        }
                              
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
                        case 0 -> { //le pregunto al elfo si puedo hablar con el
                            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                            msg.addReceiver(new AID("elfo", AID.ISLOCALNAME));
                            msg.setContent("Bro podemos hablar En Plan");
                            msg.addUserDefinedParameter("idioma", "es"); //idioma fi o es
                            myAgent.send(msg);
                            step = 1;
                        }
                        case 1 -> { //recibo si puedo hablar con el elfo y le pido traduccion sobre como preguntar contraseña a santa
                            ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
                            if (msg!=null && msg.getPerformative() == ACLMessage.AGREE) {
                                ACLMessage replay = msg.createReply(ACLMessage.INFORM);
                                replay.setContent("Bro me dices la contrasena En Plan");
                                replay.addUserDefinedParameter("idioma", "es"); //idioma fi o es
                                this.myAgent.send(replay);
                                step = 2;
                            }
                            else {
                                System.out.println("Error in the coversation protocol - step" + 1);
                                myAgent.doDelete();
                            }
                        }
                        case 2 ->{ //recibo la traduccion de como preguntar la contraseña y se la paso a santa
                            ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
                            if (msg!=null &&msg.getPerformative() == ACLMessage.INFORM) {
                                System.out.println("Agent receive: " + msg.getContent());
                                ACLMessage msg2 = new ACLMessage(ACLMessage.PROPOSE);
                                msg2.addReceiver(new AID("santaClaus", AID.ISLOCALNAME));
                                msg2.setContent(msg.getContent());
                                msg2.addUserDefinedParameter("tipo", "solicitudPermiso");
                                myAgent.send(msg2);
                                step = 3;
                            } else {
                                System.out.println("Error in the coversation protocol - step" + 2);
                                myAgent.doDelete();
                            }
                        }
                        case 3 ->{ //recibo de santa la respuesta
                            ACLMessage response = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("santaClaus", AID.ISLOCALNAME)));
                            if (response != null && response.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                                // Mandar la respuesta a elfo
                                ACLMessage replyToElfo = new ACLMessage(ACLMessage.INFORM);
                                replyToElfo.addReceiver(new AID("elfo", AID.ISLOCALNAME));
                                replyToElfo.addUserDefinedParameter("idioma", "fi"); //idioma fi o es
                                replyToElfo.setContent(response.getContent());
                                myAgent.send(replyToElfo);

                                // Recibir la respuesta de elfo
                                ACLMessage elfoResponse = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
                                if (elfoResponse != null) {
                                    
                                    //limpio el mensaje
                                    contrasena = elfoResponse.getContent().replaceAll("Bro ", "");
                                    contrasena = contrasena.replaceAll(" En Plan", "");
                                    
                                    System.out.println("la contrasena desde entorno es " + contrasena);
                                    if("No".equals(contrasena)){
                                        step = 0; // Reinicia el comportamiento
                                        return;
                                    }

                                    // Hablamos con rudolf
                                    ACLMessage rudolphRequest = new ACLMessage(ACLMessage.REQUEST);
                                    rudolphRequest.addReceiver(new AID("rudolf", AID.ISLOCALNAME));
                                    rudolphRequest.setContent(contrasena);
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
                            ACLMessage rudolphResponse = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("rudolf", AID.ISLOCALNAME)));
                            if (rudolphResponse != null) {
                                String content = rudolphResponse.getContent();
                                if (content.equalsIgnoreCase("No quedan mas renos disponibles")) {
                                    System.out.println("No hay enemigos restantes.");
                                    step = 5; // Termina el comportamiento
                                }else if(content.equalsIgnoreCase("Denegado")){
                                    step = 0;
                                    return;
                                }
                                else {
                                    System.out.println("Recibidas coordenadas: " + content);
                                    // Añadir el comportamiento de movimiento
                                    String[] coordenadas = content.split(","); 
                                    filaObjetivo=Integer.parseInt(coordenadas[0]);
                                    columnaObjetivo=Integer.parseInt(coordenadas[1]);
                                    mapa.set(filaObjetivo*ancho + columnaObjetivo, new Casilla(filaObjetivo, columnaObjetivo, 2));
                                    jugador.setObjetivo(filaObjetivo, columnaObjetivo);
                                    try {
                                        moverJugador(false);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Entorno.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    mapa.set(filaObjetivo*ancho + columnaObjetivo, new Casilla(filaObjetivo, columnaObjetivo, 3));
                                    System.out.println("FILA:"+jugador.getFilaActual()+", Col:"+jugador.getColumnaActual());


                                    // Pedir nuevas coordenadas
                                    ACLMessage rudolphRequest = new ACLMessage(ACLMessage.REQUEST);
                                    rudolphRequest.addReceiver(new AID("rudolf", AID.ISLOCALNAME));
                                    rudolphRequest.setContent(contrasena);
                                    myAgent.send(rudolphRequest);
                                }
                            } else {
                                System.out.println("Error en la respuesta de Rudolph.");
                                myAgent.doDelete();
                            }
                        }
                        case 5->{
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(new AID("elfo", AID.ISLOCALNAME));
                            msg.setContent("Bro donde esta tu casa En Plan");
                            msg.addUserDefinedParameter("idioma", "es"); //idioma fi o es
                            myAgent.send(msg);
                            
                            ACLMessage msg2 = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
                            if (msg2!=null &&msg.getPerformative() == ACLMessage.INFORM) {
                                System.out.println("Agent receive: " + msg2.getContent());
                                ACLMessage casasanta = new ACLMessage(ACLMessage.INFORM);
                                casasanta.addReceiver(new AID("santaClaus", AID.ISLOCALNAME));
                                casasanta.setContent(msg2.getContent());
                                casasanta.addUserDefinedParameter("tipo", "solicitudCoordenadas");
                                myAgent.send(casasanta);
                                step = 6;
                            } else {
                                System.out.println("Error in the coversation protocol - step" + 2);
                                myAgent.doDelete();
                            }
                        }
                        case 6 ->{
                            ACLMessage response = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("santaClaus", AID.ISLOCALNAME)));
                            if (response != null && response.getPerformative() == ACLMessage.INFORM) {
                                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                                msg.addReceiver(new AID("elfo", AID.ISLOCALNAME));
                                msg.setContent(response.getContent());
                                msg.addUserDefinedParameter("idioma", "fi"); //idioma fi o es
                                myAgent.send(msg);
                                
                                ACLMessage msg2 = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
                                if (msg2!=null &&msg2.getPerformative() == ACLMessage.INFORM) {
                                    String aux = msg2.getContent().replaceAll("Bro ", "");
                                    aux = aux.replaceAll(" En Plan", "");
                                    System.out.println("Las coordenadas de la casa son "+ aux);
                                    String[] coordenadas = aux.trim().split(","); 
                                    filaObjetivo=Integer.parseInt(coordenadas[0]);
                                    columnaObjetivo=Integer.parseInt(coordenadas[1]);
                                    mapa.set(filaObjetivo*ancho + columnaObjetivo, new Casilla(filaObjetivo, columnaObjetivo, 4));
                                    jugador.setObjetivo(filaObjetivo, columnaObjetivo);
                                    try {
                                        moverJugador(true);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Entorno.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    step=7;
                                }
                            }
                        }
                        case 7 ->{
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(new AID("elfo", AID.ISLOCALNAME));
                            msg.setContent("Bro ya he llegado En Plan");
                            msg.addUserDefinedParameter("idioma", "es"); //idioma fi o es
                            myAgent.send(msg);
                            
                            ACLMessage msg2 = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("elfo", AID.ISLOCALNAME)));
                            if (msg2!=null &&msg2.getPerformative() == ACLMessage.INFORM) {
                                System.out.println("Agent receive: " + msg2.getContent());
                                ACLMessage casasanta = new ACLMessage(ACLMessage.INFORM);
                                casasanta.addReceiver(new AID("santaClaus", AID.ISLOCALNAME));
                                casasanta.setContent(msg.getContent());
                                casasanta.addUserDefinedParameter("tipo", "llegada");
                                myAgent.send(casasanta);
                                step = 8;
                            } else {
                                System.out.println("Error in the coversation protocol - step" + 2);
                                myAgent.doDelete();
                            }
                        }
                        case 8->{
                            ACLMessage response = myAgent.blockingReceive(MessageTemplate.MatchSender(new AID("santaClaus", AID.ISLOCALNAME)));
                            if (response != null && response.getPerformative() == ACLMessage.INFORM) {
                                System.out.println(response.getContent());
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