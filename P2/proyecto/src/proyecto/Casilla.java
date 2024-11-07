package proyecto;

/**
 *
 * @author jcasmar
 */
public class Casilla {
    private int fila;
    private int columna;
    private int pasos;//Numero de veces por las que ha pasado en esta casilla
    private int valor;//Representa si hay muro, si esta libre...
    private int heuristica;

    public Casilla(int fila, int columna, int valor, int objetivoFila, int objetivoColumna) {
        this.columna = columna;
        this.fila = fila;
        this.valor = valor;
        this.pasos = 0;
        calcularHeuristica(objetivoFila, objetivoColumna);
    }

    public Casilla(int fila, int columna, int valor) {
        this.columna = columna;
        this.fila = fila;
        this.valor = valor;
    }
    
    public Casilla(){
        this.valor = -3;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public void sumarPaso(){
        this.pasos++;
    }
    
    private void calcularHeuristica(int objetivoFila, int objetivoColumna){
        //Calcular mediante distancia Manhattan o similar
        this.heuristica = Math.abs((objetivoFila - fila) + (objetivoColumna - columna));
        
    }
    
    

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    public int getPasos() {
        return pasos;
    }

    public int getValor() {
        return valor;
    }

    public int getHeuristica() {
        return heuristica;
    }
    
    
    
}
