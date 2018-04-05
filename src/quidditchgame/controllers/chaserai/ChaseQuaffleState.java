package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D.Double;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;

/**
 * State for ChaserAI for when the chaser should chase the quaffle.
 * @author Aydin, Ashley
 * Checked- Chris
 */
class ChaseQuaffleState implements AIState {
    ChaserAI chaserAI = null;
    
    protected static double MAX_MOVE_SPEED = 4;
    private ObjectOverseer objOver;
    private Double quaffle_pos;
    
    /**
     * Initializes the state with a given Chaser AI
     * @param chaserAI Chaser AI for which the state is for
     */
    ChaseQuaffleState(ChaserAI chaserAI){
        this.chaserAI = chaserAI;   
    }
    
    /**
     * Finds the quaffle position and moves toward it
     */
    @Override
    public void doStateBehaviour(){
        objOver = ObjectOverseer.getInstance();
        Chaser chaser = chaserAI.chaser;
        quaffle_pos = objOver.getFirst(ObjectType.QUAFFLE).getPosition();

        if (!chaser.isStunned()){
            double movementSpeed = MAX_MOVE_SPEED;
            objOver.moveTowardPoint(chaser, quaffle_pos, movementSpeed);
        } else{
            chaser.setDesiredSpeed(0);
        }        
    }
}
