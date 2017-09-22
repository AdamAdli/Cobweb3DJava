package cobweb3d.core;

public interface SimulationTimeSpace extends RandomSource {

	public abstract long getTime();

	public abstract Topology getTopology();

}
