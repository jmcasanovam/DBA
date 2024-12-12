package proyecto;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import javax.swing.*;
import java.io.File;

import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.wrapper.ContainerController;


//public class Proyecto {
//
//    public static void main(String[] args) {
//
//        try {
//            // Crear un JFileChooser para seleccionar el archivo del mapa
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setDialogTitle("Selecciona un archivo de mapa");
//            int userSelection = fileChooser.showOpenDialog(null);
//
//            // Validar la selección del archivo
//            if (userSelection == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                String mapPath = selectedFile.getAbsolutePath();
//
//                // Verificar que el archivo sea un archivo de texto
//                if (!mapPath.endsWith(".txt")) {
//                    JOptionPane.showMessageDialog(null, "El archivo seleccionado no es un archivo de texto.", "Error", JOptionPane.ERROR_MESSAGE);
//                    return; // Finalizar el programa
//                }
//
//                // Solicitar al usuario las posiciones del agente y del objetivo
//                int agentRow = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la fila inicial del agente:"));
//                int agentCol = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la columna inicial del agente:"));
//                int goalRow = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la fila del  objetivo:"));
//                int goalCol = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la columna del objetivo:"));
//
//                // Configurar el perfil para el contenedor de JADE
//                Runtime rt = Runtime.instance();
//                Profile profile = new ProfileImpl();
//
//                // Crear el contenedor donde los agentes serán ejecutados
//                AgentContainer container = rt.createAgentContainer(profile);
//
//                // Crear y ejecutar el agente "Entorno" pasando los parámetros necesarios
//                Object[] parameters = new Object[]{
//                    mapPath, // Ruta del mapa seleccionada
//                    agentRow, agentCol, // Posición inicial del agente
//                    goalRow, goalCol // Posición objetivo
//                };
//
//                // Crear el agente Entorno y arrancarlo
//                AgentController agenteEntorno = container.createNewAgent("Entorno", "proyecto.Entorno", parameters);
//                agenteEntorno.start(); // Inicia el agente
//
//                System.out.println("Agente Entorno iniciado correctamente.");
//            } else {
//                System.out.println("No se seleccionó ningún archivo. El programa se cerrará.");
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(null, "Entrada no válida. El programa se cerrará.", "Error", JOptionPane.ERROR_MESSAGE);
//        } catch (StaleProxyException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

// Agente MiAgente que envía mensajes


public class Proyecto {

    public static void main(String[] args) {
        try {
            // Inicializar JADE Runtime
            Runtime rt = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.MAIN_HOST, "localhost"); // Host local
            profile.setParameter(Profile.CONTAINER_NAME, "contenedor1");
            
            ContainerController cc = rt.createAgentContainer(profile);

            // Lanzamiento de agentes
            try {
                AgentController miAgente = cc.createNewAgent("miAgente", "proyecto.MiAgente", null);
                miAgente.start();
                cc.createNewAgent("santaClaus", "proyecto.Santa", null).start();
                cc.createNewAgent("elfo", "proyecto.Elfo", null).start();
                cc.createNewAgent("rudolf", "proyecto.Rudolf", null).start();



            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




