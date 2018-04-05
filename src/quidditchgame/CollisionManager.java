package quidditchgame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import quidditchgame.objects.CollidableObject;

/**
 * Explaination of Collision Events
 *
 * Generally speaking, if two objects collide, the collision is detected by
 * the CollisionManager. The CollisionManager then notifies each object that
 * they collided with another object in the form of a collisionEvent. These
 * events take the form of:
 *      public void collisionEvent( objectYouCollidedWith );
 *
 * The collisionManager deals with all objects on the field as being of
 * type CollidableObject. As it purely deals with detecting collisions and
 * notifying the two objects that they have collided with the other, it
 * treats the objects with this generic type.
 *
 * When you collide with another object, and are notified of the
 * collision by the collisionManager, it is useful to know the class of the
 * object you have collided with in order to take appropriate action. For
 * instance, a Chaser will pick up the Quaffle when it collides with it, but
 * when it collides with another Chaser it will simply bounce off.
 *
 * To effect these different events when colliding, the collisionEvent()
 * method has been overloaded. A generic collisionEvent with an object of
 * type CollidableObject is located in the MovableObject class, while move
 * specific, overloaded methods are declared as you descend the inheritance
 * tree.
 *      For Example, a Snitch has an overloaded call for objects of type
 *      Seeker, that allows the seeker to pick up the snitch when it
 *      collides with it (ending the game, incidentally). For almost all
 *      other cases of a collision, the code ascends the inheritance tree
 *      and calls the generic collisionEvent defined in MovableObject.
 */

/**
 * Used to do collision detection - provided an array of collidable objects it 
 * calls collisionEvents for all colliding objects.
 * @author Matthew Wigley, Campbell Letts
 */
class CollisionManager {

    /**
     * Iterate through all the CollidableObjects provided and, in the event of 
     * a collision call their collisionEvent.
     *
     *      ***  NOTE THAT COLLISION EVENTS MUST BE DECLARED PUBLIC  ***
     *
     * @param objects List of all collidableObjects in the field of play.
     */
    void fireCollisionEvents(ArrayList<CollidableObject> objects) {

        for (int i = 0; i < objects.size(); i++) {
            for (int j = i+1; j < objects.size(); j++) {
                CollidableObject object1 = objects.get(i);
                CollidableObject object2 = objects.get(j);

                if (object1.intersects(object2)) {
                    /* Calls the collisionEvent of each object, passing it the 
                     * other object as reference  */
                    callCollisionEvent(object1,object2);
                    callCollisionEvent(object2,object1);
                }
            }
        }
    }

    /* callCollisionEvent calls the most derived collisionEvent method.
     * 
     * @param object1 
     * @param object2
     */
    void callCollisionEvent(CollidableObject object, CollidableObject other) {
        /* The 'true runtime type' of the object you have collided with. */
        Class otherObjectsClass = other.getClass();

        while (true) {
            try {
                // Attempt to find the most derived collisionEvent method call.
                Method method = object.getClass().getMethod("collisionEvent", otherObjectsClass);
                // Having found the applicable method we then invoke it. In this
                // case this is equavalent to writing object.collisionEvent(other)
                method.invoke(object,other);
                return;
            } catch (IllegalAccessException e) {
                // Illegal access exceptions should not happen because all collision events should be public
                System.err.println("Error: Illegal access exception in collision manager: " + e);
                return;
            } catch (InvocationTargetException e) {
                // Invocation target exceptions should not occur because the correct target should be called.
                System.err.println("Error: InvocationTargetException in collision manager: " + e);
                return;
            } catch (NoSuchMethodException e) {
                // If object's class does not have a specific collisionEvent for
                // dealing with other's type, then we try again using other's
                // superclass.
                otherObjectsClass = otherObjectsClass.getSuperclass();
            }
        }
    }
}