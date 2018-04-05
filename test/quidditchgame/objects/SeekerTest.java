/*
 *  Junit test for the seeker.java class
 *  Author: Chris Chester
 *  Created: 10/07/12
 */
package quidditchgame.objects;

import org.junit.*;
import static org.junit.Assert.*;
import quidditchgame.Game;
import quidditchgame.Team;

/**
 *
 * @author Chris Chester
 */
public class SeekerTest {
    private Seeker instance = null;
    private Snitch snitch = null;
    private Game game = null;
    private final static int MAX_SPEED = 5;
    private final static int BOOST_SPEED = 6; // new max speed due to boost
    private final static int BOOST_TIME = 500; // duration of boost in ms
    private final static int BOOST_COOLDOWN = 5000; // duration of cool down
    
    public SeekerTest() {
    }
    
    @Before
    public void setUp() {
        Team team = new Team("team",Team.color.red,Team.side.LEFT);
        instance = new Seeker(team,game);
        snitch = new Snitch();
        instance.possessionStatus = true;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of collisionEvent method, of class Seeker.
     */
    @Test
    public void testCollisionEvent_Snitch() {
        snitch.setPosition(instance.getPosition());
        
        instance.collisionEvent(snitch);
        assertEquals(true,instance.getPossessionStatus());
       
    }

    /**
     * Test of collisionEvent method, of class Seeker.
     */
    @Test
    public void testCollisionEvent_Bludger() {
        Bludger bludger = new Bludger();
        bludger.setPosition(instance.getPosition());
        
        instance.collisionEvent(bludger);
        assertEquals(true,instance.isStunned());
    }

    /**
     * Test of paint method, of class Seeker.
     */
    @Test
    public void testPaint() {
           //All visual
    }

    /**
     * Test of performAction method, of class Seeker.
     */
    @Test
    public void testPerformAction() {
        boolean aPressed = false;
        boolean dPressed = false;
        instance.performAction(aPressed, dPressed);
        assertEquals(false,instance.isBoosting());
        
        aPressed = true;
        instance.performAction(aPressed, dPressed);
        assertEquals(true,instance.isBoosting());
        
        dPressed = true;
        instance.performAction(aPressed, dPressed);
        assertEquals(true,instance.isBoosting());
        
    }

    /**
     * Test of startBoost method, of class Seeker.
     */
    @Test
    public void testStartBoost() {
         instance.startBoost();
         assertEquals(true,instance.getSpeed() == BOOST_SPEED);
         assertEquals(true,instance.isBoosting());
         
    }

    /**
     * Test of finishBoost method, of class Seeker.
     */
    @Test
    public void testFinishBoost() {
        instance.finishBoost();
        assertEquals(false,instance.isBoosting());
        assertEquals(true,instance.isCoolDown());
        assertEquals(false,instance.getSpeed()>MAX_SPEED);

    }

    /**
     * Test of isBoosting method, of class Seeker.
     */
    @Test
    public void testIsBoosting() {
        assertEquals(false, instance.isBoosting());
        //Getter, assume rest works
    }

    /**
     * Test of isCoolDown method, of class Seeker.
     */
    @Test
    public void testIsCoolDown() {
        assertEquals(false, instance.isCoolDown());
         //Getter, assume rest works
        
    }

    /**
     * Test of doTick method, of class Seeker.
     */
    @Test
    public void testDoTick() {
       //TODO annoying to test as have to fully det up game for the AI
        
        
    }
}
