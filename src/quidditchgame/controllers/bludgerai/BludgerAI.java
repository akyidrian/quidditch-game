package quidditchgame.controllers.bludgerai;

import quidditchgame.controllers.AIController;
import quidditchgame.objects.Bludger;
import quidditchgame.controllers.AIState;

/**
 * Acts as the context of the BludgerAI State Pattern.
 *
 * @author Aydin Arik
 */
public class BludgerAI extends AIController {

    //All the states of this AI.
    AIState flyRandomlyState;
    AIState targetPlayerState;
    AIState chasePlayerState;
    
    private Bludger bludger = null;

    /**
     * Constructor to assign and setup state system of Bludger.
     * @param bludger reference to the Bludger object this AI is controlling
     */
    public BludgerAI(Bludger bludger) {
        this.bludger = bludger;

        flyRandomlyState = new FlyRandomlyState(bludger);
        chasePlayerState = new ChasePlayerState(bludger);

        setState(flyRandomlyState); //begin by flying randomly firstly.
    }
    
    /**
     * The state decider method for this BludgerAI.
     */
    @Override
    public void determineState() {
        
        //isHitByBeater() is false after a collision with a player or when
        //the player managed to get away (which is done through a tick timer).
        if (bludger.isHitByBeater() ) {
            setState(chasePlayerState);
        }
        else {
            setState(flyRandomlyState);
        }
    }  
}
