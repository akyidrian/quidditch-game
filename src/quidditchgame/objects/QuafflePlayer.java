package quidditchgame.objects;

import java.awt.Graphics;
import java.util.Random;
import quidditchgame.Game;
import quidditchgame.Team;


/**
 * Describes the class of Players that can pick up, drop, and shoot the quaffle.
 *
 * @author Aydin, Sam
 */
public abstract class QuafflePlayer extends Player {

    boolean possessionStatus = false;
    Quaffle quaffle = null;
    Game game;
    Random random = new Random();
    
    /**
     * Creates a quaffle player, assigning it a team, player type, and 0 initial position.
     * @param team the team the player is on
     * @param player the name of the player sprite
     */
    QuafflePlayer(Team team, String player) {
        super(team, player);
    }
    
    /**
     * Returns whether the chaser is in possession of the quaffle
     *
     * @return
     */
    public Boolean getPossessionStatus() {
        return possessionStatus;
    }

    /**
     * Sets the chaser to either being in possession or not
     *
     * @param possession
     */
    private void setPossessionStatus(Boolean possession) {
        possessionStatus = possession;
    }

    /**
     * Used when the Player picks up the quaffle.
     * @param quaffle The quaffle to pick up.
     */
    protected void obtainQuaffle(Quaffle quaffle) {
        this.quaffle = quaffle;
        setPossessionStatus(true);
        setImage(this.getTeam(), getClassName(this.getClass()) + "InPossession");
    }
    
    /**
     * Used when the Player drops, shoots, passes or otherwise looses possession
     * of the quaffle.
     */
    protected void relinquishQuaffle() {
        setPossessionStatus(false);
        setImage(this.getTeam(), getClassName(this.getClass()));
        quaffle = null;
    }
    
    /**
     * FROM: http://www.rgagnon.com/javadetails/java-0389.html
     * Returns the class (without the package if any)
     * @param c The object that you want the class name of
     * @return
     */
    private String getClassName(Class c) {
        String className = c.getName();
        int firstChar;
        firstChar = className.lastIndexOf ('.') + 1;
        if ( firstChar > 0 ) {
            className = className.substring ( firstChar );
        }
        return className;
    }
    
    
    /**
     * Drops the Quaffle if holding one. Notifies the Quaffle that it has been
     * dropped.
     */
    public void dropQuaffle() {
        if (getPossessionStatus()) {
            //drops it infront of the player with 180 deg variance
            quaffle.dropped(((random.nextInt(5)-1)*Math.PI/4-Math.PI/2) + getRotation(), getSpeed());
            relinquishQuaffle();
        }
    }

    /**
     * Updates the position of the quaffle if it is possessed.
     */
    @Override
    public void doTick() {
        if (getPossessionStatus()) {
            quaffle.setPosition(this.getPosition());
        }
        super.doTick();
    }

    /**
     * Paints the player. It also draws the character
     * on top of the player that defines the type.
     * @param g the graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        if (getPossessionStatus()) {
            Highlight highlightBall = new Highlight(getPosition());
            highlightBall.paint(g);
        }
        super.paint(g);
    }   

    /**
     * Shoots the ball forward when either of the action keys are pressed
     * @param aPressed if the 'A' key is pressed
     * @param dPressed if the 'D' key is pressed
     */
    @Override
    public void performAction(boolean aPressed, boolean dPressed) {
        shoot(getRotation());
    }
    
    /**
     * Shoots the quaffle in a direction.
     * @param angleInRadians Angle to shoot the quaffle in, zero is vertically 
     *                          upward, increasing clockwise.
     */
    public void shoot(double angleInRadians) {
        if(getPossessionStatus()){
            quaffle.setPosition(getPosition());
            quaffle.shoot(angleInRadians);
            relinquishQuaffle();
        }
    }

    /**
     * Determines the result of a Keeper/Chaser colliding with a Quaffle.
     *
     * @param quaffle The quaffle that the Player collides with
     */
    public void collisionEvent(Quaffle quaffle) {
        if(!isStunned()) {
            if(!getPossessionStatus() && !quaffle.getPossessedStatus()) {
                obtainQuaffle(quaffle);
            }
        }
    }
}
