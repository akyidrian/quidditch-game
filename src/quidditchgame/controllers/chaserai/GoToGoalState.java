package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D.Double;
import quidditchgame.ObjectOverseer;
import quidditchgame.Team;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;

/**
 * State for ChaserAI to instruct the chaser to go towards the goal
 * @author Aydin
 * Checked - Chris
 * Date: 13/07/2012
 */
class GoToGoalState implements AIState {
     protected static double MAX_MOVE_SPEED = 4;
    
    ChaserAI chaserAI = null;
    
    private ObjectOverseer objOver;
    private Team team;
    private Team enemyTeam;
    private Double goal_pos;
   
    /**
     * Initializer for Go to goal state
     * @param chaserAI The ChaserAI which this state will apply to
     */
    GoToGoalState (ChaserAI chaserAI) {
        this.chaserAI = chaserAI;
    }  
    
    /**
     * Performs the state behavior based on several positions
     */
    @Override
    public void doStateBehaviour(){
        
        objOver = ObjectOverseer.getInstance();

        Chaser chaser = chaserAI.chaser;
        
        team = chaser.getTeam();
        enemyTeam = objOver.getOtherTeam(team);
        
        //get goal position
        goal_pos = objOver.getFirst(ObjectOverseer.ObjectType.GOAL_HOOP, enemyTeam).getPosition();
        
        //go to goal
        if (!chaser.isStunned()) {
            double movementSpeed = MAX_MOVE_SPEED;
            objOver.moveTowardPoint(chaser, goal_pos, movementSpeed);
        } else {
            chaser.setDesiredSpeed(0);
        }        
    }
    
}
