package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Ball is an abstract class defining the generic characteristics of a ball.
 * @author Sam Leichter, Chris Chester, Campbell Letts
 */
public abstract class Ball extends ControllableObject {

    /**
     * Default constructor for Ball
     * @param balltype Type of Ball to create.
     */
    Ball(String balltype) {
        super(balltype, new Point2D.Double(0,0));
    }

    /**
     * Overloaded Constructor allowing position to be set as well
     * @param balltype
     * @param position
     */
    Ball(String balltype, Point2D.Double position) {
        super(balltype, position);
    }
    
    /**
     * A Ball bounces off the GoalHoops
     * @param hoop 
     */
    public void collisionEvent(GoalHoop hoop) {
        this.setRotationInstantaneous(this.getRotation() + Math.PI);
    }
}
