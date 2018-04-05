package quidditchgame.objects;

import java.awt.geom.Point2D;
import quidditchgame.CompassDirection;
import quidditchgame.ObjectOverseer;
import java.util.Random;

/**
 * This class is used to create movable, collidable objects with drawn sprites.
 * it provides methods for rotating, accelerating, and moving collisions with sprites
 * as well as other useful functions which movable objects often need such as bouncing
 * off other objects
 * @author Team-2, Alex Drinkwater
 */
public abstract class MovableObject extends CollidableObject {

    private double speed = 0; //current speed of the object
    private double targetSpeed = 0; //speed of the object to aim for
    private double objectMaxSpeed = 15.00; // maximum speed the object can reach
    private double acceleration = 0.1;
    double mass = 1; //in relative terms - 1 = standard mass.
    
    //these are used in collisions, they are the speeds and rotations of the object before any collisions took place
    protected double preCollisionSpeed;
    protected double preCollisionRotation;
    
    /* The desired value of rotation. The object will rotate over a number of 
     * ticks until it reaches this value. */
    private double desiredRotation;
    /* The maximum value rotation will be incremented by when adjusting to 
     * desired rotation. Must be a factor of Math.PI/4 */
    protected double maxRotationChange = Math.PI/16; 

            
    /**
     * Creates an object with the ability to move. Passes color, object name,
     * and position on to super to initialise the drawnObject higher up.
     *
     * @param color as defined in enum in Team.
     * @param object
     * @param position
     */
    MovableObject(String object, Point2D.Double position) {
        super(object, position);
        desiredRotation = getRotation();
    }

    public void doTick() {
        /*rotation defined at declaration as 0rad = due north.*/
        Point2D.Double pos = new Point2D.Double(
                getPosition().x + speed * Math.sin(getRotation()),
                getPosition().y - speed * Math.cos(getRotation()));
        setPosition(pos);
        updateRotationTowardDesired();
        accelerateToTargetSpeed();

        //save the speed and rotation from before the collision, for use during collision events.
        preCollisionSpeed = speed;
        preCollisionRotation = getRotation();
    }
    

    /**
     * Overrides setPosition() of DrawnObject, adding in conditions, regarding
     * keeping the object within the field.
     *
     * @param pos
     */
    @Override
    public void setPosition(Point2D.Double pos) {
        super.setPosition(pos);

        //keep the object within the bounds of the field.
        if (pos.x <= 0) {
            super.setPosition(new Point2D.Double(0, getPosition().y));
        } else if (pos.x >= ObjectOverseer.getFieldWidth()) {
            super.setPosition(new Point2D.Double(ObjectOverseer.getFieldWidth(), getPosition().y));
        }
        if (pos.y <= 0) {
            super.setPosition(new Point2D.Double(getPosition().x, 0));
        } else if (pos.y >= ObjectOverseer.getFieldHeight()) {
            super.setPosition(new Point2D.Double(getPosition().x, ObjectOverseer.getFieldHeight()));
        }
    }
    
    /**
     * Calling this function during the doTick() event causes the object to bounce against the edges of the field.
     */
    protected void bounceOnEdges() {
        double xComponent = getSpeed()*Math.cos((getRotation() - Math.PI/2));
        double yComponent = getSpeed()*Math.sin((getRotation() - Math.PI/2));

        //if the object is at the edge of the field and moving outwards then bounce!
        if (getPosition().x <= 0 + getWidth()/2 && xComponent < 0) {
            xComponent = -xComponent;
        } else if  (getPosition().x >= ObjectOverseer.getFieldWidth() - getWidth()/2 && xComponent > 0) {
            xComponent = -xComponent;
        } else if (getPosition().y <= 0 + getHeight()/2 && yComponent < 0) {
            yComponent = -yComponent;
        } else if (getPosition().y >= ObjectOverseer.getFieldHeight() - getHeight()/2 && yComponent > 0) {
            yComponent = -yComponent;
        } else {
            //not at the edge of the field - don't bounce!
            return;
        }
        
        setRotationInstantaneous(Math.atan2(yComponent,xComponent) + Math.PI/2);
    }
    
