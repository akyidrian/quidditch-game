package quidditchgame.controllers.keeperai;

import quidditchgame.controllers.AIState;
import quidditchgame.objects.Keeper;

/**
 * The idle state of the keeperAI, literally does nothing.
 * This state should not be used.
 * @author Aydin
 * @author Andrew
 */
class IdleState implements AIState {
    Keeper keeper = null;
    
    IdleState (Keeper keeper) {
        this.keeper = keeper;
    }
    
    /**
     * The most used method in this state, which is to do nothing.
     */
    public void doStateBehaviour() {
    }
}
