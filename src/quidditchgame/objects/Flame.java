package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Flame defines the Flame sprite to draw behind the seeker
 * when they are in boost mode.
 *
 * @author Sam Leichter
 */
public class Flame extends AnimatedObject {

    public static final int MAX_FRAMES = 3;
    public static final int FRAME_PER_TICK = 3;
    /**
     * Allocate the Flame a sprite image
     */
    public Flame() {
        super("Flame", MAX_FRAMES, FRAME_PER_TICK, new Point2D.Double(0,0));
    }
}
