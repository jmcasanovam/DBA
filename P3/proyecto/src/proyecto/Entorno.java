package proyecto;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
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

    public void ejecucion3 (){
        // establecer canal de comunicación con el elfo
        // mandar mensaje a elfo
        // recibir mensaje de elfo
        // establecer canal de comuncación con santa
        while(mensaje de santa es malo){
            // mandar mensaje a santa
            // recibirlo de santa
            // mandar mensaje a elfo
            // recibir mensaje de elfo
        }

        int contrasena = "por favor"; // guardar contraseña recibida de santa
        
        // establecer canal de comunicación con rudolph
        // pedir coordenadas a rudolf
        while(rudolf devuelve coordenadas /*implica mandarle la contraseña y recibir un mensaje*/){
            // rudolf te dara las coordenadas
            int filao = 0;
            int colo = 0;
            this.filaObjetivo=filao;
            this.columnaObjetivo=colo;
            jugador.setObjetivo(filao, colo);
            ejecucion();
            // pedir coordenadas de nuevo
        }
        // al recibir 
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
                @Override
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.setConversationId("elfo-channel");
                    myAgent.send(msg);

                    ACLMessage msg = myAgent.blockingReceive();

                    if(msg.getConversationId().equals())
                    
                }
            }
        );

            // Añadir el comportamiento de movimiento
                                                                        addBehaviour(new TickerBehaviour(this, 500) {
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
        }
    }

}
