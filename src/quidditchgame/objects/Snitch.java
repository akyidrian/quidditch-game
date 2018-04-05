package quidditchgame.objects;

import java.awt.Graphics;
import quidditchgame.CompassDirection;
import quidditchgame.controllers.snitchai.SnitchAI;

/**
 * Snitch defines the Snitch's characteristics and its reaction to collisions
 *
 * @author Chris Chester, Campbell Letts
 */
public class Snitch extends Ball {

    protected boolean possessedStatus = false;

    /**
     * Allocate the Snitch a sprite image location as well as starting position
     *
     * @param imageLocation
     * @param position
     */
    public Snitch() {
        super("snitch");

        setController(new SnitchAI(this));
        setDesiredRotation(CompassDirection.NORTH);
        this.mass = 0.01;
        setMaxRotationChange(Math.PI / 8);
        
        setDesiredSpeed(5);
        setMaxSpeed(5);
        
    }

    /**
     * Returns whether the Snitch is possessed by a player
     *
     * @return possessedStatus the possession status.
     */
    public Boolean getPossessedStatus() {
        return possessedStatus;
    }

    /**
     * Sets the Snitch to either being possessed or not
     *
     * @param possession set the possession status of the snitch.
     */
    void setPossessedStatus(Boolean possessedStatus) {
        this.possessedStatus = possessedStatus;
    }

    
    /**
     * Determines the result of a Snitch colliding with the seeker
     *
     * @param seeker the seeker collided with
     */
    public void collisionEvent(ControllableObject other) {
        final boolean moveOther = false;
    }
    
    
    /**
     * Determines the result of a Snitch colliding with the seeker
     *
     * @param seeker the seeker collided with
     */
    public void collisionEvent(Seeker seeker) {
        if (!getPossessedStatus()) {
            setPossessedStatus(true);
        }
        if (seeker.possessionStatus) {
            setPosition(seeker.getPosition());
        }
    }
    
    /**
     * A do nothing method for when there is a collision with the Snitch.
     * 
     * @param snitch 
     */
    @Override
    public void collisionEvent(GoalHoop goalhoop) {
        //Do nothing...otherwise Snitch can get caught on GoalHoops
    }

    /**
     * Only paints the image when not possessed
     *
     * @param g the graphics object to draw on
     */
    @Override
    public void paint(Graphics g) {
        if (!possessedStatus) {
            super.paint(g);
        }
    }

    /*
     * This is the old logic that was moving the snitch around.
     */
    @Override
    public void doTick() {
        super.doTick();
        this.wrapAroundOnEdges();
    }
}