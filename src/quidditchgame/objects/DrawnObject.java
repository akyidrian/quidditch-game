package quidditchgame.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import quidditchgame.CompassDirection;
import quidditchgame.Team;

/**
 * Defines a general object that is drawn to the screen, has an associated
 * sprite image, position, and rotation.
 *
 * @author  Matthew Wigley, Chris Chester, Campbell Letts
 */
public abstract class DrawnObject extends JComponent {

    private static final double IMAGE_SCALE_FACTOR = 1; //how much to scale the sprites by
    private static final String imagePath = "./assets/";
    
    //the current sprite for the object.
    private BufferedImage sprite;

    private Point2D.Double position = new Point2D.Double();
    private double rotation = 0; // [rad] North = zero degrees.

    //static image buffer so that images are not loaded more than once.
    private static HashMap<String, BufferedImage> bufferedImages = new HashMap<String, BufferedImage>();

    /**
     * Creates an object that is able to be drawn to the screen, with the
     * ability to select a sprite.
     *
     * @param object The object in string form to be concatenated to form the
     * file name.
     * @param position Position to draw the image - the center coordinate
     */
    DrawnObject(String object, Point2D.Double position) {
        this.position = position;
        setImageInternal(object);
    }

    /**
     * Return the location of the sprite. 
     *
     * @return Position defined as the center of the object.
     */
    public Point2D.Double getPosition() {
        return position;
    }

    /**
     * Sets the position of the object without checks.
     * @param pos 
     */
    protected void setPosition(Point2D.Double pos) {
        position = pos;
    }

    /**
     * Returns the current rotation of the sprite (and therefore object).
     *
     * @return rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the object to be the set value immediately.
     * Rotation is zero to 2*PI radians, with 0rad=north.
     * @param rotation 
     */
    public void setRotationInstantaneous(double rotation) {
        this.rotation = clipRadians(rotation);
    }
    
    /**
     * limit an angle in radians to values between 0 and 2*pi by shifting it by
     * multiples of 2*pi
     * @param radians unnormalized radians
     */
    public double clipRadians(double radians) {
        // Keeps rotation between 0 and 2 PI.
        while (radians < 0) {
            radians += 2 * Math.PI;
        }
        while (radians >= 2 * Math.PI) {
            radians -= 2 * Math.PI;
        }
        return radians;
    }
    
    
    /**
     * Sets the rotation of the object immediately to one of the main 8 points
     * of the compass. Overloading of above.
     * @param direction 
     */
    public void setRotationInstantaneous(CompassDirection direction) {
        setRotationInstantaneous(direction.toRadians());
    }

    /**
     * Transforms (translation and rotation) the sprite, then draws to screen.
     *
     * @param g Graphics object.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform affineTransform = new AffineTransform();
        //translate the image to the correct coordinates
        affineTransform.translate((int) position.x - getSpriteWidth() / 2,
                (int) position.y - getSpriteHeight() / 2);
        //rotate the image to face the correct direction
        affineTransform.rotate(rotation, getSpriteWidth() / 2, getSpriteHeight() / 2);

        //draw the rotated and translated image
        g2d.drawImage(sprite, affineTransform, this);
    }
    
    /**
     * Returns the width of the bufferedImage sprite
     * @return 
     */
    public int getSpriteWidth() {
        return sprite.getWidth(this);
    }
    
    /**
     * Returns the Height of the bufferedImage sprite
     * @return 
     */
    public int getSpriteHeight() {
        return sprite.getHeight(this);
    }
    
    /**
     * Returns the BufferedImage Sprite
     * @return 
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * Returns the bounds of the sprite in polygon format. NOTE: Code used from
     * http://stackoverflow.com/questions/4145609/rotate-rectangle-in-java
     *
     * @return bounds in polygon format
     */
    public Rectangle getRectangularBounds() {
        //first get the unrotated rectangle
        Rectangle r = new Rectangle((int) getPosition().x - getSpriteWidth() / 2,
                (int) getPosition().y - getSpriteHeight() / 2,
                getSpriteWidth(), getSpriteHeight());

        //rotate it
        AffineTransform at = AffineTransform.getRotateInstance(getRotation(),r.getCenterX(), r.getCenterY());

        //now convert the rotated object back into a rectangle
        PathIterator i = r.getPathIterator(at);
        Polygon boundingRectangle = getPolygonFromPathIterator(i);
        
        return boundingRectangle.getBounds();
    }
    
