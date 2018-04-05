package quidditchgame;

/**
 * Team holds information such as score and color for a team of players.
 * @author Chris Chester, Campbell Letts
 */
public class Team {

    /**
     * Defines colors supported by sprites.
     * Lower case required for sprite directories
     */
    public enum color {
        red, green, blue, yellow
    }
    
    /**
     * Defines the side in which the teams goal is situated. For example, the 
     * team that starts on the left has goals which they must defend on the left:
     * hence, this is the LEFT team.
     */
    public enum side {
        LEFT, RIGHT
    }

    // Team Name
    private String name = null;
    // Team Colour
    private color color = null;
    // Teams Score
    private int score = 0;
    // The side of the field which the team's goal is located.
    private side side;

    /**
     * Constructs a team with the specified name, color, and side to start on.
     * @param name
     * @param color
     * @param side
     */
    public Team(String name, color color, side side) {
        this.name = name;
        this.color = color;
        this.side = side;
    }

    /**
     * function to return the name of the team as a string
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * function to return the color of the team as an enum
     * @return color
     */
    public color getColor() {
        return color;
    }

    /**
     * function to return the side of the team as a enum
     * @return side of team
     */
    public side getSide() {
        return side;
    }

    /**
     * function to return the number of the team as a string
     * @return number as 0 or 1
     */
    public int getNumber() {
        if (getSide() == side.LEFT)
            return 0;
        else
            return 1;
    }

    /**
     * Returns the direction the team will be facing in when they begin the
     * game.
     */
    public CompassDirection getFacing() {
        if (side == side.LEFT) {
            return CompassDirection.EAST;
        } else{
            return CompassDirection.WEST;
        }
    }

    public int getScore() {
        return score;
    }

    /**
     * Increments the team's score by the amount supplied.
     * @param addPoints
     */
    void addToScore(int addPoints) {
        score += addPoints;
    }

    /**
     * Resets the team score to zero.
     * @return void
     */
    void resetScore() {
        score = 0;
    }
}
