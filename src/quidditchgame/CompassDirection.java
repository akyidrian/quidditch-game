package quidditchgame;

/**
 * Defines the main 8 points of direction on a compass, to be used for 
 * convenience when setting rotation.
 * @author Campbell Letts
 */
public enum CompassDirection {

    // 8 Points of the Compass, with North as 0 radians.
    NORTH(0),
    NORTH_EAST(Math.PI / 4),
    EAST(Math.PI / 2),
    SOUTH_EAST(3 * Math.PI / 4),
    SOUTH(Math.PI),
    SOUTH_WEST(5 * Math.PI / 4),
    WEST(3 * Math.PI / 2),
    NORTH_WEST(7 * Math.PI / 4);
    
    // Stored as type Double [radians]
    private final double radians;
    
    CompassDirection(double radians) {
        this.radians = radians;
    }
    
    // Call CompassDirection.West.toRadians() to convert to value.
    public double toRadians() {
        return radians;
    }
}