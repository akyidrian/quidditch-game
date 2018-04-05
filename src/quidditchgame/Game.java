package quidditchgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.controllers.KeyboardController;
import quidditchgame.menus.GameStatusPanel;
import quidditchgame.objects.*;

 /**
 * 
 * This is the main in-game class for the Quidditch game. IT:
 * - Constructs all the objects on the field of play and sets their initial position
 * - Handles game loop, implemented as a java Timer
 * - handles the updating of score
 * - sets up the Human controlled character
 * - provides functionality for pausing and resuming the game
 * 
 * @authors Aydin Arik, Sam Leichter, Matthew Wigley 
 * 
 */
final public class Game extends JPanel {
    
    /**
     * List of all the playable characters
     */
    public enum PlayableCharacter {
        SEEKER,
        KEEPER,
        CHASER,
        BEATER
    }

    public enum GameCondition {
        PLAYING,
        TIME_UP,
        SNITCH_CAUGHT_0,
        SNITCH_CAUGHT_1
    }

    private long timeGameStarted;//milliseconds.
    private long gameTime; //seconds.
    private long totalGameTime; //seconds.
    private int snitchScoreValue; // score value for snitch used from setup
    private GameCondition gameCondition; // game condition to determine wins
    private PlayableCharacter currentPlayerType; //current player type being played
    KeyboardController keyboardControl; //current keyboard controller
    
    boolean gamePaused = true;
    long timePaused = 0;
    long timeGamePaused = 0;
    long totalTimePaused = 0;
    
    private static final int GOAL_SCORE_VALUE = 10;

    
    private static final int GAME_TICK_DELAY = 35; //the length of a game tick. In milliseconds.
    
    GameStatusPanel gameStatusPanel;
    ObjectOverseer overseer;
    
    CollisionManager CollisionManager = new CollisionManager();
    
    //Signs which are displayed after certain game actions
    GoalScoredSign goalScoredSign = new GoalScoredSign(ObjectOverseer.getGameCenterPoint());
    boolean displayGoalScored = false;   
    SnitchCaughtSign snitchCaughtSign = new SnitchCaughtSign(ObjectOverseer.getGameCenterPoint());
    boolean displaySnitchCaught = false;   
    
    private int spacePress = 0; //Used to choose player in team after space bar press.
    
