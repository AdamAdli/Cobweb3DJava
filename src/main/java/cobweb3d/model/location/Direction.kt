package cobweb3d.model.location

import java.io.Serializable

/**
 * 3D direction represented as X, Y, Z deltas
 * -1 < x < 1
 * -1 < y < 1
 * -1 < z < 1
 */
data class Direction(var x: Int, var y: Int, var z: Int) : Serializable {

    override fun toString(): String {
        return "($x,$y,$z)"
    }

    infix fun or(otherDir: Direction): Direction {
        return Direction(this.x.or(otherDir.x), this.y.or(otherDir.y), this.z.or(otherDir.z))
    }

    companion object {
        @JvmField
        val serialVersionUID = 2L
        @JvmField
        val NONE = Direction(0, 0, 0)
        @JvmField
        val NORTH = Direction(0, 0, 1)
        @JvmField
        val EAST = Direction(1, 0, 0)
        @JvmField
        val SOUTH = Direction(0, 0, -1)
        @JvmField
        val WEST = Direction(-1, 0, 0)
        @JvmField
        val UP = Direction(0, 1, 0)
        @JvmField
        val DOWN = Direction(0, -1, 0)
    }
}
