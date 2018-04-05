package quidditchgame.objects;

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * KeeperBoundingBox Class is used to contain each keeper to a certain bounding
 * box based on thier original position.
 *
 * @author Chris Chester
 */
public class KeeperBoundingBox extends CollidableObject {
    
    /**
     * Paints the keeperbox
     *
     * @param position the position to draw the keeper bounding box at
     */
     public KeeperBoundingBox(Point2D.Double position) {
        super("keeperBox" ,position);
    }
    /**
     * Paints the keeperbox
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    
    /**
    * Determines the result of the keeperBoundBox colliding with anything.
    *
    * @param collidableObject
    */
    @Override
    public void collisionEvent(CollidableObject collidableObject) {
        //it doesn't move.
    }
}