    /**
     * This is the timer for the game loop - when it is unpaused the game is running.
     * It simply calls doTick() repeatedly
     */
    javax.swing.Timer tickTimer = new javax.swing.Timer(GAME_TICK_DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
                doTick();
        }
    });
    
    /**
     * Constructor for the Game class.  The game is initially paused and only started 
     * @param team1 the first team
     * @param team2 the second team
     * @param gameStatusPanel the status panel displayed at the bottom of window
     * @param playerControlledType the type of player that the player will be able to control
     */
    public Game(Team team1, Team team2, GameStatusPanel gameStatusPanel, 
            PlayableCharacter playerControlledType, long totalGameTime, int snitchScoreValue) {
        ObjectOverseer.makeNewInstance();
        ObjectOverseer.setGame(this);
        
        overseer = ObjectOverseer.getInstance();
        overseer.setupTeams(team1, team2);
        
        this.gameStatusPanel = gameStatusPanel;
        gameStatusPanel.updateScore(team1,team2); //update the score so that the game status panel knows the team names

        
        overseer.createObjects();
        keyboardControl = overseer.initHumanPlayer(playerControlledType);
        this.addKeyListener(keyboardControl);
        this.currentPlayerType = playerControlledType;
        overseer.resetPlayerLocations();
        overseer.resetBallLocations(true, true);
        
        this.setBackground(Color.gray);
        this.invalidate();

        this.setFocusable(true); //needs focus to get keyboard events.
        this.setMinimumSize(new Dimension(ObjectOverseer.getFieldWidth(), ObjectOverseer.getFieldHeight()));
        
        this.addKeyListener((KeyListener) new MenuKeyListener());
        
        this.totalGameTime = totalGameTime; //setting the game time from setup menu
        this.snitchScoreValue = snitchScoreValue;
        
        timeGameStarted = getCurrentTimeMillis();

    }
    
    /**
     * Return is in milliseconds.
     */
    private long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
    

    /**
     * Draws the field, players, balls, and signs to the screen.
     * @param g graphics object to draw with
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        overseer.getField().paint(g);
        
        for (DrawnObject drawnObject : overseer.getAll(ObjectType.DRAWN)) {
            drawnObject.paint(g);
        }
        
        if (displayGoalScored) {
            goalScoredSign.paint(g);
        }
        
        if (displaySnitchCaught) {
            snitchCaughtSign.paint(g);
        }
    }
    

    /**
     * This function does a single game tick.  The ticks for each of the objects
     * in play, then it fires collision events, and finally redraws the screen.
     */
    void doTick() {

        if(!gamePaused){

            for (CollidableObject objectWithTick : overseer.getAll(ObjectType.HAS_TICK)) {
                ((MovableObject)objectWithTick).doTick();
            }

            CollisionManager.fireCollisionEvents(overseer.getAll(ObjectType.COLLIDABLE));

            updateGameTime(gamePaused);

            this.repaint();
        }
    }
    

    /**
     * Updates the gameTime variable which reflects how long the game have been
     * going for.
     * @param gamePaused local variable that is set based on whether the game is
     * paused
     */
    private void updateGameTime(boolean gamePaused) {

        //Don't update if game is paused
        if(!gamePaused){

            gameTime = totalGameTime - ((getCurrentTimeMillis() - timeGameStarted)/1000) + totalTimePaused; //in seconds.
            if (gameTime <= 0) {
                gameCondition = GameCondition.TIME_UP;
                finishGame(gameCondition);
            }

            gameStatusPanel.updateGameTime(gameTime); //display new game time.
        }

        

    }
    
    
    /**
     * This function is called when a goal is scored.
     * It increments the score of the team which scored the goal,
     * resets the player and ball locations, and pauses the game
     * and displays a "GOAL" sign for a time
     * @param team the teams goal which was scored into - so points will be awarded to the other team.
     */
    public void goalScored(Team team) {
        displayGoalScored = true;
        pauseGame();
        ObjectOverseer.getInstance().getOtherTeam(team).addToScore(GOAL_SCORE_VALUE);
        
        gameStatusPanel.updateScore(overseer.getTeam(0), overseer.getTeam(1));

        overseer.resetPlayerLocations(false);
        overseer.resetBallLocations(false, true); //resetting bludger to prevent game reset player stunning (as this is cruel and unfair).
        
        javax.swing.Timer goalScoredTimer = new javax.swing.Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        doneGoalScoredDisplay();
                    }
                });
        goalScoredTimer.setRepeats(false);
        goalScoredTimer.start();
    }
    
    /**
     * This function is called when the "GOAL" sign is finished displaying.
     * It resumes gameplay and stops the sign displaying
     */
    private void doneGoalScoredDisplay() {
        resumeGame();
        displayGoalScored = false;
    }
    
    /**
     * This function is called when the snitch has been caught.
     * It increments the catching teams score by 30 points and ends the game after displaying a snitch caught sign
     * 
     * @param team the team that caught the snitch.
     */
    public void snitchCaught(Team team) {
        displaySnitchCaught = true;
        pauseGame();
        team.addToScore(snitchScoreValue);
        gameStatusPanel.updateScore(overseer.getTeam(0), overseer.getTeam(1));
        if (team.getNumber() == 0)
            gameCondition = GameCondition.SNITCH_CAUGHT_0;
        else
            gameCondition = GameCondition.SNITCH_CAUGHT_1;
 
        javax.swing.Timer snitchCaughtTimer = new javax.swing.Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        snitchCaughtDoneSignDisplay();
                    }
                });
        snitchCaughtTimer.setRepeats(false);
        snitchCaughtTimer.start();
    }
    
    /**
     * This function is called once the snitch caught sign is finished displaying.
     * It moves the player onto the 'finished game' screen.
     */
    private void snitchCaughtDoneSignDisplay() {
        displaySnitchCaught = false;
        finishGame(gameCondition);
    }

    /**
     * Call this method to start the game
     */
    public void startGame() {
        resumeGame();
        timeGamePaused = 0;
        totalTimePaused = 0;
    }

    /**
     * Call this method to pause the game
     */
    public void pauseGame() {
        gamePaused = true;
        timeGamePaused = getCurrentTimeMillis();
        tickTimer.stop();
    }

    /**
     * Call this method to resume the game after having paused it
     */
    public void resumeGame() {
        gamePaused = false;
        timePaused = (getCurrentTimeMillis() - timeGamePaused) / 1000;
        totalTimePaused = totalTimePaused + timePaused;
        tickTimer.start();
    }
    
    /**
     * Call this method to finish the game and enter game finish menu
     * @param condition the game condition to be parsed by the gameStatusPanel
     */
    public void finishGame(GameCondition condition) {
        pauseGame();
        gameStatusPanel.finishGame(overseer.getTeam(0), overseer.getTeam(1), condition);
    }

    /**
     * this function moves control of the current type of player to the next type
     */
    public void moveToNextPlayer() {
        //remove the current keyboardController
        this.removeKeyListener(keyboardControl);
        
        Player currentHumanPlayer = overseer.getCurrentHumanPlayer();
        Player prevHumanPlayer = currentHumanPlayer;
        
        //Give the old player an ai.
        prevHumanPlayer.restartAIController();
        
        //cycle through player in the human players team and give it the next player from the one they are using now.
        ArrayList <CollidableObject> allHumanTeamPlayers = overseer.getAll(ObjectType.PLAYERS, overseer.getTeam(0));
        currentHumanPlayer = (Player) allHumanTeamPlayers.get(spacePress++);
        
        //Reset to wrap back around to first player in team for next space bar press.
        if (spacePress == allHumanTeamPlayers.size()) {
            spacePress = 0;
        }
        
        //currentHumanPlayer = (Player) overseer.getNearestObjectNotThis(prevHumanPlayer.getPosition(), prevHumanPlayer, ObjectType.PLAYERS, overseer.getTeam(0));
        keyboardControl = overseer.reinitHumanPlayer(currentHumanPlayer);

        this.addKeyListener(keyboardControl);

    }
    
    private class MenuKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_P:
                    gameStatusPanel.pauseGame();
                    break;
                case KeyEvent.VK_SPACE:
                    moveToNextPlayer();
            }
            
        }
        
    }
}