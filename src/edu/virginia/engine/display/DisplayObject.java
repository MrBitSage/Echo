package edu.virginia.engine.display;

import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.util.GameClock;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A very basic display object for a java based gaming engine
 */
public class DisplayObject extends EventDispatcher {

    //Parent
    private DisplayObject parent;

    /* All DisplayObject have a unique id */
    private String id;

    /* The image that is displayed by this object */
    private BufferedImage displayImage;

    private boolean visible;
    private boolean visibilityToggled;
    private Point position;
    private Point pivotPoint;
    private double scaleX;
    private double scaleY;
    private double rotation;
    private float alpha;

    private GameClock deltaT = new GameClock();

    /**
     * Constructors: can pass in the id OR the id and image's file path and
     * position OR the id and a buffered image and position
     */
    public DisplayObject(String id) {
        super();
        this.setId(id);
        init();
    }

    public DisplayObject(String id, String fileName) {
        super();
        this.setId(id);
        this.setImage(fileName);
        init();
    }

    public DisplayObject(String id, BufferedImage displayImage) {
        this.id = id;
        this.displayImage = displayImage;
        init();
    }

    private void init() {
        parent = null;
        visible = true;
        position = new Point(0, 0);
        pivotPoint = new Point(0, 0);
        scaleX = 1;
        scaleY = 1;
        rotation = 0;
        alpha = 1;
    }

    public DisplayObject getParent() {
        return parent;
    }

    public void setParent(DisplayObject parent) {
        this.parent = parent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    /**
     * Returns the unscaled width and height of this display object
     */
    public int getUnscaledWidth() {
        if (displayImage == null) return 0;
        return displayImage.getWidth();
    }

    public int getUnscaledHeight() {
        if (displayImage == null) return 0;
        return displayImage.getHeight();
    }

    public BufferedImage getDisplayImage() {
        return this.displayImage;
    }

    protected void setImage(String imageName) {
        if (imageName == null) {
            return;
        }
        displayImage = readImage(imageName);
        if (displayImage == null) {
            System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
        }
    }


    /**
     * Helper function that simply reads an image from the given image name
     * (looks in resources\\) and returns the bufferedimage for that filename
     */
    public BufferedImage readImage(String imageName) {
        BufferedImage image = null;
        try {
            String file = ("resources" + File.separator + imageName);
            image = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
            e.printStackTrace();
        }
        return image;
    }

    public void setImage(BufferedImage image) {
        if (image == null) return;
        displayImage = image;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisibilityToggled() {
        return visibilityToggled;
    }

    public void setVisibilityToggled(boolean visibilityToggled) {
        this.visibilityToggled = visibilityToggled;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPivotPoint() {
        return pivotPoint;
    }

    public void setPivotPoint(Point pivotPoint) {
        this.pivotPoint = pivotPoint;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getAlphaTransform() {
        if (parent != null) {
            return getParent().getAlphaTransform() * getAlpha();
        } else {
            return getAlpha();
        }
    }

    public float getAlphaReverseTransform() {
        if (parent != null) {
            if (getAlpha() != 0) {
                return getParent().getAlphaReverseTransform() / getAlpha();
            } else {
                return getParent().getAlphaReverseTransform();
            }
        } else {
            return 1;
        }
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public GameClock getDeltaT() {
        return deltaT;
    }

    public void setDeltaT(GameClock deltaT) {
        this.deltaT = deltaT;
    }

    /**
     * Invoked on every frame before drawing. Used to update this display
     * objects state before the draw occurs. Should be overridden if necessary
     * to update objects appropriately.
     */
    protected void update(ArrayList<String> pressedKeys) {
        double dT = deltaT.getElapsedTime() / 1000;
        // Put General DisplayObject Update Code Here
        deltaT.resetGameClock();
    }

    /**
     * Draws this image. This should be overloaded if a display object should
     * draw to the screen differently. This method is automatically invoked on
     * every frame.
     */
    public void draw(Graphics g) {
        if (displayImage != null && isVisible()) {
            // Get the graphics and apply this objects transformations (rotation, etc.)
            Graphics2D g2d = (Graphics2D) g;
            Rectangle r = getHitbox();
            g2d.drawRect(r.x, r.y, r.width, r.height);
            applyTransformations(g2d);

            // Draw
            g2d.drawImage(displayImage, 0, 0, (int) (getUnscaledWidth()), (int) (getUnscaledHeight()), null);

            // Undo the transformations so this doesn't affect other display objects
            reverseTransformations(g2d);
        }
    }

    /**
     * Applies transformations for this display object to the given graphics
     * object
     */
    protected void applyTransformations(Graphics2D g2d) {
        //Translation
        g2d.translate(getPosition().getX(), getPosition().getY());

        //Scale
        g2d.scale(getScaleX(), getScaleY());

        //Rotation
        g2d.rotate(getRotation());

        //Pivot Translation
        g2d.translate(-getPivotPoint().getX(), -getPivotPoint().getY());

        //Set Transparency
        AlphaComposite alcom = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, getAlphaTransform());
        g2d.setComposite(alcom);
    }

    /**
     * Reverses transformations for this display object to the given graphics
     * object
     */
    protected void reverseTransformations(Graphics2D g2d) {
        //Set Transparency
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 / getAlphaReverseTransform());
        g2d.setComposite(alcom);

        //Pivot Translation
        g2d.translate(getPivotPoint().getX(), getPivotPoint().getY());

        //Rotation
        g2d.rotate(-getRotation());

        //Scale
        g2d.scale(1 / getScaleX(), 1 / getScaleY());

        //Translation
        g2d.translate(-getPosition().getX(), -getPosition().getY());
    }

    public Point localToGlobal(Point l) {
        if (getParent() == null) {
            return l;
        } else {
            return new Point(getParent().localToGlobal(getPosition()).x + l.x,
                    getParent().localToGlobal(getPosition()).y + l.y);
        }
    }

    public Point globalToLocal(Point g) {
        if (parent == null) {
            return g;
        } else {
            return new Point(g.x - getPosition().x, g.y - getPosition().y);
        }
    }

    public Rectangle getHitbox() {
        int x = (int) (-pivotPoint.x * scaleX);
        int y = (int) (-pivotPoint.y * scaleY);
        Point origin = localToGlobal(new Point(x, y));
        return new Rectangle(origin.x, origin.y, (int) (getUnscaledWidth() * scaleX), (int) (getUnscaledHeight() * scaleY));
    }

    public boolean collidesWith(DisplayObject object) {
        if (this.getHitbox().intersects(object.getHitbox())) {
            this.dispatchEvent(new CollisionEvent(CollisionEvent.COLLISION_EVENT, this, object));
            return true;
        }
        return false;
    }

    public Point rotate(Point p, double theta) {
        double x = Math.cos(theta - Math.atan(p.getY() / p.getX())) *
                Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
        double y = Math.sin(theta - Math.atan(p.getY() / p.getX())) *
                Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
        return new Point((int) x, (int) y);
    }

    public double getTweenableParam(TweenableParams param) {
        switch (param) {
            case X:
                return getPosition().getX();
            case Y:
                return getPosition().getY();
            case SCALE_X:
                return getScaleX();
            case SCALE_Y:
                return getScaleY();
            case ROTATION:
                return getRotation();
            case ALPHA:
                return getAlpha();
        }
        return 0;
    }
}