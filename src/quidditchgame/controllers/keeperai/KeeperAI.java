package quidditchgame.controllers.keeperai;

import quidditchgame.ObjectOverseer;
import quidditchgame.Team;
import quidditchgame.controllers.AIController;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Keeper;

/**
 * Acts as the context of the KeeperAI State Pattern. 
 * 
 * @author Andrew
 * @author Aydin
 */
public class KeeperAI extends AIController {
    private ObjectOverseer objOver;
    
    private final double DELTA_DIST = 2; /**the smallest change in position the keeper should move. Should be less than goalKeeperOffset in TrackQuaffleState.java*/
    private final double PRECISION = 0.1; /**smallest angle in radians the keeper will attempt to achieve*/
    private final double MAX_SPEED = 4; /**fastest the keeper will ever move (under it's own power)*/
    private final double GOAL_KEEPER_OFFSET = 0; /**the distance between the goal and the start of the goal area*/
    private final double GOAL_AREA_WIDTH = 50; /**the width of the area in front of the goal the keeper should stay in*/
    private final double GOAL_AREA_HEIGHT_PAD = 50; /**the height added to the top and bottom of the goal area*/
       
    private Keeper keeper = null;
    
    //All the states of this AI.
    private AIState idleState;
    private AIState trackQuaffleState;
    private AIState passQuaffleState;
    private AIState getToGoalState;
    
    
    /**
     * Constructor to assign and setup state machine.
     */       
    public KeeperAI(Keeper keeper) {
        this.keeper = keeper;
        
        objOver = ObjectOverseer.getInstance();
        
        idleState = new IdleState(keeper);
        trackQuaffleState = new TrackQuaffleState(keeper, 
                                objOver,
                                GOAL_KEEPER_OFFSET,
                                GOAL_AREA_WIDTH,
                                GOAL_AREA_HEIGHT_PAD,
                                DELTA_DIST,
                                PRECISION,
                                MAX_SPEED);
        passQuaffleState = new PassQuaffleState(keeper, 
                                objOver, 
                                GOAL_KEEPER_OFFSET,
                                GOAL_AREA_WIDTH,
                                MAX_SPEED);
        getToGoalState = new GetToGoalAreaState(keeper,
                                objOver,
                                DELTA_DIST, 
                                PRECISION, 
                                MAX_SPEED);
        
        setState(idleState);
    }
    
    /**
     * This method determines the state that the KeeperAI should be in, all state
     * changes occur here. 
     */
    @Override
    public void determineState() {
        boolean onLeftTeam = true;
        
        if (keeper.getTeam().getSide() == Team.side.RIGHT) {
            onLeftTeam = false;
        }
        
        //If have the quaffle, pass it to the nearest chaser.
        if (keeper.getPossessionStatus()) {
            setState(passQuaffleState);
        }
        //if the keeper is within DELTA_DIST of their own goal or less.
        else if ((onLeftTeam && keeper.getPosition().x < 
                objOver.getAveragePosition(
                objOver.getAll(ObjectOverseer.ObjectType.GOAL_HOOP, keeper.getTeam())).x + DELTA_DIST)
                || (!onLeftTeam &&  keeper.getPosition().x > 
                objOver.getAveragePosition(
                objOver.getAll(ObjectOverseer.ObjectType.GOAL_HOOP, keeper.getTeam())).x - DELTA_DIST)) {
            //keeper is behind it's own goal
            setState(getToGoalState);
        }
        else {
        //Mimic the y value of the quaffle
        setState(trackQuaffleState);
        }
    }
}
