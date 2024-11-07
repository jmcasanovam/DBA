/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;

/**
 *
 * @author jcasmar
 */
public class Agente {
    private int filaActual;
    private int columnaActual;
    private int filaObjetivo;
    private int columnaObjetivo;    
    private ArrayList<Casilla> vision;

    public Agente(int filaActual, int columnaActual, int filaObjetivo, int columnaObjetivo) {
        this.filaActual = filaActual;
        this.columnaActual = columnaActual;
        this.filaObjetivo = filaObjetivo;
        this.columnaObjetivo = columnaObjetivo;
        this.vision = new ArrayList<>();
    }
    
    public void actualizarVision(ArrayList<Casilla> nuevaVision){
        this.vision = (ArrayList<Casilla>) nuevaVision.clone();
    }
    
    public int metodo1(){//para calcular la direccion nueva
        return 1;
    }
    
    public void moverse(){
        int direccion = metodo1();
        actualizarPosicion(direccion);
    }
    
    public void actualizarPosicion(int direccion){
        switch (direccion) {
            case 0:
                filaActual--;
                columnaActual--;
                break;
            case 1:
                filaActual--;
                break;
            case 2:
                filaActual--;
                columnaActual++;
                break;
            case 3:
                columnaActual++;
                break;
            case 4:
                filaActual++;
                columnaActual++;
                break;
            case 5:
                filaActual++;
                break;
            case 6:
                filaActual++;
                columnaActual--;
                break;
            case 7:
                columnaActual--;
                break;
            default:
                throw new AssertionError();
        }
    }

    public int getFilaActual() {
        return filaActual;
    }

    public int getColumnaActual() {
        return columnaActual;
    }
    
    
}
