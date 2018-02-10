package cobweb3d.plugins.reproduction;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.*;
import util.ArrayUtilities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ReproductionMutator extends StatefulMutatorBase<ReproductionState> implements ContactMutator,
        StepMutator, UpdateMutator, LoggingMutator {
    ReproductionParams params;

    private int[] birthCounts = new int[0];

    private SimulationTimeSpace simulation;

    public ReproductionMutator() {
        super(ReproductionState.class);
    }

    public void setParams(SimulationTimeSpace sim, ReproductionParams reproductionParams, int agentTypes) {
        this.simulation = sim;
        this.params = reproductionParams;
        this.birthCounts = ArrayUtilities.resizeArray(birthCounts, agentTypes);
    }

    @Override
    public Collection<String> logDataAgent(int agentType) {
        List<String> l = new LinkedList<String>();
        l.add(Integer.toString(birthCounts[agentType]));
        return l;
    }

    @Override
    public Collection<String> logDataTotal() {
        List<String> l = new LinkedList<String>();
        int sum = 0;
        for (int x : birthCounts)
            sum += x;
        l.add(Integer.toString(sum));
        return l;
    }

    @Override
    public Collection<String> logHeadersAgent() {
        List<String> header = new LinkedList<String>();
        header.add("Births");
        return header;
    }

    @Override
    public Collection<String> logHeaderTotal() {
        List<String> header = new LinkedList<String>();
        header.add("Births");
        return header;
    }

    private void tryGiveBirth(BaseAgent agent, Location location) {
        if (agent.enoughEnergy(params.of(agent).breedEnergy.getValue()) && getPregnancyPeriod(agent) <= 0) {
            LocationDirection breedPos = new LocationDirection(location, agent.getPosition().direction);
            BaseAgent breedPartner;
            if ((breedPartner = getBreedPartner(agent)) != null) {
                agent.createChildSexual(breedPos, breedPartner);
                setBreedPartner(agent, null);
            } else agent.createChildAsexual(breedPos);
            setPregnant(agent, false);
            if (agent instanceof Agent) {
                agent.changeEnergy(params.of(agent).breedEnergy.getValue(), new Agent.ReproductionCause());
            }
        }
    }

    private void tryAsexualBreed(BaseAgent agent) {
        int breedEnergy = params.of(agent).breedEnergy.getValue();
        if ((breedEnergy == 0 || agent.enoughEnergy(breedEnergy)) && params.of(agent).asexualBreedChance.getValue() != 0.0
                && simulation.getRandom().nextFloat() < params.of(agent).asexualBreedChance.getValue()) {
            setAgentState(agent, ReproductionState.makePregnantState(params.of(agent).asexPregnancyPeriod.getValue(), null));
        }
    }

    private void trySexualBreed(BaseAgent mother, BaseAgent father) {
        if (mother.getType() == father.getType()) {
            // TODO: double sim = 0.0;
            boolean canBreed = !isPregnant(mother) && mother.enoughEnergy(params.of(mother).breedEnergy.getValue()) && params.of(mother).sexualBreedChance.getValue() != 0.0
                    && simulation.getRandom().nextFloat() < params.of(mother).sexualBreedChance.getValue();
            // TODO: Calculate Similarity sim = bumper, check if agent good.
            if (canBreed)
                setAgentState(mother, ReproductionState.makePregnantState(params.of(mother).asexPregnancyPeriod.getValue(), father));
        }
    }

    public boolean isPregnant(BaseAgent agent) {
        return hasAgentState(agent) && getAgentState(agent).pregnant;
    }

    public int getPregnancyPeriod(BaseAgent agent) {
        return hasAgentState(agent) ? getAgentState(agent).pregPeriod : 0;
    }

    public BaseAgent getBreedPartner(BaseAgent agent) {
        return hasAgentState(agent) ? getAgentState(agent).breedPartner : null;
    }

    public void setPregnant(BaseAgent agent, boolean isPregnant) {
        if (hasAgentState(agent)) getAgentState(agent).pregnant = isPregnant;
    }

    public void setPregnancyPeriod(BaseAgent agent, int pregnancyPeriod) {
        if (hasAgentState(agent)) getAgentState(agent).pregPeriod = pregnancyPeriod;
    }

    public void setBreedPartner(BaseAgent agent, BaseAgent breedPartner) {
        if (hasAgentState(agent)) getAgentState(agent).breedPartner = breedPartner;
    }

    @Override
    protected boolean validState(ReproductionState value) {
        return value != null;
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        trySexualBreed(bumper, bumpee);
    }

    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {
        if (isPregnant(agent)) tryGiveBirth(agent, from);
    }

    @Override
    public void onUpdate(BaseAgent agent) {
        if (isPregnant(agent)) getAgentState(agent).pregPeriod--;
        else tryAsexualBreed(agent);
    }
}
