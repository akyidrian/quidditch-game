/*
 *  Junit test for the beater.java class
 *  Author: Chris Chester
 *  Date: 09/07/12
 */
package quidditchgame.objects;

import org.junit.*;
import static org.junit.Assert.*;
import quidditchgame.Team;
/**
 *
 * @author Chris Chester
 */
public class BeaterTest {
    private final static int MAX_SPEED = 5;
    private final static int HIT_FRAMES_COUNT = 3; 
    private final static int ANIMINATION_SPEED_FACTOR = 4; //quarter speed animation
    private final static int MAX_BLUDGER_DISTNACE_TO_HEAD = 25;
    private Beater instance = null;
    
    public BeaterTest() {
               
    }

    
    @Before
    public void setUp() {
        Team team = new Team("team",Team.color.red,Team.side.LEFT);
        instance = new Beater(team);
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of doTick method, of class Beater.
     */
    @Test
    public void testDoTick() {
        //Hard to test
    }

    /**
     * Test of paint method, of class Beater.
     */
    @Test
    public void testPaint() {
        //Visual Test
    }

    /**
     * Tests the stunning of a collision with a bludger
     */
    @Test
    public void testStunEvent() {
        Bludger bludger = new Bludger();
        bludger.setPosition(instance.getPosition().x + MAX_BLUDGER_DISTNACE_TO_HEAD
                ,instance.getPosition().y + MAX_BLUDGER_DISTNACE_TO_HEAD);
        instance.collisionEvent(bludger);
        assertEquals(true,instance.isStunned());
    }
    /**
     * Tests the Hitting of a collision with a bludger
     */
    @Test
    public void testHitEvent() {
        Bludger bludger = new Bludger();
        bludger.setPosition(instance.getPosition());
        instance.collisionEvent(bludger);
        assertEquals(false,instance.isStunned());
    }
    
    

    /**
     * Test of performAction method, of class Beater.
     */
    @Test
    public void testPerformAction() {
        boolean aPressed = false;
        boolean dPressed = false;
        instance.performAction(aPressed, dPressed);
        assertEquals(false,instance.isHitting());
        
        aPressed = true;
        dPressed = false;
        instance.performAction(aPressed, dPressed);
        assertEquals(true,instance.isHitting());
        
        aPressed = false;
        dPressed = true;
        instance.performAction(aPressed, dPressed);
        assertEquals(true,instance.isHitting());
        
        aPressed = true;
        dPressed = true;
        instance.performAction(aPressed, dPressed);
        assertEquals(true,instance.isHitting());
    }

    /**
     * Test of startHit method, of class Beater.
     */
    @Test
    public void testStartHit() {
          //Nothing to test
    }

    /**
     * Test of isHitting method, of class Beater.
     */
    @Test
    public void testIsHitting() {

       assertEquals(false, instance.isHitting());
       instance.startHit();
       assertEquals(true, instance.isHitting());
     }
}
