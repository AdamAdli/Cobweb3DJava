package cobweb3d.model.location

import java.io.Serializable

data class LocationDirection(var location: Location, var direction: Direction) : Serializable {
    constructor(location: Location) : this(location, Direction.NONE)

    companion object {
        private const val serialVersionUID = 2L
    }
}
