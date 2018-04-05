package quidditchgame.objects;

import quidditchgame.Team;
import quidditchgame.controllers.bludgerai.BludgerAI;

/**
 * Bludger defines the Bludger's characteristics and its reaction to collisions.
 *
 * @author Chris Chester, Campbell Letts, Matthew Wigley, Aydin Arik
 */
public class Bludger extends Ball {

    private Team teamThatHit = null;  //last team to hit the bludger.
    private Player currentChasingPlayer; // current player that the bludger is chasing
    private long targettingTime; // targetting time of chasing a certain player
    private static final double HIT_SPEED = 10;
    private static final double STARTING_SPEED = 3;
    private static final double MAX_ROTATION_CHANGE = Math.PI / 30;
    private boolean hit = false;

    /**
     * Allocate the Bludger a sprite image location as well as starting position
     *
     * @param imageLocation
     * @param position
     */
    public Bludger() {
        super("bludger");
        setController(new BludgerAI(this));
        setSpeedInstantaneous(STARTING_SPEED);
        setMaxRotationChange(MAX_ROTATION_CHANGE);
        targettingTime = 0;
    }

    /**
     * get the targetting time of bludger to a certain player
     *
     * @return the time left chasing a certain target
     */
    public long getTargettingTime() {
        return targettingTime;
    }

    /**
     * set the targetting time of bludger to a certain player
     *
     * @param time the amount of time to target a certain object
     */
    public void setTargettingTime(long time) {
        targettingTime = time;
    }

    /**
     * get the current player that the bludger is chasing
     *
     * @return
     */
    public Player getCurrentChasingPlayer() {
        return currentChasingPlayer;
    }

    /**
     * set the current player that the bludger is chasing
     *
     * @param player Set the target that the bludger is going after
     */
    public void setCurrentChasingPlayer(Player player) {
        currentChasingPlayer = player;
    }

    /**
     * Provides a basic behaviour for the bludger. Counts down the targeting
     * time and causes the bouncing seen near the edges of the field.
     */
    @Override
    public void doTick() {
        super.doTick();
        if (targettingTime != 0) {
            targettingTime--;
            if (targettingTime == 0) {
                resetHitFlag();
            }
        }
        bounceOnEdges();
    }

    /**
     * The bludger is hit by a beater in a direction specified
     *
     * @param rotation The direction to hit the bludger in, in radians, where
     *                  zero is vertically up, increasing clockwise.
     * @param teamThatHit The team that last hit the bludger.
     */
    public void hit(double rotation, Team teamThatHit) {
        setTargettingTime(60);
        setRotationInstantaneous(rotation);
        setSpeedInstantaneous(HIT_SPEED);
        setHitFlag();
        this.teamThatHit = teamThatHit;
    }

    /**
     * Return last team that hit the Bludger.
     *
     * @return teamThatHit
     */
    public Team getLastTeamThatHit() {
        return teamThatHit;
    }

    /**
     * Set hit to true when Bludger has been hit by Beater.
     */
    private void setHitFlag() {
        hit = true;
    }

    /**
     * Reset hit to false when player is hit by a Bludger, which has been hit by
     * a Beater.
     */
    private void resetHitFlag() {
        hit = false;
    }

    /**
     * Used by BludgerAI to allow for a reset on chasing a player.
     */
    public void hitFlagTimeout() {
        resetHitFlag();
    }

    /**
     * Has the bludger been hit by the beater?
     *
     * @return hit a boolean value say if a beater hit the bludger.
     */
    public boolean isHitByBeater() {
        return hit;
    }

    /**
     * A Bludger is redirected after a collision with a Beater.
     *
     * @param beater the beater it collided with
     */
    public void collisionEvent(Beater beater) {
        final boolean moveOther = false;
        moveToCollisionPosition(beater, moveOther); //the beater does not get moved, just the bludger

    }

    /**
     * the bludger needs to bounce off goal hoops
     *
     * @param hoop The hoop to bounce off of
     */
    @Override
    public void collisionEvent(GoalHoop hoop) {
        final boolean moveOther = false;
        moveToCollisionPosition(hoop, moveOther); //the hoop does not get moved, just the bludger
        super.collisionEvent(hoop); //bounce off as usual
    }

    /**
     * Controls how the bludger behaves when it collides with a Player object.
     * @param player The player object that the bludger collided with.
     */
    public void collisionEvent(Player player) {
        super.collisionEvent(player);
        resetHitFlag(); //In the case if the bludger hits a player.
    }
}
