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
        int tileSize = 50; // Tamaño de cada celda

        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                // Dibuja diferentes colores según el valor en el mapa
                if (mapa[i][j] == 0) {
                    g.setColor(Color.WHITE); // Celda vacía
                } else if (mapa[i][j] == -1) {
                    g.setColor(Color.BLACK); // Muro
                } else if (mapa[i][j] == 1) {
                    g.setColor(Color.BLUE); // Jugador
                } else if (mapa[i][j] == 2) {
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

    public static void main(String[] args) {
        // Mapa inicial
        int[][] mapaEjemplo = {
            {0, 0, -1, 0, 2},
            {0, -1, 0, 0, 0},
            {1, 0, 0, -1, 0},
            {0, 0, 0, 0, 0}
        };

        // Configuración de la ventana
        JFrame frame = new JFrame("Mapa Visual");
        MapaVisual panelMapa = new MapaVisual(mapaEjemplo);

        frame.add(panelMapa);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Simulación de movimientos del jugador
        new Timer(1000, e -> {
            // Actualizar posición del jugador (ejemplo)
            mapaEjemplo[2][0] = 0; // Borra la posición anterior
            mapaEjemplo[1][0] = 1; // Nueva posición del jugador
            panelMapa.actualizarMapa(mapaEjemplo); // Actualiza el mapa visual
        }).start();
    }
}
