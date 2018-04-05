package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Animated defines an AnimatedObject sprite to draw behind the seeker
 * when they are in boost mode.
 *
 * @author Sam Leichter
 */
public abstract class AnimatedObject extends DrawnObject {

    private int framesPerTick;
    private int curTick = 1;
    private int maxFrames;
    private int curFrame = 1;
    private String name;

    /**
     * Creates an AnimatedObject
     *
     * @param name the image's name
     * @param maxFrames How many frames are in the animation.
     * @param framesPerTick Speed to animate the sprite
     * @param Location the Location to draw the image.

     */
    public AnimatedObject(String name, int maxFrames, int framesPerTick, Point2D.Double Location) {
        super(name + "1", Location);
        this.name = name;
        this.maxFrames = maxFrames;
        this.framesPerTick = framesPerTick;
    }

    /**
     * Changes the frame of the animation.
     */
    public void nextFrame() {

        curTick = ++curTick;
        if (curTick == framesPerTick) {
            curTick = 1;
            curFrame = (curFrame == maxFrames) ? 1 : ++curFrame;
        }
        setImage(name + curFrame);
    }
}
