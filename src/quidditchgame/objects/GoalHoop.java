package quidditchgame.objects;

import java.awt.geom.Point2D;
import quidditchgame.CompassDirection;
import quidditchgame.Game;
import quidditchgame.ObjectOverseer;
import quidditchgame.Team;

/**
 * GoalHoop defines the GoalHoop's characteristics and its reaction to
 * collisions
 *
 * @author Chris Chester, Campbell Letts
 */
public class GoalHoop extends CollidableObject {

    Team team;
    Game game;
    ObjectOverseer objectOverseer = ObjectOverseer.getInstance();

    /**
     * Allocate the GoalHoop a sprite image location as well as starting
     * position
     *
     * @param game the game object to be located in
     * @param position its drawing position
     * @param team the team the hoop should be assigned to
     */
    public GoalHoop(Game game, Point2D.Double position, Team team) {
        super("hoop", position);
        this.team = team;
        this.game = game;
    }

    /**
     * Gets the hoops team
     *
     * @return The hoops team
     */
    Team getTeam() {
        return team;
    }

    /**
     * Sets the hoops team
     *
     * @param team The team to set the goal to
     */
    void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Determines the result of the GoalHoop colliding with anything.
     *
     * @param collidableObject the object it collided with
     */
    @Override
    public void collisionEvent(CollidableObject collidableObject) {
        //it doesn't move.
    }

    /**
     * When the ball collides with the hoop enter the 'goal scored' phase, and
     * increment the score
     *
     * @param quaffle the quaffle collided with
     */
    public void collisionEvent(Quaffle quaffle) {
        if (!quaffle.getPossessedStatus()) {
            // Check quaffle is travelling within some rotation range shooting
            // from the front or back of the goal.
            if (((quaffle.getRotation() >= (CompassDirection.SOUTH_WEST.toRadians() - 0.1))
                    && (quaffle.getRotation() <= (CompassDirection.NORTH_WEST.toRadians() + 0.1)))
                    || ((quaffle.getRotation() >= (CompassDirection.NORTH_EAST.toRadians() - 0.1))
                    && (quaffle.getRotation() <= (CompassDirection.SOUTH_EAST.toRadians() + 0.1)))) {
                game.goalScored(team);
            }
        }
    }
}