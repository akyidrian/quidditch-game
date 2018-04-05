package quidditchgame.controllers;

/**
 * This is the basic class for AI based player controllers.
 * Given a set of AIState classes, classes which extend this class will implement
 * determineState to select the current state, and the states will handle the AI logic.
 * @author Matthew
 */
public class AIController implements Controller {
    protected AIState currentState;
    
    /**
     * Do an AI tick
     */
    @Override
    public final void objectBehaviour() {
        determineState();
        currentState.doStateBehaviour();
    }
    
    /**
     * Change states
     * @param state the state to change to
     */
    public void setState(AIState state) {
        currentState = state;
    }
    
    /**
     * Get the current state
     * @return the current state
     */
    protected AIState getState() {
        return currentState;
    }
    
    /**
     * Determine which state the AI should be in and switch to it.
     */
    public void determineState() {
        
    }
    
}
