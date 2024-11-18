/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

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
                peso.set(i, -100);
            }
            
            //Restarle a cada casilla el numero de pasos que tenga x5
            peso.set(i,peso.get(i) + vision.get(i).getPasos()*(-10));
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
            if(x>0 && y>0){//Arriba +7, Arriba Izq +6, Izq +5, Arriba Dcha +4, Abajo Izq +3, Dcha +2, Abajo +1
                peso.set(1, peso.get(1)+7);
                peso.set(0, peso.get(0)+6);
                peso.set(7, peso.get(7)+5);
                peso.set(2, peso.get(2)+4);
                peso.set(6, peso.get(6)+3);
                peso.set(3, peso.get(3)+2);
                peso.set(5, peso.get(5)+1);

            }
            else if(x>0 && y<0){//Arriba +7, Arriba Dcha +6, Dcha +5, Arriba Izq +4, Abajo Dcha +3, Izq +2, Abajo +1
                peso.set(1, peso.get(1)+7);
                peso.set(2, peso.get(2)+6);
                peso.set(3, peso.get(3)+5);
                peso.set(0, peso.get(0)+4);
                peso.set(4, peso.get(4)+3);
                peso.set(7, peso.get(7)+2);
                peso.set(5, peso.get(5)+1);

            }
            else if(x<0 && y>0){//Abajo +7, Abajo Izq +6, Izq +5, Abajo Dcha +4, Arriba Izq +3, Dcha +2, Arriba +1
                peso.set(5, peso.get(5)+7);
                peso.set(6, peso.get(6)+6);
                peso.set(7, peso.get(5)+5);
                peso.set(4, peso.get(4)+4);
                peso.set(0, peso.get(0)+3);
                peso.set(3, peso.get(3)+2);
                peso.set(1, peso.get(1)+1);

            }
            else if(x<0 && y==0){//Abajo +4, Abajo Izq +3, Abajo Dcha +3, Izq +2, Dcha +2, Arriba Izq +1, Arriba Dcha +1
                peso.set(5, peso.get(5)+4);
                peso.set(6, peso.get(6)+3);
                peso.set(4, peso.get(4)+3);
                peso.set(7, peso.get(7)+2);
                peso.set(3, peso.get(3)+2);
                peso.set(0, peso.get(0)+1);
                peso.set(2, peso.get(2)+1);

            }
            else if(x>0 && y==0){//Arriba +4, Arriba Izq +3, Arriba Dcha +3, Izq +2, Dcha +2, Abajo Izq +1, Abajo Dcha +1
                peso.set(1, peso.get(1)+4);
                peso.set(0, peso.get(0)+3);
                peso.set(2, peso.get(2)+3);
                peso.set(3, peso.get(3)+2);
                peso.set(7, peso.get(7)+2);
                peso.set(0, peso.get(0)+1);
                peso.set(2, peso.get(2)+1);
            }
            else{//Abajo +7, Abajo Dcha +6, Dcha +5, Abajo Izq +4, Arriba Dcha +3, Izq +2, Arriba +1
                peso.set(5, peso.get(5)+7);
                peso.set(4, peso.get(4)+6);
                peso.set(3, peso.get(3)+5);
                peso.set(6, peso.get(6)+4);
                peso.set(2, peso.get(2)+3);
                peso.set(7, peso.get(7)+2);
                peso.set(1, peso.get(1)+1);
                
            }
        }
        else if(absX < absY){
            if(x>0 && y>0){//Izq +7, Arriba Izq +6, Arriba +5, Abajo Izq +4, Arriba Dcha +3, Abajo +2, Dcha +1
                peso.set(7, peso.get(7)+7);
                peso.set(0, peso.get(0)+6);
                peso.set(1, peso.get(1)+5);
                peso.set(6, peso.get(6)+4);
                peso.set(2, peso.get(2)+3);
                peso.set(5, peso.get(5)+2);
                peso.set(3, peso.get(3)+1);

            }
            else if(x>0 && y<0){//Dcha +7, Arriba Dcha +6, Arriba +5, Abajo Dcha +4, Arriba Izq +3, Abajo +2, Izq +1
                peso.set(3, peso.get(3)+7);
                peso.set(2, peso.get(2)+6);
                peso.set(1, peso.get(1)+5);
                peso.set(4, peso.get(4)+4);
                peso.set(0, peso.get(0)+3);
                peso.set(5, peso.get(5)+2);
                peso.set(7, peso.get(7)+1);

            }
            else if(x<0 && y>0){//Izq +7, Abajo Izq +6, Abajo +5, Arriba Izq +4, Abajo Dcha +3, Arriba +2, Dcha +1
                peso.set(7, peso.get(7)+7);
                peso.set(6, peso.get(6)+6);
                peso.set(5, peso.get(5)+5);
                peso.set(0, peso.get(0)+4);
                peso.set(4, peso.get(4)+3);
                peso.set(1, peso.get(1)+2);
                peso.set(3, peso.get(3)+1);

            }
            else if(x==0 && y>0){//Izq +4, Abajo Izq +3, Arriba Izq +3, Arriba +2, Abajo +2, Arriba Dcha +1, Abajo Dcha +1
                peso.set(7, peso.get(7)+4);
                peso.set(6, peso.get(6)+3);
                peso.set(0, peso.get(0)+3);
                peso.set(1, peso.get(1)+2);
                peso.set(5, peso.get(5)+2);
                peso.set(2, peso.get(2)+1);
                peso.set(4, peso.get(4)+1);

            }
            else if(x==0 && y<0){//Dcha +4, Abajo Dcha +3, Arriba Dcha +3, Arriba +2, Abajo +2, Arriba Izq +1, Abajo Izq +1
                peso.set(3, peso.get(3)+4);
                peso.set(4, peso.get(4)+3);
                peso.set(2, peso.get(2)+3);
                peso.set(1, peso.get(1)+2);
                peso.set(5, peso.get(5)+2);
                peso.set(0, peso.get(0)+1);
                peso.set(6, peso.get(6)+1);
            }
            else{//Dcha +7, Abajo Dcha +6, Abajo +5, Arriba Dcha +4, Abajo Izq +3, Arriba +2, Izq +1
                peso.set(3, peso.get(3)+7);
                peso.set(4, peso.get(4)+6);
                peso.set(5, peso.get(5)+5);
                peso.set(2, peso.get(2)+4);
                peso.set(6, peso.get(6)+3);
                peso.set(1, peso.get(1)+2);
                peso.set(7, peso.get(7)+1);
                
            }
        }
        else{
            if(x>0 && y>0){//Arriba Izq +4, Arriba +3, Izq +3, Arriba Dcha +2, Abajo Izq +2, Dcha +1, Abajo +1
                peso.set(0, peso.get(0)+4);
                peso.set(1, peso.get(1)+3);
                peso.set(7, peso.get(7)+3);
                peso.set(2, peso.get(2)+2);
                peso.set(6, peso.get(6)+2);
                peso.set(3, peso.get(3)+1);
                peso.set(5, peso.get(5)+1);
                
            }
            else if(x>0 && y<0){//Arriba Derecha +4, Arriba+3, Derecha +3, Arriba Izq+2, Abajo Dcha +2, Izq +1, Abajo +1
                peso.set(2, peso.get(2)+4);
                peso.set(1, peso.get(1)+3);
                peso.set(3, peso.get(3)+3);
                peso.set(0, peso.get(0)+2);
                peso.set(4, peso.get(4)+2);
                peso.set(7, peso.get(7)+1);
                peso.set(5, peso.get(5)+1);
            }
            else if(x<0 && y>0){//Abajo Izq +4, Abajo +3, Izq +3, Abajo Dcha +2, Arriba Izq +2, Dcha +1, Arriba +1
                peso.set(6, peso.get(6)+4);
                peso.set(7, peso.get(7)+3);
                peso.set(5, peso.get(5)+3);
                peso.set(0, peso.get(0)+2);
                peso.set(4, peso.get(4)+2);
                peso.set(1, peso.get(1)+1);
                peso.set(3, peso.get(3)+1);
            }
            else{//Abajo Derecha +4, Abajo+3, Derecha +3, Abajo Izq+2, Arriba Dcha +2, Izq +1, Arriba +1
                peso.set(4, peso.get(4)+4);
                peso.set(3, peso.get(3)+3);
                peso.set(5, peso.get(5)+3);
                peso.set(6, peso.get(6)+2);
                peso.set(2, peso.get(2)+2);
                peso.set(7, peso.get(7)+1);
                peso.set(1, peso.get(1)+1);
                
            }
        }
        
        //Buscamos el mejor
        int mejor = 0;
        int valor = peso.get(0);
        for(int i=1; i< 8; i++ ){
            if(peso.get(i) > valor){
                valor = peso.get(i);
                mejor = i;
            }
        }
        
        return mejor;
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
        JOptionPane.showMessageDialog(null, "He acabado en "+ energia + " pasos.", "¡Objetivo alcanzado!", JOptionPane.PLAIN_MESSAGE);
    }
    
    
}
