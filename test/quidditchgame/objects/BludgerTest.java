/*
 *  Junit test for the bludger.java class
 *  Author: Chris Chester
 *  Created: 10/07/12
 */
package quidditchgame.objects;

import org.junit.*;
import static org.junit.Assert.*;
import quidditchgame.Team;

/**
 *
 * @author Chris Chester
 */
public class BludgerTest {
    Bludger instance = null;
    
    public BludgerTest() {
    }
    
    @Before
    public void setUp() {
        instance = new Bludger();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of doTick method, of class Bludger.
     */
    @Test
    public void testDoTick() {

    }

    /**
     * Test of hit method, of class Bludger.
     */
    @Test
    public void testHit() {
    }

    /**
     * Test of getLastTeamThatHit method, of class Bludger.
     */
    @Test
    public void testGetLastTeamThatHit() {
    }

    /**
     * Test of hitFlagTimeout method, of class Bludger.
     */
    @Test
    public void testHitFlagTimeout() {
    }

    /**
     * Test of isHitByBeater method, of class Bludger.
     */
    @Test
    public void testIsHitByBeater() {
    }

    /**
     * Test of collisionEvent method, of class Bludger.
     */
    @Test
    public void testCollisionEvent_Beater() {
    }

    /**
     * Test of collisionEvent method, of class Bludger.
     */
    @Test
    public void testCollisionEvent_GoalHoop() {
        System.out.println("collisionEvent");
    }

    /**
     * Test of collisionEvent method, of class Bludger.
     */
    @Test
    public void testCollisionEvent_Player() {
        Team team = new Team("team",Team.color.red,Team.side.LEFT);
        Player player = new Player(team ,"Seeker",instance.getPosition());
        instance.collisionEvent(player);
        assertEquals(false,instance.isHitByBeater());

    }
}
