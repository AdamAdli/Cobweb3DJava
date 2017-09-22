package cobweb3d.model.location;

import cobweb3d.model.Topology;

public class LocationDirection extends Location {
	public final Direction direction;

	public LocationDirection(Location l, Direction d) {
		super(l.x, l.y, l.z);
		direction = d;
	}

	public LocationDirection(Location l) {
		this(l, Topology.NONE);
	}
}
