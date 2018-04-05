package quidditchgame.objects;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import quidditchgame.Team;

/**
 * Players are controllable objects that have an associated team and therefore
 * also the team color.
 * It is also possible to stun them, producing a stun animation and making them only
 * able to act passively.
 *
 * @author Chris Chester, Campbell Letts
 */
public class Player extends ControllableObject {

    /* The team the player belongs to. Player adopts team color. */
    private Team team;
    /* Player is stunned for STUN_TIME when hit by a bludger. stunCount is a 
     * counter to keep track of how long they have been stunned for. */
    private final int STUN_TIME = 50;
    private int stunCount = 0;
    
    /* The distance of the head from the center of the player. Used to position
     * the StunBirds animation when stunned. */
    private double headDistance = 18; 

    /* The StunBirds animation, creates birds that fly around the players head
     * when stunned. */
    private StunBirds bird = new StunBirds();
    String player;

    /**
     * Creates a player, assigning it a team, player type, and 0 initial position.
     * @param team the team the player is on
     * @param player the name of the player sprite
     */
    Player(Team team, String player) {
        super(team.getColor() + player, new Point2D.Double(0,0));
        this.team = team;
        this.player = player;
    }
    
    /**
     * Creates a player, assigning it a team, player type, and initial position.
     *
     * @param team the team the player is on
     * @param player the name of the player sprite
     * @param position the position of the player
     */
    Player(Team team, String player, Point2D.Double position) {
        super(team.getColor() + player, position);
        this.team = team;
        this.player = player;
    }

    /**
     * This function restarts the AI for the player
     */
    public void restartAIController() {
        
    }

    /**
     * Returns players name
     *
     * @return The players name
     */
    public String getPlayerName() {
        return player;
    }

    /**
     * Gets the player's team
     *
     * @return The players team
     */
    public Team getTeam() {
        return this.team;
    }

    /**
     * Sets the players team.
     */
    void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Stun informs the controller the player has been stunned, so that it may
     * immobilise the player. It sets the stunCount to count out the time the
     * player is stunned for.
     */
    public void stun() {
        stunCount = STUN_TIME;
        setDesiredSpeed(0);
    }

    /**
     * unStun is called by the controller when the player has recovered. It sets
     * the sprite back to normal.
     */
    public void unStun() {
        stunCount = 0;
    }
   
    
    /**
     * isStunned returns the current stunned state of the player.
     * 
     * @return stunned boolean value.
     */
    public boolean isStunned() {
        return stunCount != 0;
    }
    
    /**
     * Adds the extra functionality of unstunning the player.
     */
    @Override
    public void doTick() {
        super.doTick();
        
        if (stunCount > 0) {
            stunCount--;
        }

        bounceOnEdges();
    }

    /**
     * Paints the player.  It also draws the player highlight if the controller is human
     * and draws birds if the player is stunned.
     * @param g the graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        /* This first so higlight is under the player. */
        if (isHumanController()) {
            Highlight highlight = new Highlight(this.team, getPosition());
            highlight.paint(g);
        }
        super.paint(g);
        /* Birds painted last, so on top. */
        if (isStunned()) {
            Point2D.Double head = getHeadPosition();
            bird.setPosition(head);
            bird.paint(g);
            bird.nextFrame();
        }
    }

    /**
     * Gets the position of the head of the player taking into account the
     * rotation of the player.
     * @return head's position.
     */
    public Point2D.Double getHeadPosition() {
        return new Point2D.Double(
                    getPosition().x + headDistance * Math.sin(getRotation()),
                    getPosition().y - headDistance * Math.cos(getRotation()));
    }
    
    /**
     * This resets the speed and location of the object and resets its rotation
     * to the initial game value it is useful for when the object is teleported
     * across the map and it should not retain its previous speed or rotation
     * @param x the x location to move to
     * @param y the y location to move to
     */
    @Override
    public void resetLocation(double x,double y) {
        unStun();
        setPosition(x,y);
        setRotationInstantaneous(team.getFacing().toRadians());
        setSpeedInstantaneous(0);
    }

    /**
     * A player does not bounce off when colliding with a goal hoop. They merely
     * stop. (aim is to look more natural as the hoop will not move.)
     * @param hoop
     */
    public void collisionEvent(GoalHoop hoop) {
        moveToCollisionPosition(hoop);
    }
}