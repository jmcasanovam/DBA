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

    public Casilla(int fila, int columna, int valor) {
        this.columna = columna;
        this.fila = fila;
        this.valor = valor;
        this.pasos = 0;
    }
    
    public Casilla(){
        this.valor = -3;
        this.pasos = 0;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public void sumarPaso(){
        this.pasos++;
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

    public void setPasos(int pasos) {
        this.pasos = pasos;
    }

    
    
    
    
}