    /**
     * Calling this function during the doTick() event causes the object to wrap around on the edges of the field.
     */
    protected void wrapAroundOnEdges() {

        Point2D.Double pos = this.getPosition();
        Random random = new Random();

        if (pos.x == 0) {
            this.setPosition(new Point2D.Double(ObjectOverseer.getFieldWidth(), random.nextInt(ObjectOverseer.getFieldHeight())));
        } else if (pos.x == ObjectOverseer.getFieldWidth()) {
            this.setPosition(new Point2D.Double(0, random.nextInt(ObjectOverseer.getFieldHeight())));
        } else if (pos.y == 0) {
            this.setPosition(new Point2D.Double(random.nextInt(ObjectOverseer.getFieldWidth()), ObjectOverseer.getFieldHeight()));
        } else if (pos.y == ObjectOverseer.getFieldHeight()) {
            this.setPosition(new Point2D.Double(random.nextInt(ObjectOverseer.getFieldWidth()), 0));
        }
    }

    /**
     * set the position of the object in terms of x-y coordinates
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setPosition(double x, double y) {
        setPosition(new Point2D.Double(x, y));
    }

    /**
     * set the position of the object relative to the current one.
     * @param x how much to increase the x position by
     * @param y how much to increas
     */
    public void setRelativePosition(double x, double y) {
        setPosition(getPosition().x + x, getPosition().y + y);
    }
    
    /**
     * This function sets the desired rotation of the object. The object will 
     * slowly adjust its course to this desired value over a number of ticks.
     * @param desiredRotation 
     */
    public void setDesiredRotation(double desiredRotation) {
        this.desiredRotation = clipRadians(desiredRotation);
    }
    
    /**
     * Sets the desiredRotation to a point of the compass. Overloading of above.
     * @param direction 
     */
    public void setDesiredRotation(CompassDirection direction) {
        setDesiredRotation(direction.toRadians());
    }

    /**
     * @return returns the rotation angle the object is moving towards
     */
    public double getDesiredRotation() {
        return desiredRotation;
    }
    
    /**
     * Sets the maximum angle the object can change in one tick as it adjusts
     * its rotation to meet that of the desiredRotation.
     * @param change 
     */
    public void setMaxRotationChange(double change) {
        this.maxRotationChange = change;
    }
    
    public void setMaxRotationChange(CompassDirection direction) {
        this.maxRotationChange = direction.toRadians();
    }
    
    /**
     * To be called in the doTick(), this turns the object rotation towards the 
     * desired angle, by up to the maxRotationChange.
     */
    private void updateRotationTowardDesired() {
        /* If we need to rotate to get to the desiredRotation... */
        if (getRotation() != getDesiredRotation()) {
            /* If we are within the maximum change allow each tick, then simply
             * set the rotation to the desiredRotation. This ensures that we 
             * always end up EXACTLY at the rotation desired. */
            if (Math.abs(getRotation() - getDesiredRotation()) < maxRotationChange) {
                setRotationInstantaneous(getDesiredRotation());
            } else { /* Else continue adjusting. */
                /* Rotate via the smallest angle. IE north->north-east, rotate cw. */
                if (Math.abs(getDesiredRotation() - getRotation()) > Math.PI ) {
                    if((getDesiredRotation() - getRotation()) > 0)
                        setRotationInstantaneous(getRotation() - maxRotationChange);
                    else
                        setRotationInstantaneous(getRotation() + maxRotationChange);
                } else /*if ((getDesiredRotation() - getRotation()) < Math.PI )*/ {
                    if((getDesiredRotation() - getRotation()) > 0)
                        setRotationInstantaneous(getRotation() + maxRotationChange);
                    else
                        setRotationInstantaneous(getRotation() - maxRotationChange);
                }
            }
        }
    }
    
    /**
     * Sets the rotation of the player instantly instead of gradually.
     * Overridden so that the desired rotation is set to the new instantaneous rotation.
     * @param rotation 
     */
    @Override
    public void setRotationInstantaneous(double rotation) {
        setDesiredRotation(rotation);
        super.setRotationInstantaneous(rotation);
    }

