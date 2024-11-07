/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;
import java.util.Arrays;

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
    private int energia;

    public Agente(int filaActual, int columnaActual, int filaObjetivo, int columnaObjetivo) {
        this.filaActual = filaActual;
        this.columnaActual = columnaActual;
        this.filaObjetivo = filaObjetivo;
        this.columnaObjetivo = columnaObjetivo;
        this.vision = new ArrayList<>();
        this.energia = 0;
    }
    
    public void actualizarVision(ArrayList<Casilla> nuevaVision){
        this.vision = (ArrayList<Casilla>) nuevaVision.clone();
    }
    
    public int metodo1(){//para calcular la direccion nueva
        ArrayList<Integer> peso = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
        
        //Compruebo si tiene muro
        for(int i=0; i<8; i++){
            if(vision.get(i).getValor() == -1){
                peso.set(i, peso.get(i)-100);
            }
            
            //Restarle a cada casilla el numero de pasos que tenga x5
            peso.set(i, vision.get(i).getPasos()*-5);
        }
        
        //Compruebo si hay muros en la diagonal
        if(vision.get(1).getValor() == -1 && vision.get(7).getValor() == -1){
            peso.set(0, peso.get(0)-100);
        }
        if(vision.get(1).getValor() == -1 && vision.get(3).getValor() == -1){
            peso.set(2, peso.get(2)-100);
        }
        if(vision.get(3).getValor() == -1 && vision.get(5).getValor() == -1){
            peso.set(4, peso.get(4)-100);
        }
        if(vision.get(5).getValor() == -1 && vision.get(7).getValor() == -1){
            peso.set(6, peso.get(6)-100);
        }
        
        //Vamos a calcular la mejor opcion
        int x = filaActual - filaObjetivo;
        int y = columnaActual - columnaObjetivo;
        
        int absX = Math.abs(x);
        int absY = Math.abs(y);

        
        if(absX > absY){
            if(x>0 && y>0){//Arriba +4, Arriba Izq +3, Izq +2, Arriba Dch +1
                peso.set(1, peso.get(1)+4);
                peso.set(0, peso.get(0)+3);
                peso.set(7, peso.get(7)+2);
                peso.set(2, peso.get(2)+1);

            }
            else if(x>0 && y<0){//Arriba +4, Arriba Dcha +3, Dcha +2, Arriba Izq +1
                peso.set(1, peso.get(1)+4);
                peso.set(2, peso.get(2)+3);
                peso.set(3, peso.get(3)+2);
                peso.set(0, peso.get(0)+1);

            }
            else if(x<0 && y>0){//Abajo +4, Abajo Izq +3, Izq +2, Abajo Dcha +1
                peso.set(5, peso.get(5)+4);
                peso.set(6, peso.get(6)+3);
                peso.set(7, peso.get(5)+2);
                peso.set(4, peso.get(4)+1);

            }
            else{//Abajo +4, Abajo Dcha +3, Dcha +2, Abajo Izq +1
                peso.set(5, peso.get(5)+4);
                peso.set(4, peso.get(4)+3);
                peso.set(3, peso.get(3)+2);
                peso.set(6, peso.get(5)+1);

                
            }
        }
        else if(absX < absY){
            
        }
        else{
            if(x>0 && y>0){//Arriba Izq +2 y Izq/Arriba +1
                peso.set(0, peso.get(0)+2);
                peso.set(7, peso.get(7)+1);
                peso.set(1, peso.get(1)+1);
            }
            else if(x>0 && y<0){//Arriba Derecha +2 y Arriba/Derecha +1
                peso.set(2, peso.get(2)+2);
                peso.set(3, peso.get(3)+1);
                peso.set(1, peso.get(1)+1);
            }
            else if(x<0 && y>0){//Abajo Izq +2 y Izq/Abajo +1
                peso.set(6, peso.get(6)+2);
                peso.set(7, peso.get(7)+1);
                peso.set(5, peso.get(5)+1);
            }
            else{//Abajo Derecha +2 y Abajo/Dch +1
                peso.set(4, peso.get(4)+2);
                peso.set(5, peso.get(5)+1);
                peso.set(3, peso.get(3)+1);
                
            }
        }
        
        
        
        
        
        return 4;
    }
    
    public void moverse(){
        energia++;
        int direccion = metodo1();
        actualizarPosicion(direccion);
    }
    
    public void actualizarPosicion(int direccion){
        switch (direccion) {
            case 0://Arriba izquierda
                filaActual--;
                columnaActual--;
                break;
            case 1://Arriba
                filaActual--;
                break;
            case 2://Arriba derecha
                filaActual--;
                columnaActual++;
                break;
            case 3://Derecha
                columnaActual++;
                break;
            case 4://Abajo derecha
                filaActual++;
                columnaActual++;
                break;
            case 5://Abajo
                filaActual++;
                break;
            case 6://Abajo izquierda
                filaActual++;
                columnaActual--;
                break;
            case 7://Izquierda
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
    
    public void finalizar(){
        System.out.println("He acabado en "+ energia + " pasos.");
    }
    
    
}
