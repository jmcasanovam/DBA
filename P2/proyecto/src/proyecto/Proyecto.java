/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author jcasmar
 */
public class Proyecto extends Agent{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       //Entorno entorno= new Entorno("C:\\Users\\marco\\Downloads\\DBA\\DBA\\P2\\proyecto\\Pr2-maps\\mapWithComplexObstacle1.txt",
         //       7, 2, 5, 5);
       
         try {
            // Configurar el perfil para el contenedor de JADE
            Runtime rt = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.MAIN_PORT, "1100");

            // Crear el contenedor donde los agentes serán ejecutados
            AgentContainer container = rt.createAgentContainer(profile);

            // Crear y ejecutar el agente "Entorno" pasando los parámetros necesarios
            Object[] parameters = new Object[] {
                "C:\\Users\\mario\\OneDrive\\Escritorio\\Studies\\.MVR - 4º Ingeniería Informática\\Primer cuatrimestre\\Desarrollo Basado en Agentes\\Prácticas\\p2\\Pr2-maps\\mapWithComplexObstacle3.txt",
                8, 8, 0, 0  // Pasamos parámetros al constructor del agente
            };

            // Crear el agente Entorno y arrancarlo
            AgentController agenteEntorno = container.createNewAgent("Entorno", "proyecto.Entorno", parameters);
            agenteEntorno.start(); // Inicia el agente
            //entorno.ejecucion();

            System.out.println("Agente Entorno iniciado correctamente.");
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
        
   

       
    

    
    

