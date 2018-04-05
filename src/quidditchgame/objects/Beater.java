package quidditchgame.objects;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import quidditchgame.Team;
import quidditchgame.controllers.beaterai.BeaterAI;

/**
 * Beater defines the Beater characters and their reactions to collisions
 *
 * @author Chris Chester, Campbell Letts, Matthew Wigley
 */
public class Beater extends Player {
    private final static int MAX_SPEED = 5;
    private final static int HIT_FRAMES_COUNT = 3; 
    private final static int ANIMINATION_SPEED_FACTOR = 4; //quarter speed animation
    private final static int MAX_BLUDGER_DISTNACE_TO_HEAD = 25;
            
    private int hitFrame = 0; //the current animation from for the 'hit' animation
    private Bludger bludger;


    /**
     * Allocate the Beater sprite image location.
     * @param team the team the beater plays for
     */
    public Beater(Team team) {
        super(team, "Beater");
        setMaxSpeed(MAX_SPEED);
        this.mass = 100;
        this.setController(new BeaterAI(this));
    }

    /**
     * This function restarts the AI for the player
     */
    @Override
    public void restartAIController() {
        this.setController(new BeaterAI(this));
    }

    /**
     * Allocate the bludger this beater is chasing
     * @param bludger The bludger to allocate to this beater
     */
    public void setBludger(Bludger bludger) {
        this.bludger = bludger;
    }


    /**
     * Allocate the bludger this beater is chasing
     * @return bludger being chased
     */
    public Bludger getBludger() {
        return bludger;
    }
    
    /**
     * override the default tick so that bludger possession status only lasts
     * a short time
     */
    @Override
    public void doTick() {
        super.doTick();
        
        //display the bat swinging animation if a hit is in process
        if (isHitting()) {
            hitFrame--;
            switch(hitFrame/ANIMINATION_SPEED_FACTOR) {
                case 0:
                    setImage(this.getTeam(), "Beater");
                    break;
                case 1:
                    setImage(this.getTeam(), "BeaterHit2");
                    break;
                case 2:
                    setImage(this.getTeam(), "BeaterHit1");
                    break;
            }
        }
    }

    /**
     * Paints the player. It also draws the character
     * on top of the player that defines the type.
     * @param g the graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Letter letter = new Letter("B", getPosition());
        letter.paint(g);
    }

    /**
     * Determines the result of a Beater colliding with a Bludger.
     * When a collision of this type occurs the bludger is momentarially 'in possession'
     * of the beater and if they hit the action key it will cause the bludger
     * to be hit by the beater
     * 
     * Note that the player will be stunned if they do not hit the ball with the fore portion of their body
     * or if they are not hitting the ball when they collide with it.
     *
     * @param bludger
     */
    public void collisionEvent(Bludger bludger) {         
        Point2D.Double head = this.getHeadPosition();
        if (head.distance(bludger.getPosition()) < MAX_BLUDGER_DISTNACE_TO_HEAD && !isStunned()) {
            if (isHitting()) {
                bludger.hit(getRotation(), this.getTeam());
            }
        } else {
            stun();
        }
    }
    
    
   /**
    * Causes the beater to do a 'hit', if they collide head on with a bludger whilst hitting
    * the bludger will be hit away
    * Hits when either the A key or D key are pressed
    * @param aPressed if the 'A' Key is pressed
    * @param dPressed if the 'D' Key is pressed
    */
    @Override
    public void performAction(boolean aPressed, boolean dPressed){
        if (!isStunned() && (aPressed || dPressed)) {
            startHit();
        }
    }
    
    /**
     * This starts a hit, it starts the animation and causes isHitting() to return true
     * for the duration of the hit.
     */
    void startHit() {
        hitFrame = HIT_FRAMES_COUNT*ANIMINATION_SPEED_FACTOR;
    }
    
    /**
     * @return true if the bat is being swung.
     */
    public boolean isHitting() {
        return hitFrame != 0;
    }
}