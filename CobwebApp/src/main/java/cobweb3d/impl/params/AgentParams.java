package cobweb3d.impl.params;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import io.CloneHelper;
import io.ConfDisplayName;
import io.ConfXMLTag;
import util.MutatableFloat;
import util.MutatableInt;

public class AgentParams implements ResizableParam {
    private static final long serialVersionUID = -7852361484228627542L;

    /**
     * Initial number of agents.
     */
    @ConfDisplayName("Initial count")
    @ConfXMLTag("Agents")
    public int initialAgents = 20;

    /**
     * Initial number of agents.
     */
    @ConfDisplayName("Color")
    @ConfXMLTag("Color")
    public String color = "";

    /**
     * Initial energy amount.
     */
    @ConfDisplayName("Initial energy")
    @ConfXMLTag("InitEnergy")
    public MutatableInt initEnergy = new MutatableInt(100);

    /**
     * Energy used to step forward.
     */
    @ConfDisplayName("Step energy")
    @ConfXMLTag("StepEnergy")
    public MutatableInt stepEnergy = new MutatableInt(1);

    /**
     * Amount of energy used to breed.
     */
    @ConfDisplayName("Breed energy")
    @ConfXMLTag("BreedEnergy")
    public MutatableInt breedEnergy = new MutatableInt(60);

    /**
     * Energy lost bumping into a rock/wall.
     */
    @ConfDisplayName("Rock bump energy")
    @ConfXMLTag("StepRockEnergy")
    public MutatableInt stepRockEnergy = new MutatableInt(2);

    /**
     * Energy lost bumping into another agent.
     */
    @ConfDisplayName("BaseAgent bump energy")
    @ConfXMLTag("StepAgentEnergy")
    public MutatableInt stepAgentEnergy = new MutatableInt(2);

    /**
     * Chance that bumping into another agent will result in sexual breeding.
     */
    @ConfDisplayName("Sexual breed chance")
    @ConfXMLTag("sexualBreedChance")
    public MutatableFloat sexualBreedChance = new MutatableFloat(1);

    /**
     * Chance an agent breeds asexually at a time step.
     */
    @ConfDisplayName("Asexual breed chance")
    @ConfXMLTag("asexualBreedChance")
    public MutatableFloat asexualBreedChance = new MutatableFloat(0);

    /**
     * Enable aging mode.
     */
    @ConfDisplayName("Aging")
    @ConfXMLTag("agingMode")
    public boolean agingMode = false;

    /**
     * Age limit after which the agent is forced to die.
     */
    @ConfDisplayName("Age limit")
    @ConfXMLTag("agingLimit")
    public MutatableInt agingLimit = new MutatableInt(300);

    public AgentParams() {

    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        // Resize stuff arrays and stuff that needs to be resize. Ex. Foodweb array downstream.
    }

    @Override
    public cobweb3d.core.params.BaseAgentParams clone() {
        try {
            cobweb3d.core.params.BaseAgentParams copy = (cobweb3d.core.params.BaseAgentParams) super.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
