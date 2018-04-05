package quidditchgame.objects;

import java.awt.geom.Point2D;

/**
 * Letter defines the Letters's characteristics.
 *
 * @author Sam Leichter
 */
public class Letter extends DrawnObject {

    /**
     * Allocate the Letter a sprite image location as well as starting
     * position
     *
     * @param letter the letter to draw
     * @param position the letter's location
     */
    public Letter(String letter, Point2D.Double position) {
        super("letters/" + letter, position);
    }
}
