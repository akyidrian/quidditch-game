package quidditchgame.controllers.beaterai;

import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.controllers.AIController;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Beater;
import quidditchgame.objects.Bludger;
import quidditchgame.objects.CollidableObject;

/**
 * Acts as the context of the BeaterAI State Pattern. 
 * 
 * @author Aydin
 */
public class BeaterAI extends AIController {
    Beater beater;
    
    //All the states of this AI.
    private AIState chaseBludgerState;
    private AIState hitBludgerState;

    ObjectOverseer overseer = null;
    
    
    /**
     * Constructor to assign and setup state machine.
     */
    public BeaterAI(Beater beater) {
        this.beater = beater; 
        overseer = ObjectOverseer.getInstance();
        
        chaseBludgerState = new ChaseBludgerState(beater);
        hitBludgerState = new HitBludgerState(beater);
        
        setState(chaseBludgerState);
        
    }    
    
    /**
     *
     * This method called by objects to get an the AI to update their 
     * position, speed, etc.
     * 
     */
    @Override
    public void determineState() {
              
        //Iterate through the bludgers, and set the bludger that the beater will chaser
        for (CollidableObject bludger : overseer.getAll(ObjectType.BLUDGER)){

            //Closest beater to current bludger
            CollidableObject closestBeater = overseer.getNearestObject(bludger.getPosition(), ObjectType.BEATER, this.beater.getTeam());
            
            //Is this "closest beater" the current beaterAI/me?
            //If so set the bluddger to chase to the current one then break from the loop.
            //If not iterate again untill the closest bludger is found, that does not have a team member
            //chasing it.
            if (closestBeater == this.beater){
                this.beater.setBludger((Bludger)bludger);
                break;
            }
        }
        
        //Distance from the bludger that is being chased and the beater
        double dist = this.beater.getBludger().getPosition().distance(this.beater.getPosition());

        if (dist < 30){
            setState(hitBludgerState);
            
        }        
        else{
            setState(chaseBludgerState);
  
        }
    }
}
