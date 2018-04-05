package quidditchgame.controllers.chaserai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import quidditchgame.ObjectOverseer;
import quidditchgame.ObjectOverseer.ObjectType;
import quidditchgame.Team;
import quidditchgame.controllers.AIController;
import quidditchgame.controllers.AIState;
import quidditchgame.objects.Chaser;
import quidditchgame.objects.CollidableObject;
import quidditchgame.objects.Keeper;

/**
 * Acts as the context of the ChaserAI State Pattern.
 *
 * @author Aydin, Ashley
 * Checked By Chris
 * Date: 13/07/2012
 */
public class ChaserAI extends AIController{

    static final double CLOSE_TO_GOAL_FACTOR = 0.20;
    static final double LEFT_GOAL_AREA = 100;
    static final double RIGHT_GOAL_AREA = 900;
    static final double MAX_PASS_RANGE = 300;
    static final double MIN_PASS_RANGE = 200;
    static final double MAX_SHOOTING_DIST = 300;
    static final double IDEAL_PASS_DIST_FROM_GOAL = 400;
    static final double KEEPER_BLOCK_DIST = 200;
    static final double MOVING_FAST_SPEED = 3;
   
    Chaser chaser = null;
    protected Chaser friendly_in_pass_pos = null;
    
    private ObjectOverseer objOver;

    private AIState chaseQuaffleState;
    private AIState goToGoalState;
    private AIState shootState;
    private AIState teamInPosState;
    private AIState enemyInPosState;
    private AIState passState;
    private AIState waitForPassState;
    
    private Team team;
    private Team enemyTeam;
    

    /**
     * Constructor to assign and setup state machine.
     * @param chaser The chaser who will be controlled by this AI.
     */
    public ChaserAI(Chaser chaser) {
        this.chaser = chaser;

        objOver = ObjectOverseer.getInstance();
        friendly_in_pass_pos = null; 

        chaseQuaffleState = new ChaseQuaffleState(this);
        goToGoalState = new GoToGoalState(this);
        shootState = new ShootState(this);
        teamInPosState = new TeamInPosState(this);
        enemyInPosState = new EnemyInPosState(this);
        passState = new PassState(this);
        waitForPassState = new WaitForPassState(this);

        setState(chaseQuaffleState);
    }
    
    /**
     * Determines and sets the state that the Chaser should be set to.
     */
    @Override
    public void determineState(){
        team = chaser.getTeam();
        enemyTeam = objOver.getOtherTeam(team);
        
        Point2D.Double chaser_pos = chaser.getPosition();
        Point2D.Double goal_pos = objOver.getNearestObject(chaser_pos, ObjectType.GOAL_HOOP, enemyTeam).getPosition();
        Point2D.Double quaffle_pos = objOver.getFirst(ObjectType.QUAFFLE).getPosition();

        double distFromGoal = chaser_pos.distance(goal_pos);
        double goodShootDist = objOver.getFieldWidth() * CLOSE_TO_GOAL_FACTOR;
        
        boolean enemyInPos = checkQuafflePosession(enemyTeam);
        boolean friendlyInPos = checkQuafflePosession(team);
        
        friendly_in_pass_pos = null;       
           
        //used to make sure only one player from each team chases the quaffle.
        boolean okToChase = false;

        //Looks for nearest Chaser to the Quaffle in this team
        CollidableObject nearestChaser = objOver.getNearestObject(quaffle_pos, ObjectType.CHASER, this.chaser.getTeam());

        if (nearestChaser == this.chaser) {
            okToChase = true;
        }

        //Setting states based on positions and possession
        if (chaser.getPossessionStatus()) {
            
            friendly_in_pass_pos = checkFriendlyInPassPos(team);
            
            // Looks for if in a good shooting position
            if (distFromGoal < goodShootDist && chaser.getPosition().x > LEFT_GOAL_AREA
                                               && chaser.getPosition().x < RIGHT_GOAL_AREA) {
                setState(shootState);
            } else if (friendly_in_pass_pos != null) {

                setState(passState);
            } else {
                
                setState(goToGoalState);
            }
        } else {
            if (enemyInPos && okToChase) {

                setState(enemyInPosState);
            } else if (friendlyInPos && !okToChase && distFromGoal > MAX_SHOOTING_DIST) {

                setState(teamInPosState);
            } else if (okToChase) {

                setState(chaseQuaffleState);
            } else {

                setState(waitForPassState); 
            }
        }        
    }

    /**
     * Checks to see if Team is in possession of the Quaffle.
     *
     * @param team Team to check for possession
     * @return Returns whether any of the Chasers in the team are in possession
     * of the Quaffle.
     */
    private boolean checkQuafflePosession(Team team){
        for (CollidableObject object : objOver.getAll(ObjectType.CHASER, team)) {
            Chaser obj = (Chaser) object;
            if (obj.getPossessionStatus()) {
                return true;
            }
        }
        //get teams keeper
        Keeper keeper = (Keeper)objOver.getFirst(ObjectType.KEEPER, team);
        //check keeper for possesion also
        if(keeper.getPossessionStatus()){
            return true;
        }
        return false;
    }

    /**
     * Checks to see which chaser on the team are in the best passing position. 
     * 
     * @param team Team to check chasers position.
     * @return Returns the chaser in the best passing position.
     */
    private Chaser checkFriendlyInPassPos(Team team){        
        
        ArrayList<CollidableObject> chaserList = objOver.getAll(ObjectType.CHASER, team);
        ArrayList<Chaser> chaserWithOutBallList = new ArrayList<Chaser>();
        
        //Finds possible chasers to pass to.
        for(CollidableObject obj : chaserList){
            Chaser obj2 = (Chaser)obj;
            if (obj2 != this.chaser) chaserWithOutBallList.add(obj2);
        }
        
        for (int i = 0; i < chaserWithOutBallList.size(); i++){
            
            Chaser otherChaser = (Chaser)chaserWithOutBallList.get(i);
            
            // Checks to see if chaser is in a good passing position.
            if (otherChaser.getPosition().distance(chaser.getPosition()) < MAX_PASS_RANGE && 
                otherChaser.getPosition().distance(chaser.getPosition()) > MIN_PASS_RANGE &&
                //checks to see if other chaser is close to the goal to pass to
                otherChaser.getPosition().distance(objOver.getFirst(ObjectType.GOAL_HOOP, objOver.getOtherTeam(team)).getPosition()) < IDEAL_PASS_DIST_FROM_GOAL &&
                //Checks to see if chaser in posession is far enough away from keeper to pass effectively.
                otherChaser.getPosition().distance(objOver.getFirst(ObjectType.KEEPER, objOver.getOtherTeam(team)).getPosition()) > KEEPER_BLOCK_DIST &&
                //checks to see if other chaser is moving slow enough to pass to
                otherChaser.getSpeed() < MOVING_FAST_SPEED){
                
                    return otherChaser;  
            }
        }
        
        return null;
    }
}
