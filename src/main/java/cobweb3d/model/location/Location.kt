package cobweb3d.model.location

import java.io.Serializable

/**
 * Location on a 3D grid.
 */
data class Location(var x: Int, var y: Int, var z: Int) : Serializable {
    override fun toString(): String {
        return "($x,$y,$z)"
    }

    companion object {
        private const val serialVersionUID = 2L
    }
}
