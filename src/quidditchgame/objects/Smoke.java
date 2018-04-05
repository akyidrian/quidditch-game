package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Flame defines the Smoke sprite to draw behind the seeker
 * after they have boosted.
 *
 * @author Jimmy + Sam
 */
public class Smoke extends AnimatedObject {

    public static final int MAX_FRAMES = 6;
    public static final int FRAME_PER_TICK = 4;

    /**
     * Allocate the Smoke a sprite image
     */
    public Smoke() {
        super("Smoke", MAX_FRAMES, FRAME_PER_TICK, new Point2D.Double(0,0));
    }
}
