/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto;

/**
 *
 * @author jcasmar
 */
public class Proyecto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mapa2 mapa = new Mapa2();
        mapa.leerMapa("/home/jcasmar/Documentos/4_IIFO/DBA/DBA_compartida/DBA/P2/Pr2-maps/mapWithComplexObstacle1.txt");
        mapa.mostrarMapa();
    }
    
}
