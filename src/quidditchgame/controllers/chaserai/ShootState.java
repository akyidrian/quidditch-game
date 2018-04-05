package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import quidditchgame.ObjectOverseer;
import quidditchgame.Team;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;
import quidditchgame.objects.CollidableObject;

/**
 * State of ChaserAI for shooting the quaffle
 * @author Aydin
 * Checked - Chris
 * Date: 13/07/2012
 */
public class ShootState implements AIState {
    protected static double SHOOT_SPEED = 5;
    ChaserAI chaserAI = null;
    
    private ObjectOverseer objOver;
    private Team team;
    
    /**
     * Initialized Shoot state
     * @param chaserAI The ChaserAI which the state will apply to.
     */
    ShootState (ChaserAI chaserAI) {
        this.chaserAI = chaserAI;
    }
 
    /**
     * Performs the state based on various positions.
     */
    @Override
    public void doStateBehaviour(){
        
        Chaser chaser = chaserAI.chaser;
        
        team = chaser.getTeam();
        objOver = ObjectOverseer.getInstance();

        //Keeper and goals being shot at.
        CollidableObject keeper = objOver.getFirst(ObjectOverseer.ObjectType.KEEPER, objOver.getOtherTeam(team));
        ArrayList<CollidableObject> goalList = objOver.getAll(ObjectOverseer.ObjectType.GOAL_HOOP, objOver.getOtherTeam(team));
        
        Point2D.Double goalPos = objOver.getNearestObject(chaser.getPosition(), ObjectOverseer.ObjectType.GOAL_HOOP, objOver.getOtherTeam(team)).getPosition();

        //Find the goal hoop not being defended by the keeper
        for(CollidableObject obj : goalList){
            if (objOver.getNearestObject(keeper.getPosition(), ObjectOverseer.ObjectType.GOAL_HOOP, objOver.getOtherTeam(team)) != obj){
                goalPos = obj.getPosition();
            }
        }

        objOver.moveTowardPoint(chaser, goalPos, SHOOT_SPEED);

        double angle = objOver.getAngleToPoint(chaser.getPosition(), goalPos);

         //Adjusts angle to within a 0 -> 2pi limit.
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        
        double angleDif = Math.abs(angle - chaser.getRotation());
                   
        if(angleDif < Math.PI/2){
            chaser.shoot(angle); 
        }
    
    }
}
