package quidditchgame.controllers;

/**
 * Interface for allowing the Strategy Pattern for the Controller section of the
 * Quidditch game. This pattern allows the AI and keyboard control to be seen as
 * one in the same.
 *
 * @author Aydin
 */
public interface Controller {

    //Each controller will have their own behaviour by overriding this function
    void objectBehaviour();
}
