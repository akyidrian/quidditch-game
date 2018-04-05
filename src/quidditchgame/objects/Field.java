package quidditchgame.objects;

import java.awt.geom.Point2D;

public class Field extends DrawnObject {

    public static final int FIELD_WIDTH = 1000;
    public static final int FIELD_HEIGHT = 600;
    /**
     * Allocate the Field a sprite image location as well as starting
     * position
     *
     * @param imageLocation
     * @param position
     */
    public Field(Point2D.Double position) {
        super("field_bigger", position);
    }
}