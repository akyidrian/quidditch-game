package quidditchgame.controllers.snitchai;

import java.awt.geom.Point2D;
import java.util.Random;
import quidditchgame.ObjectOverseer;
import quidditchgame.objects.Seeker;
import quidditchgame.objects.Snitch;

/**
 * A state of the state pattern.
 *
 * Snitch flies somewhat randomly whilst avoiding seekers.
 *
 * @author Aydin
 * Checked: Chris
 * Date: 13/07/2012 
 */
class AvoidSeekerState extends SnitchState {

    // Used to limit snitch rotation when chased. In this case the angle range in the V is 50degrees.
    double exactBearing;
    private boolean snitchCornered = false;
    private static final int MAX_RUNAWAY_ROTATION_DEG = 20; //in degrees
    private static final double MAX_RUNAWAY_ROTATION_RAD = Math.PI / (180 / MAX_RUNAWAY_ROTATION_DEG);
    private static final int POSSIBLE_NUMS = 100;
    SnitchAI snitchAI = null;
    Snitch snitch = null;
    ObjectOverseer objectOverseer;
    double MAX_ROTATION_CHANGE = Math.PI / 8;
    double radialAwarenessDist;
    Random rng = new Random();

    /**
     * Initializes Avoid Seeker State
     * @param snitch The snitch for which this state will be implemented
     * @param radialAwarenessDist The radial distance from the snitch that it
     * will be able to be aware of Seekers within.
     */
    AvoidSeekerState(Snitch snitch, double radialAwarenessDist) {
        this.snitch = snitch;
        this.radialAwarenessDist = radialAwarenessDist;
        this.objectOverseer = ObjectOverseer.getInstance();
    }

