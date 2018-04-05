package quidditchgame.controllers.snitchai;

import java.util.Random;
import quidditchgame.ObjectOverseer;
import quidditchgame.objects.Snitch;

/**
 * A state of the state pattern.
 * 
 * This state simply flies randomly for a ball object.
 *
 * @author Aydin, Matthew
 * Checked - Chris
 * Date: 13/07/2012
 */
public class FlyRandomlyState extends SnitchState {
     
    Snitch snitch = null;
    ObjectOverseer objectOverseer;
    
    /**
     * Initializer for fly randomly state
     * 
     * @param snitch Snitch for which the state will be utilized.
     */
    public FlyRandomlyState(Snitch snitch) {
        this.snitch = snitch;
        
        this.objectOverseer = ObjectOverseer.getInstance();
    }

    /**
     * Method defining how the snitch flies randomly.
     */
    @Override
    public void doStateBehaviour() {
        
        //rotate in a random direction.
        Random rng = new Random();
        double randomRotation = 2 * Math.PI*rng.nextDouble();
        snitch.setDesiredRotation(randomRotation);

        // A final check to make sure a Seeker isn't trying to trick the Snitch.
        avoidSeekersAtFieldBounds(snitch); 
    }
}
