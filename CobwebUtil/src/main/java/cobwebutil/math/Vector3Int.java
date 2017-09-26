package cobwebutil.math;

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
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vector3F average(Vector3F other) {
        if (other == null) other = Vector3F.ZERO;
        return new Vector3F((other.x - this.x) / 2, (other.y - this.y) / 2, (other.z - this.z) / 2);
    }

    public Vector3F invertSign() {
        return new Vector3F(-x, -y, -z);
    }

    public Vector3F plus(Vector3F other) {
        if (other == null) other = Vector3F.ZERO;
        return new Vector3F(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3F minus(Vector3F other) {
        return plus(other.invertSign());
    }

    public float length() {
        return (float) Math.sqrt((x*x) + (y*y) + (z*z));
    }
}
