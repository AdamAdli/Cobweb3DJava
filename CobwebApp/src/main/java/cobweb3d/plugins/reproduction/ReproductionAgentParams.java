package cobweb3d.plugins.reproduction;

import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;
import util.MutatableFloat;
import util.MutatableInt;

public class ReproductionAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 12L;
    /**
     * Chance that bumping into another agent will result in sexual breeding.
     */
    @ConfDisplayName("Sexual reproduction chance")
    @ConfXMLTag("sexualBreedChance")
    public MutatableFloat sexualBreedChance = new MutatableFloat(1);
    /**
     * Chance an agent breeds asexually at a time step.
     */
    @ConfDisplayName("Asexual reproduction chance")
    @ConfXMLTag("asexualBreedChance")
    public MutatableFloat asexualBreedChance = new MutatableFloat(0);
    /**
     * Amount of energy used to reproduction.
     */
    @ConfDisplayName("Breed energy")
    @ConfXMLTag("BreedEnergy")
    public MutatableInt breedEnergy = new MutatableInt(60);
    /**
     * Time between sexual breeding and producing child agent.
     */
    @ConfDisplayName("Sexual pregnancy period")
    @ConfXMLTag("sexualPregnancyPeriod")
    public MutatableInt sexualPregnancyPeriod = new MutatableInt(5);


    /*
    /**
     * Agent types this agent can transmit the disease to.
     *
    @ConfDisplayName("Reproduce with to")
    @ConfXMLTag("transmitTo")
    @ConfList(indexName = "Agent", startAtOne = true)
    public boolean[] transmitTo = new boolean[0];*/
    /**
     * Time between asexual breeding and producing child agent.
     */
    @ConfDisplayName("Asexual pregnancy period")
    @ConfXMLTag("pregnancyPeriod")
    public MutatableInt asexPregnancyPeriod = new MutatableInt(0);

    //@Deprecated // for reflection use only!
    public ReproductionAgentParams() {
    }
}