package proyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jcasmar
 * Hace la funcion de entorno
 */
public class Mapa2 {
    private ArrayList<Casilla> mapa;
    private int alto;
    private int ancho;

    public Mapa2() {
        this.mapa = new ArrayList<>();
        this.alto = 0;
        this.ancho = 0;
    }
    
    public void leerMapa(String rutaArchivo){
        int fila = 0;
        int columna = 0;
        Casilla temp;
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = br.readLine();
            if(linea != null){
                this.alto = Integer.parseInt(linea);
                linea = br.readLine();
                if(linea != null){
                    this.ancho = Integer.parseInt(linea);
                    while ((linea = br.readLine()) != null) {
                       String[] lista = linea.split("\t");
                       for(String valor : lista){
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
        System.out.println("Mapa2:");
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                System.out.print(mapa.get(i * ancho + j).getValor() + "\t");  // Imprimir cada valor con tabulador
            }
            System.out.println();  // Nueva linea despues de cada fila
        }
    }
    
    
    
    
    
}
