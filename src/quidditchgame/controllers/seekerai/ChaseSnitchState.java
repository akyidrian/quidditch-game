package quidditchgame.controllers.seekerai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Seeker;
import quidditchgame.objects.Snitch;

/**
 *
 * @author Matthew Wigley
 */
class ChaseSnitchState implements AIState {
    Seeker seeker;
    
    ChaseSnitchState (Seeker seeker) {
        this.seeker = seeker;
    }

    /**
     *
     * Method to chase the snitch constantly as long as hes not stunned
     */
    @Override
    public void doStateBehaviour() {
        //Use the overseer to find the snitch
        ObjectOverseer overseer = ObjectOverseer.getInstance();
        Snitch snitch = (Snitch)overseer.getFirst(ObjectType.SNITCH);
        Point2D.Double snitchPosition = snitch.getPosition();

        //Chase snitch if not stunned
        if (!seeker.isStunned()) {
            double movementSpeed = 4;
            overseer.moveTowardPoint(seeker, snitchPosition, movementSpeed);
        } else {
            seeker.setDesiredSpeed(0);
        }
    }
    
}
