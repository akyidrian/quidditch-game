package quidditchgame.controllers.bludgerai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import quidditchgame.Team;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Bludger;
import quidditchgame.objects.CollidableObject;
import quidditchgame.objects.Player;
import quidditchgame.ObjectOverseer;


/**
 * This bludger state looks at which team just hit the bludger and then
 * targets non-stunned players on the oppsing team near it.
 * This state should only ever be entered into when the bludger has recently been hit by a beater
 * 
 * @author Aydin
 */
class ChasePlayerState implements AIState {
    Bludger bludger;

    ChasePlayerState(Bludger bludger) {
        this.bludger = bludger;
    }

    /**
     * Method which finds appropriate player and chases them down.
     */
    @Override
    public void doStateBehaviour() {

        //Find a valid player to target.
        Point2D.Double bludgerPos = bludger.getPosition();

        ObjectOverseer objectOverseer = ObjectOverseer.getInstance();

        //Creating an object ignore list for players which are close to the Bludger but are already stunned.
        ArrayList<CollidableObject> playersToNotTarget;

        //Get the last team that hit the bludger.
        Team lastTeamThatHit = bludger.getLastTeamThatHit();

        //Get team that need to be targetted by the bludger.
        Team teamToBeTargetted = objectOverseer.getOtherTeam(lastTeamThatHit);

        //Don't target Beaters.
        playersToNotTarget = objectOverseer.getAll(ObjectOverseer.ObjectType.BEATER, teamToBeTargetted);

        //Get the closest player. We are assuming for now this is a valid unstunned player...
        Player playerTargetted = (Player) objectOverseer.getNearestObject(bludgerPos,
                ObjectOverseer.ObjectType.PLAYERS, teamToBeTargetted);

        //Is this close player stunned?
        boolean playerStunned = playerTargetted.isStunned();

        //Find a player that is close and unstunned.
        if (playerStunned) {

            playersToNotTarget.add(playerTargetted);//add the closest player to ignore list.

            //While this potential close target is stunned, search for another player that isn't.
            while (playerStunned) {
                playerTargetted = (Player) objectOverseer.getNearestObjectNotThese(bludgerPos, playersToNotTarget, ObjectOverseer.ObjectType.PLAYERS, teamToBeTargetted);
                
                // Accounting for the possibility that all players are stunned.
                if (playerTargetted == null) { 
                    return;
                }
                
                playerStunned = playerTargetted.isStunned(); //is this player stunned as well?

                //If it is stunned then ignore this player.
                if (playerStunned) {
                    playersToNotTarget.add(playerTargetted);//add to an ignore list.
                }
            }
        }
            
        //Now chase targeted player.
        double movementSpeed = 4;
        objectOverseer.moveTowardPoint(bludger, playerTargetted.getPosition(), movementSpeed);
            
    }
}
