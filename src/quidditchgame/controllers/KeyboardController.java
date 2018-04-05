package quidditchgame.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import quidditchgame.CompassDirection;
import quidditchgame.objects.ControllableObject;
import quidditchgame.objects.Player;

/**
 *
 * This is the class for the user based player controller.
 * It takes input from the keyboard and translates it into
 * user-controlled player actions.
 *
 */

public class KeyboardController implements KeyListener, Controller {

    ControllableObject controllableObject;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;
    private boolean aAction = false;
    private boolean dAction = false;
    private boolean noDirectionPressed = false;


    public KeyboardController(ControllableObject controllableObject) {
        this.controllableObject = controllableObject;
    }

    /**
     * overriding the keypressed function to add the up,down,left,right
     * A and D keypressed event variable controlls
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;

            case KeyEvent.VK_A:
                aPressed = true;
                break;
            case KeyEvent.VK_D:
                dPressed = true;
                break;
        }
    }
    /**
     * overriding the keypressed function to add the up,down,left,right
     * A and D keyreleased event variable controlls
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_A:
                aPressed = false;
                break;
            case KeyEvent.VK_D:
                dPressed = false;
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    boolean getActionStatus() {
        if (aPressed || dPressed) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function looks at the keys which are currently pressed and then
     * converts them into a Player.Direction and returns it.
     *
     * @return the Player.Direction which the keys are pressed in
     */
    CompassDirection getDirectionFromKeyPresses() {
        noDirectionPressed = false;
        if (upPressed) {
            if (leftPressed) {
                return CompassDirection.NORTH_WEST;
            } else if (rightPressed) {
                return CompassDirection.NORTH_EAST;
            } else {
                return CompassDirection.NORTH;
            }
        } else if (downPressed) {
            if (leftPressed) {
                return CompassDirection.SOUTH_WEST;
            } else if (rightPressed) {
                return CompassDirection.SOUTH_EAST;
            } else {
                return CompassDirection.SOUTH;
            }
        } else if (leftPressed) {
            return CompassDirection.WEST;
        } else if (rightPressed) {
            return CompassDirection.EAST;
        } else {
            noDirectionPressed = true;
            return CompassDirection.NORTH;
        }
    }


    @Override
    public void objectBehaviour() {

        Player player = (Player) controllableObject;
        //Player is performing an action , shooting passing etc

        if (player.isStunned()) {
            player.setDesiredSpeed(0);
        } else {
            //If loading to perform an action
            if (getActionStatus()) {
                CompassDirection direction = getDirectionFromKeyPresses();
                if(!noDirectionPressed)
                    controllableObject.setDesiredRotation(direction);

                    aAction = aPressed;
                    dAction = dPressed;
            } else {
                if (aAction || dAction) {
                    controllableObject.performAction(aAction, dAction);
                    aAction = false;
                    dAction = false;
                }

                // setting up the speed and direction if the controlls keys are
                // used (keys other than A and D)
                CompassDirection direction = getDirectionFromKeyPresses();
                if(!noDirectionPressed)
                    controllableObject.setDesiredRotation(direction);
                
                if (noDirectionPressed) {
                    controllableObject.setDesiredSpeed(0);
                } else {
                    controllableObject.setDesiredSpeed(4);
                }
            }

        }

    }
}
