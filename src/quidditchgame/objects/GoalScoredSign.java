package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Used to display a sign when a goal is scored
 * @author Matt
 */
public class GoalScoredSign extends DrawnObject {
    
    /**
     * Allocate the GoalScoredSign a sprite image location as well as starting
     * position
     *
     * @param position the position of the highlight sprite on the field
     */
    public GoalScoredSign(Point2D.Double position) {
        super("GoalScored", position);
    }
}
