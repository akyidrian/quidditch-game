package quidditchgame.controllers.beaterai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Beater;

/**
 *
 * This class implements one of the bludger AI states.
 * This state is called when the bludger is not within a defined distance from
 * the Beater. This state returns commands such that the Beater will chase
 * after the nearest bludger.
 * 
 * @author Aydin
 * @author Jimmy
 */
class ChaseBludgerState implements AIState{
    Beater beater;
    
    ChaseBludgerState (Beater beater) {
        this.beater = beater;
    }
    
    
    @Override
    public void doStateBehaviour() {
        Point2D.Double bludgerPosition = beater.getBludger().getPosition();

        //Distance from bludger to beater criteria included to slow game down a bit.
        if (!beater.isStunned()) {
            double movementSpeed = 4;
            ObjectOverseer.getInstance().moveTowardPoint(beater, bludgerPosition, movementSpeed);
        } else {
            beater.setDesiredSpeed(0);
        }
        
    }

}
