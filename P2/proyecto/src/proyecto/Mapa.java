package proyecto;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jcasmar
 */
public class Mapa {
    private ArrayList<Integer> mapa = new ArrayList<>();
    private int alto;
    private int ancho;
    
    
    public Mapa() {
        this.alto = 0;
        this.ancho = 0;
    }
    
    public void leerMapa(String rutaArchivo){
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = br.readLine();
            if(linea != null){
                this.alto = Integer.parseInt(linea);
                linea = br.readLine();
                if(linea != null){
                    this.alto = Integer.parseInt(linea);
                    while ((linea = br.readLine()) != null) {
                       String[] lista = linea.split("\t");
                       for(String valor : lista){
                           this.mapa.add(Integer.parseInt(valor));
                       }
                    }
                }   
            }
            
        } catch (IOException e) {
            e.printStackTrace();  // Manejo de excepción si el archivo no existe o hay un error
        }
    }

    public ArrayList<Integer> getMapa() {
        return mapa;
    }
    
    
    
    public void mostrarMapa() {
        System.out.println("Mapa:");
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                System.out.print(mapa.get(i * ancho + j) + "\t");  // Imprimir cada valor con tabulador
            }
            System.out.println();  // Nueva línea después de cada fila
        }
    }
}