    /**
     * Returns the current speed
     *
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * set the speed that the movable object should accelerate towards.
     * it is clipped to the maxSpeed of the object.
     * @param newSpeed the new speed to set the object to
     */
    public void setDesiredSpeed(double newSpeed) {
        if (newSpeed >= 0 && newSpeed <= objectMaxSpeed) {
            targetSpeed = newSpeed;
        }
    }

    /**
     * set the speed of the object instantaneously, bypassing acceleration.
     * @param newSpeed
     */
    public void setSpeedInstantaneous(double newSpeed) {
        if (newSpeed <= 0)
            speed = 0;
        else if(newSpeed >= objectMaxSpeed)
            speed = objectMaxSpeed;
        else
            speed = newSpeed;
    }

    /**
     * accelerate towards the current target speed
     *
     */
    public void accelerateToTargetSpeed() {
        if (targetSpeed > speed) {
            increaseSpeed();
        } else if (targetSpeed < speed) {
            decreaseSpeed();
        }
    }

    /**
     * increase the current speed by an incremental amount
     */
    public void increaseSpeed() {
        if (speed + acceleration < getMaxSpeed()) {
            speed += acceleration;
        } else {
            speed = getMaxSpeed();
        }
    }

    /**
     * decrease the current speed by an incremental amount
     */
    public void decreaseSpeed() {
        if (speed - acceleration > 0) {
            speed -= acceleration;
        } else {
            speed = 0;
        }
    }

    /**
     * Returns the current max speed
     *
     * @return speed
     */
    public double getMaxSpeed() {
        return this.objectMaxSpeed;
    }

    /**
     * Sets a new speed
     *
     * @param spd the new speed
     */
    public void setMaxSpeed(double spd) {
        if (spd > 0) {
            this.objectMaxSpeed = spd;
        }
    }

    /**
     * This resets the speed and location of the object; it is useful for when
     * the object is teleported across the map and it should not retain its previous speed
     * @param x the x location to move to
     * @param y the y location to move to
     */
    public void resetLocation(double x,double y) {
        setPosition(x,y);
        setSpeedInstantaneous(0);
    }
    
    
    /**
     * This function moves two colliding objects away from each other until they
     * are no longer touching. if the other object is not a MovableObject then
     * it will not be moved
     *
     * @param other the object to move to so that it is just touching
     */
    public void moveToCollisionPosition(CollidableObject other) {
        moveToCollisionPosition(other, true);
    }
     /*
     * This function moves two colliding objects away from each other until they
     * are no longer touching. if the other object is either not a MovableObject 
     * or moveOther is false then  it will not be moved
     *
     * @param other the object to move to so that it is just touching
     * @param moveOther whether the other object should be moved
     */
    public void moveToCollisionPosition(CollidableObject other, boolean moveOther) {
        
        //if the objects are at the exact same x y position
        //attempting to call doMoveAwayStep would result in an infinite loop
        if (getPosition().equals(other.getPosition())) {
            return;
        }

        int trys = 0;
        while (this.intersects(other)) {
            trys++;
            doMoveToCollisionPositionStep(other);
            if (moveOther) {
                try {
                    ((MovableObject)other).doMoveToCollisionPositionStep(this);
                } catch (Exception e) {
                    //if the other object is not a movable object then it is fine to just let it not move
                }
            }
            //as a fallback teleport the object away.  This should never happen, but it is better than 'stuck' objects.
            if (trys > 60) {
                this.setPosition(this.getPosition().x - 100, this.getPosition().y);
            } else if (trys > 40){
                this.setPosition(this.getPosition().x + 100, this.getPosition().y);
            }

        }
    }

