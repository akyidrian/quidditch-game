package quidditchgame.controllers.seekerai;

import quidditchgame.controllers.AIController;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Seeker;

/**
 * Acts as the context of the SeekerAI State Pattern. 
 * 
 * @author Matthew
 */
public class SeekerAI extends AIController {
    Seeker seeker;
    
    //All the states of this AI.
    private AIState chaseSnitchState;

    //Initiate AI to be chasing the snitch
    public SeekerAI(Seeker seeker) {
        this.seeker = seeker;
        
        chaseSnitchState = new ChaseSnitchState(seeker);
        
        setState(chaseSnitchState);
    }
    
    //determineState() does not need to be overriten as the seeker has only one state.
}
