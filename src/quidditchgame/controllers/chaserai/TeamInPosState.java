package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D;
import quidditchgame.ObjectOverseer;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;
import quidditchgame.objects.CollidableObject;

/**
 *
 * @author Aydin
 */
public class TeamInPosState implements AIState {
    ChaserAI chaserAI = null;

    ObjectOverseer objOver = null;
    protected final static int QUAFFLE_DISTANCE = 100;


    TeamInPosState (ChaserAI chaserAI) {
        this.chaserAI = chaserAI;
    }

    /**
     * The default behaviour when our team is in possession of the quaffle:
     *  - position self in a good pass position
     *  - get location of teammate
     *  - fly to point half way between teammate and goal but not directly between
     *  - up to the side a bit so out of the way but ready to receive a pass
     */
    @Override
    public void doStateBehaviour(){
        Chaser chaser = chaserAI.chaser;
        objOver = ObjectOverseer.getInstance();
        Point2D.Double goalPos = objOver.getFirst(ObjectOverseer.ObjectType.GOAL_HOOP, objOver.getOtherTeam(chaser.getTeam())).getPosition();

        Chaser chaserWithBall = null;
        for(CollidableObject obj : objOver.getAll(ObjectOverseer.ObjectType.CHASER, chaser.getTeam())){
            chaserWithBall = (Chaser) obj;
            if(chaserWithBall.getPossessionStatus()){
                break;
            }
        }

        Point2D.Double newPos = new Point2D.Double();
        Point2D.Double quafflePos = chaserWithBall.getPosition();

        newPos.x = ((goalPos.x - quafflePos.x) / 2) + chaserWithBall.getPosition().x;
        if(chaser.getPosition().y >= 300){
            //newPos.y = 450;
            if ((quafflePos.y + QUAFFLE_DISTANCE) < ObjectOverseer.getFieldHeight())
                newPos.y =  quafflePos.y + QUAFFLE_DISTANCE;
            else
                newPos.y = ObjectOverseer.getFieldHeight() - QUAFFLE_DISTANCE/2;
        }
        else{
            //newPos.y = 150;
            if ((quafflePos.y - QUAFFLE_DISTANCE) > 0)
                newPos.y =  quafflePos.y - QUAFFLE_DISTANCE;
            else
                newPos.y = ObjectOverseer.getFieldHeight() + QUAFFLE_DISTANCE/2;
        }

        objOver.moveTowardPoint(chaser, newPos, 5);
    }
}
