package quidditchgame.menus;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import quidditchgame.Game;

/**
 * Class Menu. General user interface for Quidditch game. Instantiates the
 * different menu JPanels.
 *
 * @author Jimmy Yuan, Ben Han
 */
public class Menu {

    /**
     * Creates all the elements of the frame for direct access within the class
     */
    private JFrame frame;
    private static final int MENU_WIDTH = 1000;
    private static final int MENU_HEIGHT = 630;
    private Game game;
    private static boolean paused = false;

    // initialize these two panels as they are used to store some game values
    private GameStatusPanel statusPanel = new GameStatusPanel(this);
    private MenuSetupPanel menuSetupPanel = new MenuSetupPanel(this);

    /**
     * Menu constructor to first create a frame then create a panel to add to
     * this frame At the end it initate and generate the frame
     *
     * @param f parent frame containing the menu panels
     */
    public Menu(JFrame f) {

        frame = f;
        //Create and set up the window.
        frame.setBackground(Color.lightGray);
        //frame.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        frame.setResizable(false);

        //Create the panel and add it to the frame
        frame.getContentPane().add(new MenuMainPanel(this, paused));

        //Set size of the frame to full screen, then display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method to get game
     *
     * @param game
     */
    public Game getGame() {
        return game;
    }

    /**
     * This method is used to change the menu panel that is displayed in the
     * main frame of the game.
     *
     * @param panel the panel to be displayed in the main frame
     */
    private void changePane(JPanel panel) {

        //Reset up the window.
        frame.setBackground(Color.lightGray);

        //Remove the previous panels and add the new one
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * This method is used to setup the frame for the main game view.
     *
     * @param panel the game panel
     */
    private void gameScreen(Game panel) {

        //Reset up the window.
        frame.setBackground(Color.lightGray);

        //Remove the previous panels and add the new one
        frame.getContentPane().removeAll();

        JPanel basePanel = new JPanel();
        GroupLayout layout = new GroupLayout(basePanel);
        basePanel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(panel)
                    .addComponent(statusPanel));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent(panel)
                    .addComponent(statusPanel));

        //Display the window.
        frame.getContentPane().removeAll();
        frame.getContentPane().add(basePanel);
        frame.setSize(frame.getMinimumSize());
        frame.setVisible(true);
    }

    /**
     * Called by event handlers when the exit option is chosen by the user.
     * Housekeeping and tidy up that is required before the game closes should
     * be contained here.
     */
    void exitGame() {
        System.exit(0);
    }

    /**
     * Called by event handlers when the resume option is chosen by the user.
     */
    void resumeGame() {
        gameScreen(game);
        paused = false;
        game.resumeGame();
        game.requestFocus();
    }

    /**
     * Called by event handlers when the game is paused by the user.
     */
    void pauseGame() {
        game.pauseGame();
        paused = true;
        changePane(new MenuMainPanel(this, paused));
    }

    /**
     * Called by event handlers when the user clicks "Start game" in the game
     * setup screen.
     */
    void startGame() {
        game = new Game(menuSetupPanel.getTeam1(), menuSetupPanel.getTeam2(), statusPanel, 
                menuSetupPanel.getPlayableCharacter(), menuSetupPanel.getGameTime(), menuSetupPanel.getSnitchScoreValue());
        gameScreen(game);
        game.startGame();
        game.requestFocus();
    }

    /**
     * Called by event handlers when the user clicks "New game" from the main
     * menu.
     */
    void gameSetup() {
        menuSetupPanel = new MenuSetupPanel(this);
        changePane(menuSetupPanel);
    }

    /**
     * Called by event handlers when the user clicks "Instructions" in the main
     * menu.
     */
    void instructions() {
        changePane(new MenuInstructionPanel(this));
    }

    /**
     * Called by event handlers when the user clicks "Instructions" in the main
     * menu.
     */
    void howtoplay() {
        changePane(new MenuHowtoplayPanel(this));
    }

    /**
     * Called by event handlers when the user chooses to return to the main
     * menu.
     */
    void mainMenu() {
        if (game != null)
            game.pauseGame();
        changePane(new MenuMainPanel(this, paused));
    }

    /**
     * Called by event handlers when the user chooses to return to the main
     * menu.
     * @param team1 Team 1 name
     * @param team2 Team 2 name
     * @param score Final game score
     * @param winner Game winner
     */
    void finishGame(String team1, String team2, String score, String winner) {
        changePane(new MenuFinishPanel(this, team1, team2, score, winner));
    }

}
