package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;

/**
 * State for ChaserAI for how the chaser should react when the enemy is in
 * possession
 * @author Ashley
 * Checked - Chris
 * Date: 13/07/2012
 */
public class EnemyInPosState implements AIState{
    protected static double MAX_MOVE_SPEED = 4;

    ChaserAI chaserAI = null;
    ObjectOverseer objOver;
    Point2D.Double quafflePos = new Point2D.Double();

    /**
     * Initializer for Enemy in pos state.
     * @param chaserAI The ChaserAI for which the state will occur in
     */
    EnemyInPosState (ChaserAI chaserAI) {
        this.chaserAI = chaserAI;
    }

    /**
     * The default behaviour when the enemy is in possession of the quaffle:
     *  - get enemy with Quaffle position
     *  - get goal position
     *  - get between enemy and goal, try to block and intercept
     */
    @Override
    public void doStateBehaviour(){
        objOver = ObjectOverseer.getInstance();
        Point2D.Double quaffle_pos = objOver.getFirst(ObjectType.QUAFFLE).getPosition();
        Chaser chaser = this.chaserAI.chaser;
        
        //if the quaffle is in the corner then dont move towards it, It must be held in there by
        //another player
        //else chase the quaffle, i.e the other player
        if ((quafflePos.x == 0 && quafflePos.y == 0) ||
            (quafflePos.x == 0 && quafflePos.y == ObjectOverseer.getFieldHeight()) ||
            (quafflePos.x == ObjectOverseer.getFieldWidth() && quafflePos.y == 0) ||
            (quafflePos.x == ObjectOverseer.getFieldWidth() && quafflePos.y == ObjectOverseer.getFieldHeight())) {
            //this.chaserAI.setState(chaserAI.goToGoalState);

            chaser.setSpeedInstantaneous(0);
        }
        else{
            chase();       
        }
        
        quafflePos = quaffle_pos;        

    }

    /**
     * set this chaser to move towards the quaffle
     */
    /**
     * Chases the Quaffle to try regain possession
     */
    private void chase() {
        Chaser chaser = this.chaserAI.chaser;
        chaser.setDesiredSpeed(0);

        objOver = ObjectOverseer.getInstance();
        Point2D.Double quaffle_pos = objOver.getFirst(ObjectType.QUAFFLE).getPosition();
        
        if (!chaser.isStunned()){
            double movementSpeed = MAX_MOVE_SPEED;
            objOver.moveTowardPoint(chaser, quaffle_pos, movementSpeed);
        } else{
            chaser.setDesiredSpeed(0);
        }
    }
}
