package cobweb3d.model.location;

import java.io.Serializable;
import java.util.Objects;

/**
 * 3D direction represented as X, Y, Z deltas
 * -1 < x < 1
 * -1 < y < 1
 * -1 < z < 1
 */
public class Vector3Int implements Serializable {

    public final int x, y, z;

    public Vector3Int(int x, int y, int z) {
        this.x = Integer.signum(x);
        this.y = Integer.signum(y);
        this.z = Integer.signum(z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3Int direction = (Vector3Int) o;
        return x == direction.x &&
                y == direction.y &&
                z == direction.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    protected static final long serialVersionUID = 2L;
}
