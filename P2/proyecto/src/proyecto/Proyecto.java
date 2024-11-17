package proyecto;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Proyecto extends Agent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            // Configurar el perfil para el contenedor de JADE
            Runtime rt = Runtime.instance();
            Profile profile = new ProfileImpl();

            // Crear el contenedor donde los agentes serán ejecutados
            AgentContainer container = rt.createAgentContainer(profile);

            // Crear y ejecutar el agente "Entorno" pasando los parámetros necesarios
            Object[] parameters = new Object[]{
                "/home/jcasmar/Documentos/4_IIFO/DBA/DBA_compartida/DBA/P2/Pr2-maps/mapWithComplexObstacle1.txt",
                7, 2, 5, 5 // Pasamos parámetros al constructor del agente
            };

            // Crear el agente Entorno y arrancarlo
            AgentController agenteEntorno = container.createNewAgent("Entorno", "proyecto.Entorno", parameters);
            agenteEntorno.start(); // Inicia el agente

            System.out.println("Agente Entorno iniciado correctamente.");
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
