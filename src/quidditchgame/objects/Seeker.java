package quidditchgame.objects;

import java.awt.Graphics;
import quidditchgame.Game;
import quidditchgame.Team;
import quidditchgame.controllers.seekerai.SeekerAI;

/**
 * Seeker defines the seeker characters and their reactions to collisions
 *
 * @author Chris Chester, Campbell Letts
 */
public class Seeker extends Player {
    private final static int MAX_SPEED = 5;
    private final static int BOOST_SPEED = 6; // new max speed due to boost
    private final static int BOOST_TIME = 20; // duration of boost in TICKS
    private final static int BOOST_COOLDOWN = 100; // duration of cool down in TICKS

    private int boostFrame = 0; //the status for 'boost' duration
    private int boostCoolDown = 0; //the cool down for the boost
    boolean possessionStatus = false;
    private Flame flame = new Flame();
    private Smoke smoke = new Smoke();
    Game game;

    /**
     * Allocate the Seeker sprite image location as well as the characters
     * starting position.
     *
     * @param team The team that the seeker is on
     * @param game The game that the seeker is a part of
     */
    public Seeker(Team team, Game game) {
        super(team, "Seeker");
        this.game = game;
        setMaxSpeed(MAX_SPEED);
        this.setController(new SeekerAI(this));
    }

    /**
     * Determines the result of a Seeker colliding with the snitch.
     *
     * @param snitch The snitch that the seeker collided with
     */
    @Override
    public void collisionEvent(Snitch snitch) {
        if (!possessionStatus) {
            this.setImage(this.getTeam(), "SeekerInPossession");
            possessionStatus = true;
            game.snitchCaught(this.getTeam());
        }
    }

    /**
     * This function restarts the AI for the player
     */
    @Override
    public void restartAIController() {
        this.setController(new SeekerAI(this));
    }

    /**
     * A Seeker is stunned by a bludger. Note no need to check if the seeker is
     * in possession of the Snitch as if they are then the game is already over.
     *
     * @param bludger The bludger the seeker collided with
     */
    public void collisionEvent(Bludger bludger) {
        super.collisionEvent((MovableObject) bludger);
        stun();
    }

    /**
     * Returns whether the Seeker is in possession of the snitch
     *
     * @return
     */
    public Boolean getPossessionStatus() {
        return possessionStatus;
    }

    /**
     * Paints the player. It also draws the character
     * on top of the player that defines the type.
     * @param g the graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        if (isBoosting()) {
            flame.setPosition(this.getPosition());
            flame.setRotationInstantaneous(this.getRotation());
            flame.paint(g);
            flame.nextFrame();
        }
        if (isCoolDown()) {
            smoke.setPosition(this.getPosition());
            smoke.setRotationInstantaneous(this.getRotation());
            smoke.paint(g);
            smoke.nextFrame();
        }
        super.paint(g);
        Letter letter = new Letter("S", getPosition());
        letter.paint(g);
    }
    
   /**
    * Causes the seeker to do a 'boost', increasing the max speed
    * and setting the current speed to the max speed.
    * Effect will last for a certain duration
    * Hits when either the A key or D key are pressed
    * @param aPressed if the 'A' key is pressed
    * @param dPressed if the 'D' key is pressed
    */
    @Override
    public void performAction(boolean aPressed, boolean dPressed){
        if (!isStunned() && (aPressed)) {
            startBoost();
        } 
    }
    
    /**
     * This starts a hit, it starts the animation and causes isHitting() to return true
     * for the duration of the hit.
     */
    public void startBoost() {

        if  (!isCoolDown()) {
            boostFrame = BOOST_TIME;
            setMaxSpeed(BOOST_SPEED);
            setSpeedInstantaneous(BOOST_SPEED);
        }
        
    }
    
    /**
     * This starts a hit, it starts the animation and causes isHitting() to return true
     * for the duration of the hit.
     */
    public void finishBoost() {
        boostFrame = 0;
        if (getSpeed() > MAX_SPEED) {
            setSpeedInstantaneous(MAX_SPEED);
        }
        setMaxSpeed(MAX_SPEED);
        boostCoolDown = BOOST_COOLDOWN;
    }
    
    /**
     * Returns whether or not the seeker is currently 'boosting'.
     * @return true if the player being boosted.
     */
    public boolean isBoosting() {
        return boostFrame != 0;
    }
    
    
    /**
     * Returns if the boost cooldown period is still in effect.
     * @return true if the cool down on boost is on.
     */
    public boolean isCoolDown() {
        return boostCoolDown != 0;
    }
    
    
    /**
     * Override the default tick so that boost status only lasts
     * a short time.
     */
    @Override
    public void doTick() {
        super.doTick();
        
        //keeping the seeker at boost speed when being boosted
        if (isBoosting()) {
            setSpeedInstantaneous(BOOST_SPEED);
            boostFrame-= 1;
            if (boostFrame <= 0) {
                finishBoost();
            }
        }
        
        // ticking the cool down timer
        if (isCoolDown()) {
            boostCoolDown -= 1;
        }
    }
    
    
}