package quidditchgame.controllers.keeperai;

import java.awt.geom.Point2D;
import quidditchgame.CompassDirection;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.Team;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Keeper;

/**
 * This state is entered when the Keeper ends up on the wrong side of the goal.
 * This state controls the Keeper such that they attempt to get back on the 
 * correct side of their goal.
 *
 * @author Andrew
 * @version 1.1
 */
public class GetToGoalAreaState implements AIState{
    private ObjectOverseer objOver;
    private Keeper keeper;
    private Team team;
    private double DELTA_DIST;
    private double PRECISION;
    private double MAX_SPEED;
    private final double SLOW_SPEED = 1;
    
    /**
     * This state deals with how the keeper behaves when it goes to far toward 
     * the edge of the field (usually behind it's own goal). This state 
     * simply detects where it is and directs it back to where it should be.
     * @param keeper The Keeper object that the AI is controlling.
     * @param objOver The ObjectOverseer object that is used to obtain 
                        information about the game and other object in it.
     * @param DELTA_DIST The smallest distance the keeper will try to move (in 
     *                      pixels).
     * @param PRECISION The smallest angle the keeper will rotate (in radians).
     * @param MAX_SPEED The fastest the keeper will move.
     */
    public GetToGoalAreaState(  Keeper keeper, 
                                ObjectOverseer objOver, 
                                double DELTA_DIST, 
                                double PRECISION, 
                                double MAX_SPEED
                             ) {
        this.objOver = objOver;
        this.keeper = keeper;
        this.team = keeper.getTeam();
        this.DELTA_DIST = DELTA_DIST;
        this.PRECISION = PRECISION;
        this.MAX_SPEED = MAX_SPEED;
    }

    /**
     * If the keeper is on the wrong side of their own goal, head directly to
     * the NORTH (or SOUTH, whichever is closer) to go around the goal, and
     * back to their designated area.
     *
     */
    public void doStateBehaviour() {

        // find the team's goal hoop position
        int numGoalsPerTeam = objOver.getAll(ObjectOverseer.ObjectType.GOAL_HOOP, team).size();
        double deltaYgoal = 0;
        if (numGoalsPerTeam > 1) {
            deltaYgoal = objOver.getAll(ObjectType.GOAL_HOOP, keeper.getTeam()).get(0).getPosition().y
                    - objOver.getAll(ObjectType.GOAL_HOOP, keeper.getTeam()).get(1).getPosition().y;
        }
        // get in position between the goal hoops
        double goalAreaHeight = numGoalsPerTeam*deltaYgoal/2 + DELTA_DIST;
        Point2D.Double ownGoalPos = objOver.getAveragePosition(
                objOver.getAll(ObjectOverseer.ObjectType.GOAL_HOOP, team));
        boolean onLeftTeam = true;
        if (team.getSide() == Team.side.RIGHT) {
            onLeftTeam = false;
        }
        // note that it will actively find its side and number of goal hoops

        // go SOUTH if the the keeper is above the desired position
        if (keeper.getPosition().y >= objOver.getFieldHeight()/2
                && keeper.getPosition().y < ownGoalPos.y
                + goalAreaHeight) {
            keeper.setDesiredRotation(CompassDirection.SOUTH);
            if (Math.abs(keeper.getRotation() - CompassDirection.SOUTH.toRadians()) < PRECISION) {
                keeper.setDesiredSpeed(MAX_SPEED);
            }
        } // go NORTH if the keeper is below the desired position
        else if (keeper.getPosition().y < objOver.getFieldHeight()/2
                && keeper.getPosition().y > ownGoalPos.y
                - goalAreaHeight) {
            keeper.setDesiredRotation(CompassDirection.NORTH);
            if (Math.abs(keeper.getRotation() - CompassDirection.NORTH.toRadians()) < PRECISION) {
                keeper.setDesiredSpeed(MAX_SPEED);
            }
        }
        //by here they have at least moved a little bit north/south,
        //so should be not behind the goal anymore
        else if (onLeftTeam && (keeper.getPosition().x < ownGoalPos.x + DELTA_DIST)) {
            keeper.setDesiredRotation(CompassDirection.EAST);
            keeper.setSpeedInstantaneous(SLOW_SPEED);
        }
        else if (!onLeftTeam && (keeper.getPosition().x > ownGoalPos.x - DELTA_DIST)) {
            keeper.setDesiredRotation(CompassDirection.WEST);
            keeper.setSpeedInstantaneous(SLOW_SPEED);
        }
    }
    
    
}
