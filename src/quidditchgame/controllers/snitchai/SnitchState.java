package quidditchgame.controllers.snitchai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.CollidableObject;
import quidditchgame.objects.Snitch;

/**
 * Abstract class for the different states of the snitch
 * 
 * @author Aydin
 */
abstract class SnitchState implements AIState {

    public enum snitchToWrapAroundTo {

        NONE, TOP, BOTTOM, LEFT, RIGHT
    }
    
    
    protected int curTick = 0;
    protected final static int TICKS_PER_SPEED_CHANGE = 100;
    protected final static int EDGE_DIST = 5;
    protected final static int CLOSE_TO_EDGE = 20;

    /**
     * reset tick for random seeker speeds.
     */
    protected void resetTick () {
        curTick = 0; 
    }
    
    /**
     * Method to avoid checky Seekers waiting for one SnitchState to push the
     * Snitch to wrap around so that they can catch it. The snitch teleports to
     * another to an unexpected edge to avoid the cross map seeker trap.
     *
     * @param snitchPos Position of the snitch
     */
    protected void avoidSeekersAtFieldBounds(Snitch snitch) {

        ObjectOverseer objectOverseer = ObjectOverseer.getInstance();

        ArrayList<CollidableObject> seekersList = objectOverseer.getAll(ObjectOverseer.ObjectType.SEEKER);
        Point2D.Double snitchPos = snitch.getPosition();

        AvoidSeekerState.snitchToWrapAroundTo locToGoTo = AvoidSeekerState.snitchToWrapAroundTo.NONE;
        if (snitchPos.x >= (ObjectOverseer.getFieldWidth() - EDGE_DIST)) {//snitch on right side of field
            locToGoTo = AvoidSeekerState.snitchToWrapAroundTo.LEFT;
        } else if (snitchPos.x <= EDGE_DIST) {//snitch on left side of field
            locToGoTo = AvoidSeekerState.snitchToWrapAroundTo.RIGHT;
        } else if (snitchPos.y >= (ObjectOverseer.getFieldHeight() - EDGE_DIST)) {//snitch on south side of field
            locToGoTo = AvoidSeekerState.snitchToWrapAroundTo.TOP;
        } else if (snitchPos.y <= EDGE_DIST) {//snitch on north side of field
            locToGoTo = AvoidSeekerState.snitchToWrapAroundTo.BOTTOM;
        }


        if (locToGoTo != AvoidSeekerState.snitchToWrapAroundTo.NONE) {

            Random rnd = new Random();
            int rndNum;
            int rndSideNum;

            Point2D.Double pos;
            Point2D.Double teleportPos;
            for (CollidableObject seeker : seekersList) {
                pos = seeker.getPosition();

                //Seeker is on the right side of the field and snitch is about to 
                //wrap around to the right.
                if ((pos.x >= (ObjectOverseer.getFieldWidth() - 20)) && (locToGoTo == AvoidSeekerState.snitchToWrapAroundTo.RIGHT)) {
                    rndNum = rnd.nextInt(ObjectOverseer.getFieldWidth());
                    rndSideNum = rnd.nextInt(1);
                    teleportPos = new Point2D.Double(rndNum, rndSideNum * ObjectOverseer.getFieldHeight());
                    snitch.setPosition(teleportPos);

                    //Seeker is on the left side of the field and snitch is about to 
                    //wrap around to the left.
                } else if ((pos.x <= 20) && (locToGoTo == AvoidSeekerState.snitchToWrapAroundTo.LEFT)) {
                    rndNum = rnd.nextInt(ObjectOverseer.getFieldWidth());
                    rndSideNum = rnd.nextInt(1);
                    teleportPos = new Point2D.Double(rndNum, rndSideNum * ObjectOverseer.getFieldHeight());
                    snitch.setPosition(teleportPos);

                    //Seeker is on the south side of the field and snitch is about to 
                    //wrap around to the south.
                } else if ((pos.y >= (ObjectOverseer.getFieldHeight() - CLOSE_TO_EDGE)) && (locToGoTo == AvoidSeekerState.snitchToWrapAroundTo.BOTTOM)) {
                    rndNum = rnd.nextInt(ObjectOverseer.getFieldHeight());
                    rndSideNum = rnd.nextInt(1);
                    teleportPos = new Point2D.Double(rndSideNum * ObjectOverseer.getFieldWidth(), rndNum);
                    snitch.setPosition(teleportPos);

                    //Seeker is on the north side of the field and snitch is about to 
                    //wrap around to the north.
                } else if ((pos.y <=  CLOSE_TO_EDGE) && (locToGoTo == AvoidSeekerState.snitchToWrapAroundTo.TOP)) {
                    rndNum = rnd.nextInt(ObjectOverseer.getFieldHeight());
                    rndSideNum = rnd.nextInt(1);
                    teleportPos = new Point2D.Double(rndSideNum * ObjectOverseer.getFieldWidth(), rndNum);
                    snitch.setPosition(teleportPos);
                }
            }
        }
    }
}
