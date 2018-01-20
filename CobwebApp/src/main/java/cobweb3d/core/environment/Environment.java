package cobweb3d.core.environment;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.Updatable;
import cobweb3d.core.agent.Agent;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.impl.params.AgentParams;
import cobweb3d.impl.params.BaseAgentParams;
import cobweb3d.impl.params.EnvironmentParams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * 3D environment.
 */
public class Environment implements Updatable {
    public EnvironmentParams environmentParams;
    public BaseAgentParams[] agentParams;
    public Topology topology;
    public int FLAG_AGENT = 0x01;
    public int FLAG_STONE = 0x02;
    protected SimulationInternals simulation;
    /**
     * The implementation uses a hash table to store agents, as we assume there are many more locations than agents.
     */
    protected Map<Location, Agent> agentTable;
    protected byte[][][] flagArray; // TODO

    public Environment(SimulationInternals simulation) {
        this.simulation = simulation;
        this.agentTable = new Hashtable<>();
        flagArray = new byte[0][0][0];
    }

    public synchronized void setParams(EnvironmentParams envParams, AgentParams agentParams) throws IllegalArgumentException {
        this.environmentParams = envParams;
        this.agentParams = agentParams.agentParams;
        this.topology = new Topology(simulation, environmentParams.width, environmentParams.height, envParams.depth, false);
        this.flagArray = new byte[topology.width][topology.height][topology.depth];
    }

    private byte getLocationBits(Location l) {
        return flagArray[l.x][l.y][l.z];
    }

    private void setLocationBits(Location l, byte bits) {
        flagArray[l.x][l.y][l.z] = bits;
    }

    /**
     * Flags locations as a food/stone/waste location. It does nothing if
     * the square is already occupied (for example, setFlag((0,0),FOOD,true)
     * does nothing when (0,0) is a stone
     */
    protected void setFlag(Location l, byte flag, boolean state) {
        int flagBits = 1 << (flag - 1);

        assert (!(state && getLocationBits(l) != 0)) : "Attempted to set flag when location flags non-zero: " + getLocationBits(l);
        assert (!(!state && (getLocationBits(l) & flagBits) == 0)) : "Attempting to unset an unset flag" + flagBits;

        byte newValue = getLocationBits(l);

        if (state)
            newValue |= flagBits;
        else
            newValue &= ~flagBits;

        setLocationBits(l, newValue);
    }

    protected boolean testFlag(Location l, byte flag) {
        int flagBits = 1 << (flag - 1);
        return (getLocationBits(l) & flagBits) != 0;
    }

    protected void clearFlag(byte flag) {
        for (int x = 0; x < topology.width; ++x) {
            for (int y = 0; y < topology.height; ++y) {
                for (int z = 0; z < topology.depth; ++z) {
                    Location currentPos = new Location(x, y, z);
                    if (testFlag(currentPos, flag)) {
                        setFlag(currentPos, flag, false);
                    }
                }
            }
        }
    }

    public boolean isOccupied(Location l) {
        return getLocationBits(l) != 0 || hasAgent(l);
    }

    public boolean hasAgent(Location l) {
        if (l == null) return false;
        return agentTable != null && agentTable.containsKey(l);
    }

    public void clearAgents() {
        for (Agent a : new ArrayList<>(getAgents())) {
            a.die();
        }
        agentTable.clear();
    }

    public Agent getAgent(Location l) {
        return agentTable.get(l);
    }

    public synchronized Collection<Agent> getAgents() {
        return agentTable.values();
    }

    public int getAgentCount() {
        return agentTable.size();
    }

    public final void setAgent(Location l, Agent a) {
        if (a != null) agentTable.put(l, a);
        else agentTable.remove(l);
    }

    public synchronized void removeAgent(Location l) {
        Agent a = getAgent(l);
        if (a != null) a.die();
    }

    protected void killOffgridAgents() {
        for (Agent a : new ArrayList<>(getAgents())) {
            Location l = a.getPosition();
            if (l.x >= topology.width || l.x < 0 || l.y >= topology.height || l.y < 0 || l.z >= topology.depth || l.z < 0) {
                a.die();
            }
        }
    }

    public Collection<Location> getEmptyNearLocations(Location position) {
        Collection<Location> result = new ArrayList<>(8);
        Direction direction = new Direction(0, 0, 0);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location location = topology.getAdjacent(position, (Direction) direction.set(x, y, z));
                    if (location != null && testFlag(location, (byte) 0)) {
                        result.add(location);
                    }
                }
            }
        }
        return result;
    }
}
