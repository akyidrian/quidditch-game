package quidditchgame.controllers.keeperai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.Team;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;
import quidditchgame.objects.Keeper;

/**
 * The state that the keeper engages when it is in possession of the quaffle.
 * Simply finds the nearest friendly chaser and passes it in its direction.
 * @author Andrew
 * @version 1.1
 */
class PassQuaffleState implements AIState {
    private ObjectOverseer objOver;
    private KeeperAI keeperAI;
    private Keeper keeper;
    private Team team;
    private double GOAL_KEEPER_OFFSET;
    private double GOAL_AREA_WIDTH;
    private double MAX_SPEED;

    /**
     * Constructor for this state. Passes in the keeperAI that is using it,
     * and the ObjectOverseer that it can use (this gets rid of any need for it
     * to access the Main class or the Game object).
     * @param keeper The keeper object that the AI is controlling.
     * @param objOver The objectOverseer object that is used to get information
     *                  about the game and other objects in it.
     * @param GOAL_KEEPER_OFFSET The distance (in pixels) from the goal the
     *                              keeper should try to not go within.
     * @param GOAL_AREA_WIDTH The width of the area the keeper should try to stay
     *                          within in front of the goals (in pixels).
     * @param MAX_SPEED The fastest the keeper will move.
     */
    public PassQuaffleState(    Keeper keeper, 
                                ObjectOverseer objOver, 
                                double GOAL_KEEPER_OFFSET,
                                double GOAL_AREA_WIDTH,
                                double MAX_SPEED
                           ) {
        this.keeper = keeper;
        team = keeper.getTeam();
        this.objOver = objOver;
        this.GOAL_KEEPER_OFFSET = GOAL_KEEPER_OFFSET;
        this.GOAL_AREA_WIDTH = GOAL_AREA_WIDTH;
        this.MAX_SPEED = MAX_SPEED;
    }

    /**
     * Uses the objectOverseer to get a list of chasers on the keeper's team,
     * finds the closest one, and the nearest enemy chaser to our friendly chaser.
     * If the enemy chaser is closer to the keeper than the enemy chaser just
     * hold on to the ball. If the friendly chaser is closer, pass the ball to
     * him.
     */
    public void doStateBehaviour() {
        Chaser nearestFriendlyChaser = (Chaser) objOver.getNearestObject(keeper.getPosition(),
                ObjectOverseer.ObjectType.CHASER, team);
        Chaser enemyCloseToChaser = (Chaser) objOver.getNearestObject(nearestFriendlyChaser.getPosition(),
                ObjectOverseer.ObjectType.CHASER, objOver.getOtherTeam(team));
        
        double deltaXbetweenChasers = enemyCloseToChaser.getPosition().x - nearestFriendlyChaser.getPosition().x;
        double deltaYbetweenChasers = nearestFriendlyChaser.getPosition().y - enemyCloseToChaser.getPosition().y;
        double distBetweenChasers = enemyCloseToChaser.getPosition().distance(nearestFriendlyChaser.getPosition());
        
        double deltaXbetweenKeeperAndEnemy = enemyCloseToChaser.getPosition().x - keeper.getPosition().x;
        double deltaYbetweenKeeperAndEnemy = keeper.getPosition().y - enemyCloseToChaser.getPosition().y;
        double distBetweenKeeperAndEnemy = enemyCloseToChaser.getPosition().distance(keeper.getPosition());
        double enemyTanAngle = Math.atan2(deltaYbetweenKeeperAndEnemy, deltaXbetweenKeeperAndEnemy);
        
        double deltaY = keeper.getPosition().y - nearestFriendlyChaser.getPosition().y; 
        double deltaX = nearestFriendlyChaser.getPosition().x - keeper.getPosition().x;
        double distBetweenKeeperAndFriendly = keeper.getPosition().distance(nearestFriendlyChaser.getPosition());
        double friendlyTanAngle = Math.atan2(deltaY, deltaX);
     
        //align the angle found from atan2 with the game field.
        double angleFromKeeperToFriendly = -friendlyTanAngle + Math.PI/2;
        if (angleFromKeeperToFriendly < 0) {
                angleFromKeeperToFriendly += 2*Math.PI;
        }
        double angleFromKeeperToEnemy = -enemyTanAngle + Math.PI/2;
        if (angleFromKeeperToEnemy < 0) {
            angleFromKeeperToEnemy += 2*Math.PI;
        }
        //the keeper drops the quaffle if it is stunned while in this state.
        if (!keeper.isStunned()) {
            if (distBetweenKeeperAndFriendly < distBetweenKeeperAndEnemy)
            {
                keeper.shoot(angleFromKeeperToFriendly);
            }
            else if (Math.abs(angleFromKeeperToFriendly - angleFromKeeperToEnemy) > Math.PI/4) {
                keeper.shoot(angleFromKeeperToFriendly);
            }
            else {
                //just hold the ball until a friendly chaser gets into position
                //move toward centre of goal area
                Point2D.Double centreGoal = new Point2D.Double();
                if (keeper.getTeam().getSide() == Team.side.LEFT) {
                    centreGoal.setLocation(objOver.getAveragePosition(objOver.getAll(ObjectType.GOAL_HOOP, team)).x
                            + GOAL_AREA_WIDTH + GOAL_KEEPER_OFFSET,
                            objOver.getAveragePosition(objOver.getAll(ObjectType.GOAL_HOOP, team)).y);
                }
                else {
                    centreGoal.setLocation(objOver.getAveragePosition(objOver.getAll(ObjectType.GOAL_HOOP, team)).x
                            - GOAL_AREA_WIDTH - GOAL_KEEPER_OFFSET,
                            objOver.getAveragePosition(objOver.getAll(ObjectType.GOAL_HOOP, team)).y);
                }
                objOver.moveTowardPoint(keeper, centreGoal, MAX_SPEED);
            }
        }
        else {
            keeper.dropQuaffle();
        }
    }
}
