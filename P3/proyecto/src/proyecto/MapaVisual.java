package proyecto;
import javax.swing.*;
import java.awt.*;

public class MapaVisual extends JPanel {
    private int[][] mapa;

    public MapaVisual(int[][] mapa) {
        this.mapa = mapa;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //int tileSize = 50; // Tamaño de cada celda
        int tileWidth = getWidth() / mapa[0].length;
        int tileHeight = getHeight() / mapa.length;
        int tileSize = Math.min(tileWidth, tileHeight);
        
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                // Dibuja diferentes colores según el valor en el mapa
                if (mapa[i][j] == 0) {
                    g.setColor(Color.WHITE); // Celda vacía
                } else if (mapa[i][j] == -1) {
                    g.setColor(Color.BLACK); // Muro
                } else if (mapa[i][j] == 1) {
                    g.setColor(Color.BLUE); // Jugador
                } else if (mapa[i][j] == -2) {
                    g.setColor(Color.RED); // Objetivo
                }

                // Dibuja el rectángulo de la celda
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                g.setColor(Color.GRAY); // Bordes
                g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }

    // Método para actualizar el mapa
    public void actualizarMapa(int[][] nuevoMapa) {
        this.mapa = nuevoMapa;
        repaint(); // Redibuja el panel con el nuevo mapa
    }

}