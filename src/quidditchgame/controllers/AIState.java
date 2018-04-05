package quidditchgame.controllers;

/**
 * State interface for an AI.  The only publically implemented function should be doStateBehaviour.
 * @author Matthew
 */
public interface AIState {
    
    /**
     * The logic for the specific state.
     */
    public void doStateBehaviour();
}