    /**
     * given a path iterator this returns the polygon closed by it.
     * @param i the path iterator to get the polygon for
     * @return the polygon closed by i
     */
    protected Polygon getPolygonFromPathIterator(PathIterator i) {
        Polygon p = new Polygon();
        
        //iterate the path iterator adding each point to the polygon
        while (!i.isDone()) {
            double[] xy = new double[2];
            i.currentSegment(xy);
            if (xy[0] != 0 & xy[1] != 0) {
                p.addPoint((int) xy[0], (int) xy[1]);
            }
            i.next();
        }
        
        return p;
    }
    
    /**
     * Sets the image of the object to be displayed through paint. Also creates
     * a polygon outline of that image.
     *
     * @param color The color of the object
     * @param object The object to be displayed
     */
    public void setImage(Team team, String object) {
        setImage(getFullImagePath(team, object));
    }
    
    /**
     * Get the file path for the image given that the player is on a certain team
     */
    protected String getFullImagePath(Team team, String object) {
        String colorString;
        if (team != null) {
            colorString = team.getColor().toString() + object;
        } else {
            colorString = object;
        }
        return colorString;
    }
    
    /**
     * scales an image preserving transparency.
     * @param image the image to scale
     * @param scaleFactor the scaling factor
     * @return the scaled image
     */
    private BufferedImage scaleImage(BufferedImage image, double scaleFactor) {
        int newWidth = (int) (image.getWidth() * scaleFactor) ;
        int newHeight = (int) (image.getHeight() * scaleFactor) ;
        
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = imageBuff.createGraphics();
        g.drawImage(scaledImage, 0, 0, new Color(0,0,0,0), null);
        g.dispose();
        
        return imageBuff;
    }
    
    /**
     * Sets the image of the object to be displayed when painting it.
     *
     * @param object The path of object to be displayed
     */
    public void setImage(String object) {
        setImageInternal(object);
    }

    /**
     * Sets the image of the object to be displayed through paint.
     * Transparently loads the image from a buffer from a file if the image is not yet buffered.
     * 
     * Internal private function so that it can be called by the constructor without muckiness.
     * non-internal callers should use setImage which may be overloaded to do more (e.g. load a collision polygon)
     *
     * @param object The object to be displayed
     */
    private void setImageInternal(String object) {
        String imageLocation = imagePath + object;

        //try to use the static image and outline buffer first, if not load the image from file
        //this way images will only be loaded once and outlines only calculated once
        if (bufferedImages.containsKey(imageLocation)) {
            sprite = bufferedImages.get(imageLocation);
        } else {
            sprite = loadSpriteFromFile(imageLocation);
            
            //save the loaded image in the image buffer.
            bufferedImages.put(imageLocation, sprite);
        }

        this.setSize(getSpriteWidth(), getSpriteHeight());
    }
    
    /**
     * load a png or gif image from file.
     * Defaults to loading an error sprite on failure
     * if it cannot load the error sprite the function prints an error message and exits the program.
     * @param imageLocation the file location of the image minus the file extension.
     */
    private BufferedImage loadSpriteFromFile(String imageLocation) {
        try {
            File file = new File(imageLocation + ".png");
            if (!file.exists()) {
                file = new File(imageLocation + ".gif");
            }

            sprite = ImageIO.read(file);

            //scale the image if scaling turned on.
            if (IMAGE_SCALE_FACTOR != 1) {
                if (!imageLocation.contains("field") && !imageLocation.contains("hoop")) {
                    sprite = scaleImage(sprite, IMAGE_SCALE_FACTOR);
                }
            }
            
            return sprite;

        } catch (IOException e) {
            //failed to load the image, attempt to load the error sprite instead
            System.out.println("image for object not found: " + imageLocation);
            System.out.println("perhaps you have not used the correct setImage()");
            System.out.println(e);
            try {
                
                File file = new File(imagePath + "error.png");
                sprite = ImageIO.read(file);
                return sprite;
                
            } catch (IOException ee) {
                System.out.println("Oh boy, we can't find the error sprite.");
                System.out.println(ee);
                System.exit(-1);
                return null;
            }
        }
    }
}
