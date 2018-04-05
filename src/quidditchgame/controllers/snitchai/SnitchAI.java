package quidditchgame.controllers.snitchai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIController;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Snitch;

/**
 * Acts as the context of the SnitchAI State Pattern.
 *
 * @author Aydin Arik
 * Checked: Chris
 * Date: 13/07/2012
 */
public final class SnitchAI extends AIController {

    private ObjectOverseer objectOverseer;
    private Snitch snitch = null;
    
    //All the states of this AI.
    private AIState flyRandomlyState;
    private AIState avoidSeekerState;
    
    //Pixel radius which the snitch starts to 'feel' that the seekers are close.
    private static final int RADIAL_AWARENESS_DIST = 150;

    /**
     * Initializer for snitch AI
     * 
     * @param snitch Snitch for which the AI will be utilized.
     */
    public SnitchAI(Snitch snitch) {
        this.snitch = snitch;

        objectOverseer = ObjectOverseer.getInstance();

        flyRandomlyState = new FlyRandomlyState(snitch);
        avoidSeekerState = new AvoidSeekerState(snitch, RADIAL_AWARENESS_DIST);

        setState(flyRandomlyState); //Default state is fly randomly.
    }

    /**
     * Determines which state to use based on seeker positions
     */
    @Override
    public void determineState() {
        //get the position of the closes seeker
        Point2D.Double closestSeekerPos = objectOverseer.getPositionOfNearestObject(snitch.getPosition(), ObjectOverseer.ObjectType.SEEKER);
        
        //find the distance to it.
        double distBetweenObjs = closestSeekerPos.distance(snitch.getPosition());

        //if the seeker is close, than switch to an avoiding state
        //otherwise just use the fly randomly state
        if (distBetweenObjs <= RADIAL_AWARENESS_DIST) {
            setState(avoidSeekerState);
        } else {
            setState(flyRandomlyState);
        }
    }
}
