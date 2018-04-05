package quidditchgame;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import quidditchgame.Game.PlayableCharacter;
import quidditchgame.controllers.KeyboardController;
import quidditchgame.objects.*;

/**
 * Singleton class that returns various information about various objects to the
 * AI instances. As it is a singleton class only one instance of it may be
 * instantiated at any one time.
 *
 * @author Andrew, Ashley, Chris
 */
final public class ObjectOverseer {

    private static Game game;
    private static ObjectOverseer instance = null;

    private static final int GAME_WIDTH = 1000;
    private static final int GAME_HEIGHT = 600;

    //For each team there are:
    private static final int NUM_SEEKERS = 1;
    private static final int NUM_CHASERS = 3;
    private static final int NUM_BEATERS = 2;
    private static final int NUM_KEEPERS = 1;
    //For each game there are:
    private static final int NUM_QUAFFLES = 1;
    private static final int NUM_BLUDGERS = 2;
    private static final int NUM_GOLDEN_SNITCHES = 1;


    //For each team:
    private static final int NUM_GOALS = 3;


    /*lists of all the various game objects, such as goal hoops, players, and balls*/
    Field field = new Field(new Point2D.Double(GAME_WIDTH/2, GAME_HEIGHT/2));

    ArrayList<CollidableObject> allCollidableObjects = new ArrayList<CollidableObject>();
    ArrayList<CollidableObject> drawnObjects = new ArrayList<CollidableObject>();
    ArrayList<MovableObject> objectsWithTicks = new ArrayList<MovableObject>();
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Ball> balls = new ArrayList<Ball>();
    ArrayList<Seeker> seekers = new ArrayList<Seeker>();
    ArrayList<Chaser> chasers = new ArrayList<Chaser>();
    ArrayList<Beater> beaters = new ArrayList<Beater>();
    ArrayList<Keeper> keepers = new ArrayList<Keeper>();
    ArrayList<Snitch> snitches = new ArrayList<Snitch>();
    ArrayList<Quaffle> quaffles = new ArrayList<Quaffle>();
    ArrayList<Bludger> bludgers = new ArrayList<Bludger>();
    ArrayList<GoalHoop> leftTeamGoals = new ArrayList<GoalHoop>();
    ArrayList<GoalHoop> rightTeamGoals = new ArrayList<GoalHoop>();
    ArrayList<KeeperBoundingBox> nonKeeperArea = new ArrayList<KeeperBoundingBox>();

    Team team1, team2;
    ArrayList<Team> teams = new ArrayList<Team>();

    Player currentHumanPlayer;

    /**
     * List different types of categories of players.
     */
    public enum ObjectType {

        SEEKER,
        KEEPER,
        CHASER,
        BEATER,
        SNITCH,
        BLUDGER,
        QUAFFLE,
        BALLS,
        PLAYERS,
        GOAL_HOOP,
        KEEPER_AREA,
        DRAWN,
        HAS_TICK,
        COLLIDABLE,
        ALL,
    }

    /**
     * ObjectOverseer constructor, can only be called by the getInstance(Game
     * game) method.
     *
     * @param game The game within which all the objects are located.
     */
    private ObjectOverseer() {
    }

    /**
     * Set the internal Game variable to the input Game g.
     * @param g The Game variable to store in the ObjectOverseer
     */
    static public void setGame(Game g) {
        game = g;
    }

    /**
     * Get the current human player object.
     */
    public Player getCurrentHumanPlayer() {
        return currentHumanPlayer;
    }

    /**
     * Returns the ONLY instance of the ObjectOverseer, or creates an instance
     * if one does not already exist.
     *
     * @return instance
     */
    public static ObjectOverseer getInstance() {
        if (instance == null) {
            instance = new ObjectOverseer();
        }
        return instance;
    }

    /**
     * Creates a new instance of the ObjectOverseer, overwriting the old one.
     * This is used when a new game is started so that player positions and
     * other properties are correctly reset.
     */
    public static void makeNewInstance() {
        instance = new ObjectOverseer();
    }

