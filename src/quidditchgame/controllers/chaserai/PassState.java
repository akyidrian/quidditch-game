package quidditchgame.controllers.chaserai;

import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;

/**
 * State for ChaserAI for when the chaser is looking to pass.
 * @author Ashley
 * Checked - Chris
 * Date: 13/07/2012
 */
public class PassState implements AIState{
     protected static double PASS_SPEED = 5;
     protected static double MIN_PASS_ANGLE = 0.1;

    ChaserAI chaserAI = null;
    /**
     * Initializer for Pass state  
     * @param chaserAI The ChaserAI for which this state will be applied.
     */
    PassState (ChaserAI chaserAI) {
        this.chaserAI = chaserAI;
    }

    /**
     * Does behavior of passing based on various positions
     */
    @Override
    public void doStateBehaviour(){
        Chaser chaserToPassTo = chaserAI.friendly_in_pass_pos;
        Chaser chaser = this.chaserAI.chaser;
        ObjectOverseer objOver = ObjectOverseer.getInstance();
        
        objOver.moveTowardPoint(chaser, chaserToPassTo.getPosition(), PASS_SPEED);
        
        double angle = objOver.getAngleToPoint(chaser.getPosition(), chaserToPassTo.getPosition());
        
        //Adjusts angle to within a 0 -> 2pi limit.
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        
        double angleDif = Math.abs(angle - chaser.getRotation());
                
        if (angleDif < MIN_PASS_ANGLE){
            chaser.shoot(angle);
        }
            
    }
}

