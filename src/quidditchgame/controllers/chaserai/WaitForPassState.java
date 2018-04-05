package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;
import quidditchgame.ObjectOverseer.ObjectType;

/**
 * State of ChaserAI for when waiting for a pass
 * @author Ashley
 * Checked- Chris
 * Date: 13/07/2012
 */
public class WaitForPassState implements AIState{
    protected static double MAX_MOVE_SPEED = 5;
    
    ChaserAI chaserAI = null;
    private ObjectOverseer objOver;
    static final double DST_FRM_GOAL = 150;
    static final double UPPER_CHASER_POS = 150;
    static final double LOWER_CHASER_POS = 450;
    static final double DST_FRM_POINT = 100;
    static final double TOLERENCE_OF_POINT = 50;
    
    
    /**
     * Initializer for wait for pass state
     * @param chaserAI The ChaserAI which the state will apply to.
     */
    WaitForPassState (ChaserAI chaserAI){
        this.chaserAI = chaserAI;
    }

    /**
     * Performs the state based on various positions.
     */
    @Override
    public void doStateBehaviour(){
        Chaser chaser = chaserAI.chaser;
        objOver = ObjectOverseer.getInstance();
        Point2D.Double goalPos = objOver.getFirst(ObjectOverseer.ObjectType.GOAL_HOOP, objOver.getOtherTeam(chaser.getTeam())).getPosition();          
        Point2D.Double newPos = new Point2D.Double();

        //determine the x position of the chaser by the side of the other teams goal
        if (goalPos.x < ObjectOverseer.getFieldWidth() / 2){
            newPos.x = goalPos.x + DST_FRM_GOAL;
        }
        else{
            newPos.x = goalPos.x - DST_FRM_GOAL;
        }
        
        //create test points to determine where the chaser should move too.
        Point2D.Double testPos = new Point2D.Double(newPos.x, LOWER_CHASER_POS);
        Point2D.Double testPos2 = new Point2D.Double(newPos.x, UPPER_CHASER_POS);

        //set the y position as the nearest test point, if you are closest
        if(objOver.getNearestObject(testPos, ObjectOverseer.ObjectType.CHASER, chaser.getTeam()) == chaser){
            newPos.y = testPos.y;
        }
        else if(objOver.getNearestObject(testPos2, ObjectOverseer.ObjectType.CHASER, chaser.getTeam()) == chaser){
            newPos.y = testPos2.y;
        }
        else{
            //find the test point with a chaser that is further away that allowed and move to that point.
            if(objOver.getNearestObject(testPos, ObjectOverseer.ObjectType.CHASER, chaser.getTeam()).getPosition().distance(testPos) > DST_FRM_POINT){
                newPos.y = testPos.y;
            }
            else{
                newPos.y = testPos2.y;
            }
        }
   
        if (newPos.distance(chaser.getPosition()) > TOLERENCE_OF_POINT){
            objOver.moveTowardPoint(chaser, newPos, MAX_MOVE_SPEED); 
        }
        else{
            //if within stop and rotate to quaffle.
            chaser.setDesiredSpeed(0);
            if(chaser.getSpeed() == 0){
                chaser.setDesiredRotation(objOver.getAngleToPoint(chaser.getPosition(), objOver.getFirst(ObjectOverseer.ObjectType.QUAFFLE).getPosition()));
            }
        }
               
    }

}