    /**
     * Given an ObjectType, returns a list of CollidableObjects of that type
     * from all possible CollidableObjects.
     * @param type The type of object to return, keeper, chaser, quaffle etc.
     * @return the desired objects of its type in an arraylist
     */
    public ArrayList<CollidableObject> getAll(ObjectType type) {
        ArrayList<CollidableObject> objects = new ArrayList<CollidableObject>();
        switch (type) {
            case SEEKER:
                objects.addAll(seekers);
                break;
            case BEATER:
                objects.addAll(beaters);
                break;
            case KEEPER:
                objects.addAll(keepers);
                break;
            case CHASER:
                objects.addAll(chasers);
                break;
            case SNITCH:
                objects.addAll(snitches);
                break;
            case BLUDGER:
                objects.addAll(bludgers);
                break;
            case QUAFFLE:
                objects.addAll(quaffles);
                break;
            case BALLS:
                objects.addAll(balls);
                break;
            case PLAYERS:
                objects.addAll(players);
                break;
            case GOAL_HOOP:
                objects.addAll(leftTeamGoals);
                objects.addAll(rightTeamGoals);
                break;
            case KEEPER_AREA:
                objects.addAll(nonKeeperArea);
                 break;
            case DRAWN:
                objects.addAll(drawnObjects);
                break;
            case HAS_TICK:
                objects.addAll(objectsWithTicks);
                break;
            case COLLIDABLE:
                objects.addAll(allCollidableObjects);
                break;
            case ALL:
                objects.addAll(allCollidableObjects);
                break;
            default:
                System.out.println("You have chosen an invalid ObjectType (Object Overseer.)");
        }
        return objects;
    }

    /**
     * Given an PLAYER ObjectType and a Team, returns a list of these players that
     * are of that player type and are also on that team.
     * This function is mainly used to help the general getAll function
     * @param type The type of objects to return, limited to player type only
     * @param team The team value that each returned object must be a part of.
     * @return the desired objects of its type and team in an arraylist
     */
    private ArrayList<CollidableObject> getAllPlayer (ObjectType type, Team team) {

        ArrayList<CollidableObject> objects = new ArrayList<CollidableObject>();
        for (CollidableObject object : getAll(type)) {
            Player player = (Player) object;
            if (player.getTeam() == team) {
                objects.add(player);
            }
            else if (team == null) {
                objects.add(player);//add all players of the specified type.
            }
        }
        return objects;
    }

    /**
     * Given an ObjectType and a Team, returns a list of CollidableObjects that
     * are of that type and are also on that team.
     * @param type The type of objects to return, chaser, snitch, goal hoops etc.
     * @param team The team value that each returned object must be a part of.
     * @return the desired objects of its type and team in an arraylist
     */
    public ArrayList<CollidableObject> getAll(ObjectType type, Team team) {
        ArrayList<CollidableObject> objects = new ArrayList<CollidableObject>();


        switch (type) {
            case SEEKER:
                objects = getAllPlayer(type, team);
                break;
            case KEEPER:
                objects = getAllPlayer(type, team);
                break;
            case CHASER:
                objects = getAllPlayer(type, team);
                break;
            case BEATER:
                objects = getAllPlayer(type, team);
                break;
            case BLUDGER:
                objects = getAllPlayer(type, team);
                break;
            case PLAYERS:
                objects = getAllPlayer(type, team);
                break;

            case GOAL_HOOP:
                if (team.getSide() == Team.side.LEFT) {
                        objects.addAll(leftTeamGoals);
                }
                else if(team.getSide() == Team.side.RIGHT) {
                        objects.addAll(rightTeamGoals);
                }
                break;

            case KEEPER_AREA:

                        objects.addAll(nonKeeperArea);

                break;
            default:
                System.out.println("You have chosen an invalid ObjectType (Object Overseer)");
        }

        return objects;
    }

