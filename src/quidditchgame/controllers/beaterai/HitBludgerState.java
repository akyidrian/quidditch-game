package quidditchgame.controllers.beaterai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Beater;

/**
 *
 * This class implements one of the Bludger AI states.
 * This state is called when the Bludger is within hitting distance. This
 * state finds a target to aim the Bludger at commands the Beater such that
 * they hit the Bludger in the direction of the selected target.
 *
 * @author Aydin
 */
class HitBludgerState implements AIState{
    Beater beater;
    
    HitBludgerState (Beater beater) {
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
        //The beater should really be aiming at someone useful.
        beater.performAction(true, true);
    }
}
