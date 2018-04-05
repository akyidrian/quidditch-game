package quidditchgame.objects;

import java.awt.geom.Point2D;
import quidditchgame.controllers.Controller;

/**
 * ControllableObject defines objects that have a controller, either a human
 * interface, or an AI.
 *
 * @author Chris Chester, Campbell Letts
 */
public abstract class ControllableObject extends MovableObject {

    /*
     * The controller for this object. Defined in terms of the controller
     * interface.
     */
    private Controller controller = null;

    /**
     * Creates a controllable Object, passing object name, and position
     * on to its superclass.
     *
     * @param object the image name for the object
     * @param position the initial position of the object
     */
    public ControllableObject(String object, Point2D.Double position) {
        super(object, position);
    }

    /*
     * For performing an action based on the key press of the input keys a and d
     * @param aPressed was the A key pressed?
     * @param dPressed was the D key pressed?
     */
    public void performAction(boolean aPressed, boolean dPressed) {
    }

    /**
     * Progresses the object to the next instance in time - including doing a controller tick
     */
    @Override
    public void doTick() {
        super.doTick();
        if (controller != null) {
            controller.objectBehaviour();
        }
    }

    /**
     * Sets the controller to the inputted controller
     * @param controller reference to the controller object that is controlling
     * this object
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * @return returns true if the controller is a human player
     */
    public boolean isHumanController() {
        return this.controller instanceof quidditchgame.controllers.KeyboardController;
    }
}