    /**
     * Returns the CollidableObject that is closest to the Point2D.Double origin
     * given with the ObjectType type. Relies on the implementation of getAll(type).
     * @param origin The point that the returned object is the closest of all
     *                  other objects of ObjectType type.
     * @param type The ObjectType of the returned CollidableObjects, a way of
     *                  filtering the possible results.
     * @return the nearest object of a specific type
     */
    public CollidableObject getNearestObject(Point2D.Double origin, ObjectType type) {
        ArrayList<CollidableObject> objects = this.getAll(type);
        return getNearestObject(origin, objects);
    }

    /**
     * Returns the CollidableObject that is the closest to a Point2D.Double
     * point origin, that is of ObjectType type and is on Team team.
     * Relies on the implementation of getAll(ObjectType, Team).
     * @param origin The point that the nearest CollidableObject was found for.
     * @param type The ObjectType of the returned CollidableObject.
     * @param team The Team of the returned CollidableObject.
     * @return the nearest object of a specific type and team
     */
    public CollidableObject getNearestObject(Point2D.Double origin, ObjectType type, Team team) {
        ArrayList<CollidableObject> objects = this.getAll(type, team);
        return getNearestObject(origin, objects);
    }



    /**
     * Returns the nearest CollidableObject to Point2D.Double origin that was in
     * the given list of CollidableObjects.
     * @param origin The point that the returned object was the closest to.
     * @param objects The list of CollidableObjects to select from.
     * @return the nearest object included in certain arraylist
     */
    private CollidableObject getNearestObject(Point2D.Double origin, ArrayList<CollidableObject> objects) {
        double nearestDistance = Integer.MAX_VALUE;
        CollidableObject nearest = null;

        for (CollidableObject object : objects) {
            double distance = origin.distanceSq(object.getPosition());
            if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearest = object;
                }
            }
        return nearest;
    }

     /**
     * Gets the nearest object from a specified point that is an object we
     * are interested in. In other words, specifying an object that you don't care
     * about means this object will get ignored.
     *
     * @param origin point we are measuring from.
     * @param notThis object we are not interested in.
     * @param type The ObjectType of the returned CollidableObject
     * @param team The Team that the returned CollidableObject belongs to
     * @return closest object we are interested in.
     */
    public CollidableObject getNearestObjectNotThis(Point2D.Double origin, CollidableObject notThis, ObjectType type, Team team) {
        ArrayList<CollidableObject> objects = this.getAll(type, team);
        objects.remove(notThis);

        return getNearestObject(origin, objects);
    }

    /**
     * Gets the nearest object from a specified point that is object an we
     * are interested in. In other words, specifying objects that you don't care
     * about means these objects be ignored.
     *
     * @param origin point we are measuring from.
     * @param notThese all the objects we don't care about.
     * @param type The ObjectType of the returned CollidableObject
     * @param team The Team that the returned CollidableObject belongs to
     * @return closest object we are interested in.
     */
    public CollidableObject getNearestObjectNotThese(Point2D.Double origin, ArrayList<CollidableObject> notThese, ObjectType type, Team team) {
        ArrayList<CollidableObject> objects = this.getAll(type, team);
        objects.removeAll(notThese);

        return getNearestObject(origin, objects);
    }

    /**
     * Returns the position, in Point2D.Double form, of the nearest object of
     * ObjectType type to the Point2D.Double origin.
     * @param origin The point to measure from
     * @param type The ObjectType of the returned CollidableObjects
     * @return closest object we are close with
     */
    public Point2D.Double getPositionOfNearestObject(Point2D.Double origin, ObjectType type) {
        CollidableObject closestObject = getNearestObject(origin, type);

        return closestObject.getPosition();
    }

    /**
     * Returns the first CollidableObject of ObjectType type that is found.
     * @param type The ObjectType of the object to be selected from.
     * @return the first object of a certain group
     */
    public CollidableObject getFirst(ObjectType type) {
        return getAll(type).get(0);
    }

    /**
     * Returns the first CollidableObject of ObjectType type that is also on
     * Team team.
     * @param type The ObjectType of the object to be selected from.
     * @param team The Team that the returned CollidableObject must be a part of
     * @return the first object of a certain group in a team
     */
    public CollidableObject getFirst(ObjectType type, Team team) {
        return getAll(type, team).get(0);
    }

    /**
     * Moves the specified MovableObject toward the given Point2D.Double at the
     * passed in Speed.
     * @param object The object to move
     * @param pointToMoveTowards The point to move the object towards
     * @param speed The speed with which to move the object
     */
    public void moveTowardPoint(MovableObject object, Point2D.Double pointToMoveTowards, double speed) {
        Point2D.Double currentPoint = object.getPosition();
        double angle = getAngleToPoint(currentPoint, pointToMoveTowards);
        object.setDesiredRotation(angle);
        object.setDesiredSpeed(speed);
    }

    /**
     * Returns the Team given their team number. Only works when the number of
     * unique teams is between one and two inclusive.
     * @param teamNumber The number of the team you want (0, 1, 2, 3 etc)
     * @return the team
     */
    public Team getTeam(int teamNumber) {
        if (teamNumber == 0) {
            return team1;
        } else {
            return team2;
        }
    }

    /**
     * Returns the Team object of the other team (compared to the passed in
     * Team). Only works when there are exactly two unique teams.
     * @param team
     * @return the other team
     */
    public Team getOtherTeam(Team team) {
        if (team == team1) {
            return team2;
        } else {
            return team1;
        }
    }

    /**
     * Given any list of CollidableObjects, finds the mean position of them all
     * and returns it as a Point2D.Double.
     * @param objectsList
     * @return average position of all the objects of a specific type
     */
    public Point2D.Double getAveragePosition(ArrayList<CollidableObject> objectsList) {
        Point2D.Double averagePos = new Point2D.Double();
        double sumX = 0;
        double sumY = 0;
        for (CollidableObject object : objectsList) {
            sumX += object.getPosition().x;
            sumY += object.getPosition().y;
        }
        double averageX = sumX / objectsList.size();
        double averageY = sumY / objectsList.size();
        averagePos.setLocation(averageX, averageY);
        return averagePos;
    }

    /**
     * Finds the total goal height (along the Y-axis) for a specified team.
     * Calculates the number of goals multiplied by the offset between each
     * goal's centrepoint. Assumes homogenous spacing between goals.
     * @param team Which team's goal height that we're finding.
     * @return the height of the goal
     */
    public double getTotalGoalHeight(Team team) {
        int numGoalsPerTeam = getAll(ObjectOverseer.ObjectType.GOAL_HOOP, team).size();
        double deltaYgoal = 0;
        if (numGoalsPerTeam > 1) {
            deltaYgoal = Math.abs(getAll(ObjectType.GOAL_HOOP, team).get(0).getPosition().y
                    - getAll(ObjectType.GOAL_HOOP, team).get(1).getPosition().y);
        }
        deltaYgoal = numGoalsPerTeam * deltaYgoal;
        return deltaYgoal;
    }

    /**
     * Returns the angle to the quaffle from any given Point2D.Double point,
     * in radians.
     * @param objectPos The Point2D.Double that is the initial position to find the angle for.
     * @return the direction to the quaffle
     */
    public double getAngleToQuaffle(Point2D.Double objectPos) {
        double deltaX = getFirst(ObjectType.QUAFFLE).getPosition().x - objectPos.x;
        //due to orientation of the y-axis this needs to be backwards
        double deltaY = objectPos.y - getFirst(ObjectType.QUAFFLE).getPosition().y;
        double tanResult = Math.atan2(deltaY, deltaX); //in radians
        double angleToQuaffle = -tanResult + Math.PI/2; //align it to the field
        if (angleToQuaffle < 0) {
                angleToQuaffle += 2*Math.PI;    //take only positive values
        }
        return angleToQuaffle;
    }

    /**
     * Returns if the internal game is variable initialised. ObjectOverseer
     * cannot be used until it is.
     * @return boolean representing whether game is initialised
     */
    public boolean gameInitialised() {
        return game != null;
    }

    /**
     * Returns the field object.
     * @return field
     */
    public Field getField() {
        return field;
    }

    /**
     * Returns the field height in pixels.
     * @return height of the game
     */
    public static int getFieldHeight() {
        return GAME_HEIGHT;
    }

    /**
     * Returns the field width in pixels.
     * @return width of the game
     */
    public static int getFieldWidth() {
        return GAME_WIDTH;
    }

    /**
     * Returns the centre of the game field as a Point2D.Double.
     * @return centre of the game
     */
    public static Point2D.Double getGameCenterPoint() {
        return new Point2D.Double(GAME_WIDTH/2,GAME_HEIGHT/2);
    }

    /**
     * Calculates the angle (in radians) from the given "from" point, to the
     * passed in "to" point. A zero angle is vertically upward from the "from"
     * point, increasing in the clockwise direction.
     * @param from Point2D.Double to calculate the angle from
     * @param to Point2D.Double to calculate the angle to
     * @return direction to a certain point
     */
    public Double getAngleToPoint(Point2D.Double from, Point2D.Double to){
        return Math.atan2(from.y - to.y, from.x - to.x) - Math.PI/2;
    }

    /**
     * Function to set up the teams in the object overseer variable
     * @param team1
     * @param team2
     */
    void setupTeams(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        teams.add(team1);
        teams.add(team2);
    }

    /**
     * create all the objects which will be placed on the field
     */
    void createObjects() {

        //create the goal hoops
        for (int i = -1; i < (NUM_GOALS - 1); i++) {
            leftTeamGoals.add(new GoalHoop(game, new Point2D.Double(50, getFieldHeight()/2 + 60 * i), team1));
            rightTeamGoals.add(new GoalHoop(game, new Point2D.Double(getFieldWidth()-50, getFieldHeight()/2 + 60 * i), team2));
        }

        nonKeeperArea.add(new KeeperBoundingBox(new Point2D.Double(getFieldWidth()/2 ,getFieldHeight()/2)));

        allCollidableObjects.addAll(leftTeamGoals);
        allCollidableObjects.addAll(rightTeamGoals);
        allCollidableObjects.addAll(nonKeeperArea);


        createPlayers();
        createBalls();



        drawnObjects.addAll(allCollidableObjects);
    }

    /**
     * creates all the players and adds them to their corresponding arraylists
     */
    private void createPlayers() {
        // Creates full set of players for each team
        for (Team team : teams) {
            for (int i = 0; i < NUM_SEEKERS; i++) {
                seekers.add(new Seeker(team, game));
            }

            for (int i = 0; i < NUM_CHASERS; i++) {
                chasers.add(new Chaser(team, game));
            }

            for (int i = 0; i < NUM_BEATERS; i++) {
                beaters.add(new Beater(team));
            }

            for (int i = 0; i < NUM_KEEPERS; i++) {
                 keepers.add(new Keeper(team));
            }
        }

        // Adding all players to a players ArrayList.
        players.addAll(seekers);
        players.addAll(chasers);
        players.addAll(beaters);
        players.addAll(keepers);

        allCollidableObjects.addAll(players);
        objectsWithTicks.addAll(players);
    }
        private static final int NUM_BALLS = NUM_QUAFFLES + NUM_BLUDGERS
            + NUM_GOLDEN_SNITCHES;
    /**
     * creates all the balls and adds them to their corresponding arraylists
     */
    void createBalls() {
        for (int i = 0; i < NUM_BLUDGERS; i++) {
            bludgers.add(new Bludger());
        }
        for (int i = 0; i < NUM_QUAFFLES; i++) {
            quaffles.add(new Quaffle());
        }
        for (int i = 0; i < NUM_GOLDEN_SNITCHES; i++) {
            snitches.add(new Snitch());
        }

        // Adding all balls to a balls ArrayList.
        balls.addAll(bludgers);
        balls.addAll(quaffles);
        balls.addAll(snitches);

        allCollidableObjects.addAll(balls);
        objectsWithTicks.addAll(balls);
    }


    /**
     * TO BE CALLED ONLY ONCE AT THE BEGINNING OF A GAME.
     *
     * Initializes all the human player.
     * @param playerType the type of character the human will play
     * @return the keyboard controller that was set
     */
    KeyboardController initHumanPlayer(PlayableCharacter humanPlayerType) {

        //Get the very first player of the selected type.
        switch (humanPlayerType) {
            case SEEKER:
                currentHumanPlayer = seekers.get(0);
                break;
            case KEEPER:
                currentHumanPlayer = keepers.get(0);
                break;
            case CHASER:
                currentHumanPlayer = chasers.get(0);
                break;
            case BEATER:
                currentHumanPlayer = beaters.get(0);
                break;
            default:
                currentHumanPlayer = chasers.get(0);
                break;
        }


        KeyboardController keyboardControl = new KeyboardController(currentHumanPlayer);
        currentHumanPlayer.setController(keyboardControl);

        return keyboardControl;
    }


    /**
     * Reinitializes human player to allow player switching.
     * @param playerType the type of character the human will play
     * @return the keyboard controller that was set
     */
    KeyboardController reinitHumanPlayer(Player player) {

        currentHumanPlayer = player;
        KeyboardController keyboardControl = new KeyboardController(currentHumanPlayer);
        currentHumanPlayer.setController(keyboardControl);

        return keyboardControl;
    }

    /**
     * resets each of the players to their initial starting locations
     */
    void resetPlayerLocations() {
        resetPlayerLocations(true);
    }
    /**
     * resets each of the players to their initial starting locations with the option of not resetting seeker locations
     * @param boolean to reset the seekers
     */
    void resetPlayerLocations(boolean resetSeekers) {
        int gameCenterX = field.getWidth()/2;
        int gameCenterY = field.getHeight()/2;
        if (resetSeekers) {
            //team 1
            seekers.get(0).resetLocation(150,   gameCenterY);
            //team 2
            seekers.get(1).resetLocation(2*gameCenterX - 150, gameCenterY);
        }

        //team 1
        chasers.get(0).resetLocation(3*gameCenterX/4, 0.8*gameCenterY);
        chasers.get(1).resetLocation(3*gameCenterX/4, 1.0*gameCenterY);
        chasers.get(2).resetLocation(3*gameCenterX/4, 1.2*gameCenterY);

        //team 2
        chasers.get(3).resetLocation(5*gameCenterX/4, 0.8*gameCenterY);
        chasers.get(4).resetLocation(5*gameCenterX/4, 1.0*gameCenterY);
        chasers.get(5).resetLocation(5*gameCenterX/4, 1.2*gameCenterY);

        //team 1
        beaters.get(0).resetLocation(gameCenterX/2, 0.8*gameCenterY);
        beaters.get(1).resetLocation(gameCenterX/2, 1.2*gameCenterY);

        //team 2
        beaters.get(2).resetLocation(3*gameCenterX/2, 0.8*gameCenterY);
        beaters.get(3).resetLocation(3*gameCenterX/2, 1.2*gameCenterY);

        //team 1
        keepers.get(0).resetLocation(100, gameCenterY);
        //team 2
        keepers.get(1).resetLocation(field.getWidth() - 100, gameCenterY);
    }

    /**
     * reset the ball locations to their starting positions
     * @param resetSnitch whether the snitch should be moved
     * @param resetBludgers whether the bludgers should be moved
     */
    void resetBallLocations(boolean resetSnitch, boolean resetBludgers) {
        int gameCenterX = field.getWidth()/2;
        int gameCenterY = field.getHeight()/2;

        //Bludgers fly aways from the pitch centre.
        if (resetBludgers) {
        bludgers.get(0).resetLocation(gameCenterX, gameCenterY/2);
        bludgers.get(0).setRotationInstantaneous(CompassDirection.NORTH);

        bludgers.get(1).resetLocation(gameCenterX, 3*gameCenterY/2);
        bludgers.get(1).setRotationInstantaneous(CompassDirection.SOUTH);
        }

        quaffles.get(0).resetLocation(gameCenterX,gameCenterY);

        if (resetSnitch) {
            snitches.get(0).resetLocation(gameCenterX,gameCenterY-50);
        }
    }

}
