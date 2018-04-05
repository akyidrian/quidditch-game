package quidditchgame.objects;

import java.awt.Graphics;
import quidditchgame.Team;
import quidditchgame.controllers.keeperai.KeeperAI;

/**
 * Keeper defines the Keeper characters and their reactions to collisions
 *
 * @author Chris Chester, Campbell Letts
 */
public class Keeper extends QuafflePlayer {
    public static final int BUMPDIST = 5;
    public static final int MAX_SPEED = 5;
    /**
     * Allocate the Keeper sprite image location as well as the characters
     * starting position.
     *
     * @param imageLocation
     * @param position
     */
    public Keeper(Team team) {
        super(team, "Keeper");
        setMaxSpeed(MAX_SPEED);
        setDesiredSpeed(0);
        setController(new KeeperAI(this));
    }

    /**
     * This function restarts the AI for the player
     */
    @Override
    public void restartAIController() {
        this.setController(new KeeperAI(this));
    }


    /**
     * Paints the player. It also draws the character
     * on top of the player that defines the type.
     * @param g the graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Letter letter = new Letter("K", getPosition());
        letter.paint(g);
    }
    
    /**
     * Determines the result of a Keeper colliding with a Chaser.
     *
     * @param chaser The chaser that this keeper collided with.
     */
    void collisionEvent(Chaser chaser) {
        super.collisionEvent(chaser);
    }
    
    /**
     * Limits the area that a keeper can go
     * @param keeperArea The KeeperBoundingBox to restrict the keeper within
     */
    @Override
     public void collisionEvent(KeeperBoundingBox keeperArea) {
        if(this.getTeam().getSide() == Team.side.LEFT){
            this.setPosition(this.getPosition().x - BUMPDIST,this.getPosition().y );
        }
        else{
            this.setPosition(this.getPosition().x + BUMPDIST,this.getPosition().y );
        }
    }

    /**
     * Collision with a Bludger stuns the Keeper, dropping the Quaffle if they
     * are holding it.
     *
     * @param bludger The bludger object that the keeper collided with
     */
    public void collisionEvent(Bludger bludger) {
        if (getPossessionStatus() == true) {
            dropQuaffle();
        }
        super.collisionEvent(bludger);
        stun();
    }

}