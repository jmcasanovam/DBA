package proyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jcasmar Hace la funcion de entorno
 */
public class Entorno {

    private ArrayList<Casilla> mapa;
    private int alto;
    private int ancho;
    private String nombreArchivo;

    private int filaObjetivo;
    private int columnaObjetivo;
    private Agente jugador;

    public Entorno(String nombreArchivo, int filaJ, int columnaJ, int filaO, int columnaO) {

        this.mapa = new ArrayList<>();
        this.alto = 0;
        this.ancho = 0;
        this.nombreArchivo = nombreArchivo;

        leerMapa();

        if (filaJ < 0 || columnaJ < 0 || filaO < 0 || columnaO < 0 || filaJ >= this.alto || columnaJ >= this.ancho || filaO >= alto || columnaO >= ancho) {
            throw new IllegalArgumentException("Las coordenadas están fuera de los límites del mapa.");
        }

        filaObjetivo = filaO;
        columnaObjetivo = columnaO;

        this.jugador = new Agente(filaJ, columnaJ, filaObjetivo, columnaObjetivo);
        mostrarMapa();
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

    public void ejecucion() {
        while (jugador.getFilaActual() != filaObjetivo || jugador.getColumnaActual() != columnaObjetivo) {
            mapa.get(jugador.getFilaActual()*ancho + jugador.getColumnaActual()).sumarPaso();
//            for (int i=0; i<ancho; i++){
//                for(int j=0; j<alto; j++){
//                    System.out.print(mapa.get(i*ancho+j).getPasos()+" ");
//                }
//                System.out.println();
//            }
            
            
            cargarVision();
            jugador.moverse();
            mostrarMapa();
        }
        jugador.finalizar();
    }

    public void cargarVision() {
        //Cargar la vision. Si esta en los bordes, se carga como -1 (muro)
        ArrayList<Casilla> vision = new ArrayList<>();
        
        for (int i = 0; i< 8; i++)
            vision.add(new Casilla());
        
        if(jugador.getFilaActual() == 0){
            for(int i = 0; i<3; i++){
                vision.get(i).setValor(-1);
            }
        }
        if(jugador.getColumnaActual() == 0){
            for(int i = 0; i<3; i++){
                vision.get((i+6) % 8).setValor(-1);
            }
        }
        
        if(jugador.getFilaActual() == alto - 1){
            for(int i = 4; i<7; i++){
                vision.get(i).setValor(-1);
            }
        }
        if(jugador.getColumnaActual() == ancho -1){
            for(int i = 2; i<5; i++){
                vision.get(i).setValor(-1);
            }
        }

        if(vision.get(0).getValor() == -3){
            vision.get(0).setValor(getValorMapa(jugador.getFilaActual()-1, jugador.getColumnaActual()-1));
            vision.get(0).setPasos(mapa.get((jugador.getFilaActual()-1)*ancho + jugador.getColumnaActual()-1).getPasos());
        }
        if(vision.get(1).getValor() == -3){
            vision.get(1).setValor(getValorMapa(jugador.getFilaActual()-1, jugador.getColumnaActual()));
            vision.get(1).setPasos(mapa.get((jugador.getFilaActual()-1)*ancho + jugador.getColumnaActual()).getPasos());

        }
        if(vision.get(2).getValor() == -3){
            vision.get(2).setValor(getValorMapa(jugador.getFilaActual()-1, jugador.getColumnaActual()+1));
            vision.get(2).setPasos(mapa.get((jugador.getFilaActual()-1)*ancho + jugador.getColumnaActual()+1).getPasos());
        }
        
        if(vision.get(3).getValor() == -3){
            vision.get(3).setValor(getValorMapa(jugador.getFilaActual(), jugador.getColumnaActual()+1));
            vision.get(3).setPasos(mapa.get((jugador.getFilaActual())*ancho + jugador.getColumnaActual()+1).getPasos());
        }
        if(vision.get(4).getValor() == -3){
            vision.get(4).setValor(getValorMapa(jugador.getFilaActual()+1, jugador.getColumnaActual()+1));
            vision.get(4).setPasos(mapa.get((jugador.getFilaActual()+1)*ancho + jugador.getColumnaActual()+1).getPasos());
        }
        if(vision.get(5).getValor() == -3){
            vision.get(5).setValor(getValorMapa(jugador.getFilaActual()+1, jugador.getColumnaActual()));
            vision.get(5).setPasos(mapa.get((jugador.getFilaActual()+1)*ancho + jugador.getColumnaActual()).getPasos());
        }
        if(vision.get(6).getValor() == -3){
            vision.get(6).setValor(getValorMapa(jugador.getFilaActual()+1, jugador.getColumnaActual()-1));
            vision.get(6).setPasos(mapa.get((jugador.getFilaActual()+1)*ancho + jugador.getColumnaActual()-1).getPasos());
        }
        if(vision.get(7).getValor() == -3){
            vision.get(7).setValor(getValorMapa(jugador.getFilaActual(), jugador.getColumnaActual()-1));
            vision.get(7).setPasos(mapa.get((jugador.getFilaActual())*ancho + jugador.getColumnaActual()-1).getPasos());
        }

        this.jugador.actualizarVision(vision);
        
    }

    private int getValorMapa(int fila, int columna) {
        return mapa.get(fila * ancho + columna).getValor();
    }
    
    

}
