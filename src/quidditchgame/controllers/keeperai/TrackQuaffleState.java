package quidditchgame.controllers.keeperai;

import java.awt.geom.Point2D;
import quidditchgame.CompassDirection;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.Team;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Keeper;

/**
 * This state finds the Y value of the quaffle and moves the keeper to that
 * same Y value, slightly to the inside of the goals.
 * @author Aydin
 * @author Andrew
 * @version 1.2
 */
class TrackQuaffleState implements AIState {
    private KeeperAI keeperAI = null;
    private Keeper keeper = null;
    private ObjectOverseer objOver;
    private Team team;
    private final double GOAL_KEEPER_OFFSET;
    private final double GOAL_AREA_WIDTH;
    private final double GOAL_AREA_HEIGHT_PAD;
    private final double DELTA_DIST;
    private final double PRECISION;
    private final double MAX_SPEED;
    
    /**
     * Constructor for the TrackQuaffleState, gets passed a keeperAI that it is
     * affecting, and the ObjectOverseer that can supply all the information it
     * needs.
     * @param keeper The keeper object that the AI is controlling.
     * @param objOver   The ObjectOverseer object that is used to gather 
     *                  information anout the game and it's objects.
     * @param GOAL_KEEPER_OFFSET   The distance in pixels that the keeper should
     *                              try not to go within next to their goal.
     * @param GOAL_AREA_WIDTH   The width of the area the keeper should try to
     *                          stay inside in front of their own goals.
     * @param GOAL_AREA_HEIGHT_PAD The additional height added to both the top
     *                              and the bottom of the goal height that the
     *                              keeper is trying to stay within. 
     * @param DELTA_DIST The smallest error that the keeper will account for.
     *                      So the keeper only adjusts it's position after it is
     *                      more than DELTA_DIST pixels away from where it needs
     *                      to be.
     * @param PRECISION The smallest angle that the keeper will rotate, in radians.
     * @param MAX_SPEED The fastest the keeper will move.
     */
    TrackQuaffleState ( Keeper keeper,
                        ObjectOverseer objOver,
                        final double GOAL_KEEPER_OFFSET,
                        final double GOAL_AREA_WIDTH,
                        final double GOAL_AREA_HEIGHT_PAD,
                        final double DELTA_DIST,
                        final double PRECISION,
                        final double MAX_SPEED
                      ) {
        this.keeper = keeper;
        this.objOver = objOver;
        this.team = keeper.getTeam();
        this.GOAL_KEEPER_OFFSET = GOAL_KEEPER_OFFSET;
        this.GOAL_AREA_WIDTH = GOAL_AREA_WIDTH;
        this.GOAL_AREA_HEIGHT_PAD = GOAL_AREA_HEIGHT_PAD;
        this.DELTA_DIST = DELTA_DIST;
        this.PRECISION = PRECISION;
        this.MAX_SPEED = MAX_SPEED;
    } 
    
