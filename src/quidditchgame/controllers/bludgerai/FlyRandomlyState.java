package quidditchgame.controllers.bludgerai;

import java.util.Random;
import quidditchgame.objects.Bludger;
import quidditchgame.controllers.AIState;

/**
 * Allows the Bludger to move about randomly. This should be used before the 
 * Beater hits the Bludger.
 * 
 * @author Aydin, Matthew
 */
class FlyRandomlyState implements AIState {
    Bludger bludger = null;
    
    FlyRandomlyState (Bludger bludger) {
        this.bludger = bludger;
    }

    
    /**
     * Method that describes the how the random movements are done.
     */
    @Override
    public void doStateBehaviour() {

        //Rotate in a random direction.
        Random rand = new Random();
        double randomRotation = 2 * Math.PI*rand.nextDouble();
        bludger.setDesiredRotation(randomRotation);
        
    }
}
