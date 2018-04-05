package quidditchgame;

import javax.swing.JFrame;
import quidditchgame.menus.Menu;

/**
 *
 * This is the main class for the Quidditch game.
 * It initilises the frame, menu, and game.
 * 
 */

public class Main {

    private static Menu menu;
    private static JFrame frame;


    public static Menu getMenu() {
        return menu;
    }
    
    public static Game getGame() {
        return menu.getGame();
    }

    public static void main(String[] args) {

        // Set up the frame for the program to run in
        frame = new JFrame("The Quidditch Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // create a Menu object to control the frame display
        menu = new Menu(frame);

    }
}