    /**
     * This is a helper function for moveToCollisionPosition(), it moves the
     * current object away from the target object by a single pixel in both the
     * x and y direction.
     *
     * @param other the object to move away from
     */
    void doMoveToCollisionPositionStep(CollidableObject other) {
        //move away in the x direction
        if (this.getPosition().x - other.getPosition().x > 0) {
            setRelativePosition(1, 0);
        } else if (this.getPosition().x - other.getPosition().x != 0) {
            setRelativePosition(-1, 0);
        }

        //move away in the y direction
        if (this.getPosition().y - other.getPosition().y > 0) {
            setRelativePosition(0, 1);
        } else if (this.getPosition().y - other.getPosition().y != 0) {
            setRelativePosition(0, -1);
        }
    }

    /**
     * This returns the speed of the object before collision events were called this tick
     * It is mostly useful in CollisionEvents when another objects CollisionEvent could affect 
     * this ones speed during the same tick.
     * @return the previous speed
     */
    public double getPreCollisionSpeed() {
        return preCollisionSpeed;
    }

    /**
     * This returns the rotation of the object before collision events were called this tick
     * It is mostly useful in CollisionEvents when another objects CollisionEvent could affect 
     * this ones speed during the same tick.
     * @return the previous rotation
     */
    public double getPreCollisionRotation() {
        return preCollisionRotation;
    }

    /**
     * calculates the anular difference between two angles
     * @param firstAngle
     * @param secondAngle
     * @return 
     */
    private double calculateDifferenceBetweenAngles(double firstAngle, double secondAngle)
    {
        double difference = secondAngle - firstAngle;
        while (difference < -Math.PI) difference += 2*Math.PI;
        while (difference > Math.PI) difference -= 2*Math.PI;
        return difference;
    }
    
    
    /**
     * This is the standard collision event for a movable object
     * First the objects are made to bounce using a 2D collision
     * and then they are moved so that they are no longer colliding.
     * @param other the object which was collided with.
     */
    public void collisionEvent(MovableObject other) {

        //bounce against the object
        doCollisionBounce(other);

        //now that the new rotation and speed has been set move the objects apart so that
        //they are no longer colliding
        moveToCollisionPosition(other);
    }

    /**
     * bounce against a colliding object using a physical collision model.
     * @param other The other object that this object is colliding with, used for speed calculations.
     */
    void doCollisionBounce(MovableObject other) {
        double initialSpeed1 = this.getPreCollisionSpeed();
        double initialSpeed2 = other.getPreCollisionSpeed();

        double angle1 = this.getPreCollisionRotation();
        double angle2 = other.getPreCollisionRotation();

        double mass1 = this.mass;
        double mass2 = other.mass;

        //convert rotation to angle from positive x axis using atan2
        angle1 = Math.atan2(Math.cos(angle1), Math.sin(angle1));
        angle2 = Math.atan2(Math.cos(angle2), Math.sin(angle2));

        //calculate angle between colliding objects, i.e. angle of collision axis
        double collisionAngle = Math.atan2(getPosition().y - other.getPosition().y, getPosition().x - other.getPosition().x);

        //convert velocities to 1D collision
        double v1x = initialSpeed1 * Math.cos(angle1 - collisionAngle);
        double v1y = initialSpeed1 * Math.sin(angle1 - collisionAngle);
        double v2x = initialSpeed2 * Math.cos(angle2 - collisionAngle);

        //final velocities in 1D
        double f1x = (v1x * (mass1 - mass2) + 2 * mass2 * v2x) / (mass1 + mass2);
        double f1y = v1y;

        //final velocities
        double v1 = Math.sqrt(f1x * f1x + f1y * f1y);

        //because of divide by zero error (when atan(x,0)) we us logic instead
        double a1;
        if (Math.abs(f1x) < 0.001) {
            a1 = 0 + collisionAngle;
        } else {
            a1 = Math.atan2(f1y, f1x) + collisionAngle;
        }

        //convert back to standard rotation coordinates
        a1 = -a1 + Math.PI / 2;

        //only rotate the collision if a reasonable change in angle occured, otherwise jitter will occur.
        if (Math.abs(calculateDifferenceBetweenAngles(a1, angle1)) > Math.PI/4 ) {
            setRotationInstantaneous(a1);
        }

        //finally, set speed.
        setSpeedInstantaneous(v1);
    }
}