    /**
     * Determines the movement behaviour of the keeper when tracking the 
     * quaffle.
     * Version 1.2: While within the bounding box specified by GOAL_AREA_WIDTH
     * by the goal height (that is located GOAL_KEEPER_OFFSET toward the centre 
     * of the field) mimic the y-axis position of the quaffle.
     * When within GOAL_AREA_HEIGHT_PAD of either the top or the bottom of the
     * goal area, just watch the quaffle (turn to look in it's direction), 
     * unless the quaffle position is more toward the centre of the field than
     * the keeper's current position.
     * If the keeper is outside the goal area, then move back towards it.
     * 
     */
    public void doStateBehaviour() {
        Point2D.Double keeperPos = keeper.getPosition();
        Point2D.Double quafflePos = objOver.getFirst(ObjectType.QUAFFLE).getPosition();
        Point2D.Double ownGoalPos = objOver.getAveragePosition(objOver.getAll(ObjectType.GOAL_HOOP, keeper.getTeam()));
        boolean onLeftSide = true;
        double goalKeeperOffset = GOAL_KEEPER_OFFSET;
        double goalAreaWidth = GOAL_AREA_WIDTH;
        double goalAreaHeightPad = GOAL_AREA_HEIGHT_PAD;
        boolean dontMove = false;
        
        double angleToQuaffle = objOver.getAngleToQuaffle(keeperPos);
        
        if (keeper.getTeam().getSide() == Team.side.RIGHT) {
            onLeftSide = false;
            goalKeeperOffset = -goalKeeperOffset;
        }
        
        //goalAreaHeight is really half the height of the goal area, with an added GOAL_AREA_HEIGHT_PAD
        double goalAreaHeight = objOver.getTotalGoalHeight(team)/2 + goalAreaHeightPad;
        
        //keeper out of bounds, further south than we want to be
        if (keeperPos.y > ownGoalPos.y + goalAreaHeight) {
            //move back toward the goal (so stays in front of the goals)
            if (keeperPos.x > ownGoalPos.x + goalKeeperOffset + goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.NORTH_WEST);
            }
            else if (keeperPos.x < ownGoalPos.x + goalKeeperOffset - goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.NORTH_EAST);
            }
            else {
                //move keeper down (y axis is positive down)
                keeper.setDesiredRotation(CompassDirection.NORTH);
            }
        }
        //keeper out of bounds, further north than we want to be
        else if (keeperPos.y < ownGoalPos.y - goalAreaHeight) {
            //move back toward the goal (so stays in front of the goals)
            if (keeperPos.x > ownGoalPos.x + goalKeeperOffset + goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.SOUTH_WEST);
            }
            else if (keeperPos.x < ownGoalPos.x + goalKeeperOffset - goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.SOUTH_EAST);
            }
            else {
                //move keeper down (y axis is positive down)
                keeper.setDesiredRotation(CompassDirection.SOUTH);
            }
        }
        //so if it reaches here, then the keeper is within the goalArea
        //the next two ifs (at this level) implement the quaffle tracking
        //within the centre half of the area.
        else if (keeperPos.y < quafflePos.y - DELTA_DIST
                && keeperPos.y < ownGoalPos.y + goalAreaHeight/2) {
            //move back toward the goal (so stays in front of the goals)
            if (keeperPos.x > ownGoalPos.x + goalKeeperOffset + goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.SOUTH_WEST);
            }
            else if (keeperPos.x < ownGoalPos.x + goalKeeperOffset - goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.SOUTH_EAST);
            }
            else {
                //move keeper down (y axis is positive down)
                keeper.setDesiredRotation(CompassDirection.SOUTH);
            }
        }
        else if (keeperPos.y > quafflePos.y + DELTA_DIST
                && keeperPos.y > ownGoalPos.y - goalAreaHeight/2) {
            //move back toward the goal (so stays in front of the goals)
            if (keeperPos.x > ownGoalPos.x + goalKeeperOffset + goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.NORTH_WEST);
            }
            else if (keeperPos.x < ownGoalPos.x + goalKeeperOffset - goalAreaWidth/2) {
                keeper.setDesiredRotation(CompassDirection.NORTH_EAST);
            }
            else {
                //move keeper up
                keeper.setDesiredRotation(CompassDirection.NORTH);
            }
        }
        /* this specifies that the keeper should stop and just look at the 
         * quaffle if he already is as close to it as he can get but if it is still 
         * too far away.
        */ 
        else {
            //do nothing as we're in the right place
            keeper.setDesiredRotation(angleToQuaffle);
            dontMove = true;
        }
        if (!keeper.isStunned()) {
            if (dontMove) {
                keeper.setDesiredSpeed(0);
            }
            else {
                keeper.setDesiredSpeed(MAX_SPEED);
            }
        } // do nothing if is stunned
        else {
            keeper.setDesiredSpeed(0);
        }
    }
}
