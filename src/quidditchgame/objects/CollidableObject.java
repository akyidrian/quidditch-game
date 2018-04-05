package quidditchgame.objects;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * 
 * This is the abstract class for all collidable objects.
 * Collision dectection checking is done by CollisionDetector.
 *
 * All possible collisions are defined within this class.
 *
 * This class defines a collisionEvent to use in a subclass.
 * It also defines collisionEvents in subclasses with different arguments
 * in order to define collisions with specific classes.
 *
 * @author Alex Drinkwater
 */
public abstract class CollidableObject extends DrawnObject {
    
    // Constants for the generation of collision polygons
    private static final int ALPHASHIFT = 24;
    private static final int FULLYTRANPARENT = 0;
    
    // The accurate collision detection polygon
    protected Polygon pixelOutline = new Polygon();
    
    // Static outline buffer so that collision outlines are not calculated more than once
    private static HashMap<String, Polygon> bufferedPixelOutlines = new HashMap<String, Polygon>();

    /**
     * Creates a collidable object, passing color, object name, and position up
     * to drawnObject class.
     *
     * @param object
     * @param position
     */
    CollidableObject(String object, Point2D.Double position) {
        super(object, position);
        loadCollisionPolygon(object);
    }

    /**
     * This function returns true if 'other' intersects with this object.
     * First a simple bounding box test is used, then a pixel-perfect bit-mask is used
     * to test for collision.
     *
     * @param other the object to test for a collision with
     * @return true if there is a collision, false otherwise
     */
    public boolean intersects(CollidableObject other) {

        if (getRectangularBounds().intersects(other.getRectangularBounds())) {
            //the bounding boxes intersect, now test the image bit-masks for collision
            return bitmasksIntersect(other);

        } else {
            //bounding boxes do not intersect. No collision
            return false;
        }
    }
    

    /**
     * This function returns true if the two polygons for an object intersect it
     * should only be called if the bounding rectangles for the two objects
     * intersect.
     *
     * @param other the object to test for a collision with
     * @return true if there is a collision, false otherwise
     *
     */
    private boolean bitmasksIntersect(CollidableObject other) {

        Area areaThis = new Area(this.getAccurateCollisionBounds());
        Area areaOther = new Area(other.getAccurateCollisionBounds());

        //Changes the area of areaThis to the interesting area of areaThis and areaOther
        areaThis.intersect(areaOther);

        if (!areaThis.isEmpty()) {
            return true;

        } else {
            //Pixel masks do not intersect. No collision
            return false;
        }
    }
    
    /**
     * With the main collision event between moving objects handled in 
     * MovableObject, this event rarely should get triggered, except my an
     * object that is not movable, and will react by doing nothing anyway.
     * @param other 
     */
    public void collisionEvent(CollidableObject other) {
        /* Do Nothing */
    }
    
    /**
     * Except for the special case of the seeker, snitch collisions should do nothing.
     * @param snitch 
     */
    public void collisionEvent(Snitch snitch) {
    }
    
    /**
     * Except for the special case of the keeper, keeperArea collisions should
     * do nothing.
     *
     * @param keeperArea
     */
    public void collisionEvent(KeeperBoundingBox keeperArea) {
    }

    /**
     * @return the pixel based bounds of the object, used for precise collision detection
     */
    public Polygon getAccurateCollisionBounds() {
        
        //rotate the polygon so it is inline with the image
        AffineTransform at = AffineTransform.getRotateInstance(getRotation(),
                pixelOutline.getBounds().getCenterX(), pixelOutline.getBounds().getCenterY());
        
        PathIterator i = pixelOutline.getPathIterator(at);

        //get the pixel polygon
        Polygon accurateCollisionPolygon = getPolygonFromPathIterator(i);
        
        //translate so the pixel polygon is centered about the middle of the image
        accurateCollisionPolygon.translate((int) getPosition().x - getSpriteWidth() / 2,
                (int) getPosition().y - getSpriteHeight() / 2);

        return accurateCollisionPolygon;
    }
    
    /**
     * Sets the image of the object to be displayed through paint.
     * Also automatically loads a new collision polygon for the new image.
     *
     * @param object The path of object to be displayed
     */
    @Override
    public void setImage(String object) {
        super.setImage(object);
        loadCollisionPolygon(object);
    }
    
    /**
     * Creates or loads the collision polygon outline for the object.
     * Attempts to load from a buffer first, otherwise generates one.
     *
     * @param object The object to be displayed
     */
    private void loadCollisionPolygon(String imageName) {
        if (bufferedPixelOutlines.containsKey(imageName)) {
            pixelOutline = bufferedPixelOutlines.get(imageName); //load from buffer
        } else {
            pixelOutline = calculatePixelOutline(getSprite()); //calculate from sprite
            
            //save the loaded polygon in the polygons buffer.
            bufferedPixelOutlines.put(imageName, pixelOutline);
        }
    }
    
    /**
     * calculates the pixel outline of an object, used during collision detection
     *
     * @param image the image of the object
     * @return Polygon the pixal outline of the object image inputted
     */
    private Polygon calculatePixelOutline(BufferedImage image) {
        Polygon outline = new Polygon();
        
        boolean inObject = false;

        //Left to Right finding top pixels
        for (int i = 0; i < image.getWidth(); i++) {
            //Top to bottom
            for (int j = 0; j < image.getHeight(); j++) {


                if (!inObject) {
                    //Checks if pixel is tranparent to set top of image
                    if ((image.getRGB(i, j) >> ALPHASHIFT) != FULLYTRANPARENT) {
                        outline.addPoint(i, j);
                        inObject = true;
                    }
                }
            }
            inObject = false;
        }

        //Right to Left finding botton pixels
        for (int i = image.getWidth() - 1; i >= 0; i--) {
            //Bottom to top
            for (int j = image.getHeight() - 1; j >= 0; j--) {


                if (!inObject) {
                    //Checks if pixel is tranparent to set bottom of image
                    if ((image.getRGB(i, j) >> ALPHASHIFT) != FULLYTRANPARENT) {
                        outline.addPoint(i, j);
                        inObject = true;
                    }
                }
            }
            inObject = false;
        }
        
        return outline;
    }
    
    /**
     * Allows for the outline to be set externally
     * @param polygon 
     */
    public void setPixelOutline(Polygon polygon){
        pixelOutline = polygon;
    }
    
    

}
