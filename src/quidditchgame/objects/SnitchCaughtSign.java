package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Used to display a sign when a goal is scored
 * @author Matt
 */
public class SnitchCaughtSign extends DrawnObject {
    
    /**
     * Allocate the Smoke a sprite image
     *
     * @param position the position of the snitch caught text.
     */
    public SnitchCaughtSign(Point2D.Double position) {
        super("SnitchCaught", position);
    }
}
