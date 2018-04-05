package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * StunBirds defines the StunBirds for drawing around player's heads.
 *
 * @author Sam Leichter
 */
public class StunBirds extends AnimatedObject {

    public static final int MAX_FRAMES = 3;
    public static final int FRAME_PER_TICK = 3;
    /**
     * Allocate the Smoke a sprite image
     *
     */
    public StunBirds() {
        super("stunBirds", MAX_FRAMES, FRAME_PER_TICK, new Point2D.Double(0,0));
    }
}