     /**
     * The default behaviour when a seeker is close to the snitch
     *  - get surrounding seekers' positions within the radius
     *  - actively avoid the seekers
     */
    @Override
    public void doStateBehaviour() {

        setSpeedRandomly();

        Seeker closestSeeker = (Seeker) objectOverseer.getNearestObject(snitch.getPosition(), ObjectOverseer.ObjectType.SEEKER);
        Seeker otherSeeker = (Seeker) objectOverseer.getNearestObjectNotThis(snitch.getPosition(), closestSeeker, ObjectOverseer.ObjectType.SEEKER, null);

        Point2D.Double snitchPos = snitch.getPosition();
        Point2D.Double closestSeekerPos = closestSeeker.getPosition();

        //Pixel distance between the Snitch and the Seeker.
        double furthestSeekerDistance = snitch.getPosition().distance(otherSeeker.getPosition());

        //Find the pixel x, y components and angle of the vector representing the point between
        //the Snitch and the closest Seeker.
        double closestSeekerX = closestSeekerPos.x - snitchPos.x;
        double closestSeekerY = closestSeekerPos.y - snitchPos.y;
        double closestSeekerRot = Math.atan2(closestSeekerY, closestSeekerX) - Math.PI / 2;

        double newSnitchDir;

        //Checking if the other Seeker is also within the Snitches seeker awareness
        //zone.
        if (furthestSeekerDistance < radialAwarenessDist) {

            //Find the pixel x, y components and angle of the vector representing the point between
            //the Snitch and the other Seeker. 
            Point2D.Double otherSeekerPos = otherSeeker.getPosition();
            double otherSeekerX = otherSeekerPos.x - snitchPos.x;
            double otherSeekerY = otherSeekerPos.y - snitchPos.y;
            double otherSeekerRot = Math.atan2(otherSeekerY, otherSeekerX) - Math.PI / 2;

            //Find the pixel x, y components and angle of the addition vector
            //of closestSeeker and otherSeeker. Used to help decide the route the
            //Snitch can take to leave the greatest angle between it and the Seekers.
            double combinedVectorX = closestSeekerX + otherSeekerX;
            double combinedVectorY = closestSeekerY + otherSeekerY;
            double combinedVectorRot = Math.atan2(combinedVectorY, combinedVectorX) - Math.PI / 2;

            //The angle between the Seekers
            double angleDiff = Math.abs((closestSeekerRot - otherSeekerRot));

            if (Math.toDegrees(angleDiff) >= 0 && Math.toDegrees(angleDiff) < 180) {
                //Snitch should go in the direction which leaves the largest angle between it and the Seekers.
                exactBearing = combinedVectorRot;

                //Reset this flag to say that the Snitch got away from a 180 degree trap situation.
                snitchCornered = false;

            } else { //When angleDiff == 180 degrees. This is the trap situation where the Seekers are sandwiching the Snitch.

                //Check whether Snitch got away from the trapped situation or not.
                if (!snitchCornered) {

                    //Set this flag to say that the Snitch got away from a 180 degree trap situation.
                    snitchCornered = true;

                    //Find the pixel x, y components and angle of the difference vector
                    //of closestSeeker and otherSeeker. This is used to help decide the route the
                    //Snitch can take to get out of the Seeker trap situation. 
                    double differenceVectorX = closestSeekerX - otherSeekerX;
                    double differenceVectorY = closestSeekerY - otherSeekerY;
                    double differenceVector = Math.atan2(differenceVectorY, differenceVectorX) - Math.PI / 2;

                    //Generate random boolean to help decide which of the two possible directions
                    //to choose. For example, if the Snitch and Seeker lie on the y=100 line, then
                    //the Snitch can go up (0deg) or down (180deg) to get away from the Seekers.
                    boolean positiveRotation = rng.nextBoolean();

                    //if the Seekers are equally far away from the Snitch, then Snitch moves randomly to try get away.
                    if (differenceVectorX == 0 && differenceVectorY == 0) {
                        double randomRotation = 2 * Math.PI * rng.nextDouble();
                        snitch.setDesiredRotation(randomRotation);

                    } else if (positiveRotation) {
                        //else there is a difference vector that can decide a direction
                        //for the Snitch to go.
                        exactBearing = differenceVector + Math.PI / 2;
                    } else {
                        exactBearing = differenceVector - Math.PI / 2;
                    }
                    //If the Snitch is trying to get away from the trap but hasn't
                    //done so sucessfully yet...
                } else {
                    snitchCornered = true;
                }
            }

            //Only the closest Seeker is chasing the Snitch. Hence, we don't care about
            //the other Seeker.
        } else {
            exactBearing = closestSeekerRot;
            snitchCornered = false;

        }

        //Generate and a set a the Snitches rotation.
        Random generator = new Random();
        newSnitchDir = (Math.toRadians(generator.nextInt(MAX_RUNAWAY_ROTATION_DEG + 1) - 1) - MAX_RUNAWAY_ROTATION_RAD) + exactBearing;
        snitch.setDesiredRotation(newSnitchDir);

        avoidSeekersAtFieldBounds(snitch); // A final check to make sure a Seeker isn't trying to trick the Snitch.
    }

    /**
     * Sets a random speed for the snitch
     */
    private void setSpeedRandomly() {

        curTick = ++curTick;
        if (curTick == TICKS_PER_SPEED_CHANGE) {
            curTick = 0;

            //Select a Snitch speed randomly.
            int randNum = rng.nextInt(POSSIBLE_NUMS);
            snitch.setSpeedInstantaneous(0);
            if (randNum == 1) { //1% chance of Snitch slowing down to the slowest (0).
                snitch.setSpeedInstantaneous(0);
            } else if ((randNum >= 2) && (randNum <= 5)) {//4% chance of Snitch of slowing to 1.
                snitch.setSpeedInstantaneous(1);
            } else if ((randNum >= 6) && (randNum <= 10)) {//5% chance of Snitch of slowing to 2.
                snitch.setSpeedInstantaneous(2);
            } else if ((randNum >= 11) && (randNum <= 20)) {//10% chance of Snitch of slowing to 3.
                snitch.setSpeedInstantaneous(3);
            } else if ((randNum >= 21) && (randNum <= 40)) { //20% chance of Snitch of slowing to 4.
                snitch.setSpeedInstantaneous(4);
            } else if ((randNum >= 41) && (randNum <= 100)) {//60% chance of Snitch being at full speed (5).
                snitch.setSpeedInstantaneous(5);
            }
        }
    }
}
