package cobweb3d.io;

import cobweb3d.impl.SimulationConfig;
import io.ParameterSerializer;
import org.w3c.dom.Node;

import java.io.InputStream;


public class Cobweb3Serializer {

    public static SimulationConfig loadConfig(InputStream file) {
        return loadFile(file);
    }

    /**
     * This method extracts data from the simulation configuration file and
     * loads the data into the simulation parameters.  It does this by first
     * creating a tree that holds all data from file using the DocumentBuilder
     * class.  Next, the root node of the tree is passed to the
     * AbstractReflectionParams.loadConfig(Node) method for processing.  This
     * processing allows the ConfXMLTags to overwrite the default parameters
     * used when constructing Cobweb environment parameters.
     * <p>
     * <p>Once the environment parameters have been extracted successfully,
     * the rest of the Cobweb parameters can be set (temperature, genetics,
     * agents, etc.) using the environment parameters.
     *
     * @param file The current simulation configuration file.
     * @throws IllegalArgumentException Unable to open the simulation configuration file.
     * @see javax.xml.parsers.DocumentBuilder
     */
    public static SimulationConfig loadFile(InputStream file) throws IllegalArgumentException {
        Node root = CobwebXmlHelper.openDocument(file);
        SimulationConfig simConfig = new SimulationConfig();
        ParameterSerializer parameterSerializer = new ParameterSerializer(null);

        // Load all the @ConfXMLTag params
        parameterSerializer.load(simConfig, root);

        // Correct any missing/extra parameters after the loading
        // TODO: conf.setAgentTypes(conf.getAgentTypes());

        return simConfig;
    }
}
