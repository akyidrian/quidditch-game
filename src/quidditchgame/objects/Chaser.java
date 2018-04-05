package quidditchgame.objects;

import java.awt.Graphics;
import quidditchgame.Game;
import quidditchgame.Team;
import quidditchgame.controllers.chaserai.ChaserAI;

/**
 * Chaser defines the chaser characters and their reactions to collisions
 *
 * @author Chris Chester, Campbell Letts
 */
public class Chaser extends QuafflePlayer {

    public static final int MAX_SPEED = 5;
    public static final int CHASER_CHANCE = 2; // chance of losing the ball to other chaser out of 10
    public static final int KEEPER_CHANCE = 4; // chance of losing the ball to other chaser out of 10
    public static final int MAX_CHANCE = 10;

    /**
     * Constructor for a Chaser player object
     * @param team reference to the Team object to which this player belongs
     * @param game reference to the main Game object
     */
    public Chaser(Team team, Game game) {
        super(team, "Chaser");
        setMaxSpeed(MAX_SPEED);

        this.game = game;

        this.setController(new ChaserAI(this));
    }

    /**
     * This function restarts the AI for the player
     */
    @Override
    public void restartAIController() {
        this.setController(new ChaserAI(this));
    }

    /**
     * Paints the player. It also draws the character
     * on top of the player that defines the type.
     * @param g the graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Letter letter = new Letter("C", getPosition());
        letter.paint(g);
    }

    /**
     * Determines the result of a Chaser colliding with another Chaser.
     *
     * @param chaser Reference to the Chaser player object being collided with
     */
    public void collisionEvent(Chaser chaser) {
        if (getPossessionStatus() &&
           (chaser.getTeam().getColor() != this.getTeam().getColor()))
        {
            if(!chaser.isStunned() && //They do not tackle you if they are stunned!
                random.nextInt(MAX_CHANCE) <= CHASER_CHANCE) // and a 20% chance.
            {
                dropQuaffle();  // Simulating the chaser of the opposing team tackling you.
            }
        }
        super.collisionEvent(chaser);
    }

    /**
     * Determines the result of a Chaser colliding with a Keeper.
     *
     * @param keeper Reference to the Keeper player object being collided with
     */
    public void collisionEvent(Keeper keeper) {
        if (getPossessionStatus() &&
           (keeper.getTeam().getColor() != this.getTeam().getColor()))
        {
            if(!keeper.isStunned() && //They do not tackle you if they are stunned!
                random.nextInt(MAX_CHANCE) <= KEEPER_CHANCE) // and a 40% chance.
            {
                dropQuaffle();  // Simulating the chaser of the opposing team tackling you.
            }
        }
        super.collisionEvent(keeper);
    }

    /**
     * Collision with a Bludger stuns the chaser, dropping the Quaffle if they
     * are holding it.
     *
     * @param bludger Reference to the Bludger object hitting this object
     */
    public void collisionEvent(Bludger bludger) {
        if (getPossessionStatus()) {
            dropQuaffle();
        }
        super.collisionEvent(bludger);

        stun();
    }
}