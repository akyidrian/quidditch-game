package quidditchgame.objects;

import java.awt.Graphics;

/**
 * Quaffle defines the quaffles characteristics and its reaction to collisions
 *
 * @author Chris Chester, Campbell Letts
 */
public class Quaffle extends Ball {

    protected int tickNum = 0;
    protected boolean possessedStatus = false;
    protected int SPEEDEVENT = 8;
    protected int OBJECTCLEARANCE = 45;
    protected int SHOTSPEED = 8;

    /**
     * Allocate the quaffle a sprite image location as well as starting position
     */
    public Quaffle() {
        super("quaffle");
    }

    /**
     * Returns whether the quaffle is possessed by a player
     *
     * @return
     */
    public Boolean getPossessedStatus() {
        return possessedStatus;
    }

    /**
     * Only paints the image when not possessed
     *
     * @param g the graphics page to paint things on to.
     */
    @Override
    public void paint(Graphics g) {
        if (!possessedStatus) {
            super.paint(g);
        }
    }

    /**
     * Decreases the speed of the quaffle after every certain number of cycles
     */
    @Override
    public void doTick() {
        super.doTick();

        if (tickNum == SPEEDEVENT) {
            if (getSpeed() != 0) {
                decreaseSpeed();
            }
            tickNum = 0;
        } else {
            tickNum += 1;
        }
        bounceOnEdges();
    }
    
    /**
     * Shoots the quaffle at the specified angle (radians). Zero is vertically
     * upward, increasing clockwise.
     * @param angleInRadians Angle to shoot the ball in
     */
    public void shoot(double angleInRadians) {
        possessedStatus = false;
        setRotationInstantaneous(angleInRadians); 
        setSpeedInstantaneous(SHOTSPEED);
        setPosition(getPosition().x + OBJECTCLEARANCE * Math.sin(getRotation()),
                getPosition().y - OBJECTCLEARANCE * Math.cos(getRotation()));
    }

    /**
     * Sets the quaffle to either being possessed or not
     *
     * @param possession The possession status to set the ball as.
     */
    void setPossessedStatus(Boolean possessedStatus) {
        this.possessedStatus = possessedStatus;
    }

    /**
     * Allows for the quaffle to be dropped when it is possessed. While
     * possessed, the Quaffle will have to keep querying the chaser for
     * position. When released the quaffle will take the rotation and speed and
     * move off from that position. Meaning the quaffle will be thrown straight
     * on once the chaser loses it.
     * @param rotation The direction that the quaffle is dropped in
     * @param speed The speed that is imparted to the quaffle when dropped
     */
    void dropped(double rotation, double speed) {

        setPossessedStatus(false);
        setRotationInstantaneous(rotation);
        setSpeedInstantaneous(speed);
        setDesiredSpeed(0);
        setPosition(getPosition().x + OBJECTCLEARANCE * Math.sin(rotation),
                getPosition().y - OBJECTCLEARANCE * Math.cos(rotation));
    }

    /**
     * Chaser/Keeper picks up quaffle
     *
     * @param qPlayer The QuafflePlayer that picked up the quaffle.
     */
    public void collisionEvent(QuafflePlayer qPlayer) {
        if(!qPlayer.isStunned()) {
            if (!getPossessedStatus()) {
                setPossessedStatus(true);
            }
            if (qPlayer.getPossessionStatus()) {

                setPosition(qPlayer.getPosition());
            }
        }
    }
    
    /**
     * Sets the location of the quaffle to the coordinates specified.
     * Also sets the speed of the quaffle to zero.
     * @param x The x-coordinate to place the quaffle.
     * @param y The y-coordinate to place the quaffle.
     */
    @Override
    public void resetLocation(double x,double y) {
        super.resetLocation(x,y);
        setDesiredSpeed(0);
        setSpeedInstantaneous(0);
    }
}
