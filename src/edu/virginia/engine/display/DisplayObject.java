package edu.virginia.engine.display;

import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.EventDispatcher;

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
    private Point previousPosition;
    private Point position;
    private Point pivotPoint;
    private double scaleX;
    private double scaleY;
    private double rotation;
    private float alpha;

    protected ArrayList<DisplayObject> collidables;

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
        previousPosition = new Point(0, 0);
        position = new Point(0, 0);
        pivotPoint = new Point(0, 0);
        scaleX = 1.0;
        scaleY = 1.0;
        rotation = 0;
        alpha = 1;
        collidables = new ArrayList<>();
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
        this.previousPosition = new Point(this.position);
        this.position = position;
    }

    public void move(double x, double y) {
        this.setPosition(new Point((int) (position.x + x), (int) (position.y + y)));
    }

    public void move(int x, int y) {
        this.setPosition(new Point(position.x + x, position.y + y));
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

    public double getGlobalScaleX() {
        if (getParent() == null) {
            return this.scaleX;
        } else {
            return getParent().getGlobalScaleX() * this.scaleX;
        }
    }

    public double getGlobalScaleY() {
        if (getParent() == null) {
            return this.scaleY;
        } else {
            return getParent().getGlobalScaleY() * this.scaleY;
        }
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

    public Point getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Invoked on every frame before drawing. Used to update this display
     * objects state before the draw occurs. Should be overridden if necessary
     * to update objects appropriately.
     */
    protected void update(ArrayList<String> pressedKeys) {
        checkCollisions();
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
            applyTransformations(g2d);

            // Draw
            g2d.drawImage(displayImage, 0, 0, getUnscaledWidth(), getUnscaledHeight(), null);

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
        AlphaComposite alphaComposite = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, getAlphaTransform());
        g2d.setComposite(alphaComposite);
    }

    /**
     * Reverses transformations for this display object to the given graphics
     * object
     */
    protected void reverseTransformations(Graphics2D g2d) {
        //Set Transparency
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 / getAlphaReverseTransform());
        g2d.setComposite(alphaComposite);

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
            return new Point(getParent().localToGlobal(getPosition()).x + (int) (getGlobalScaleX() * l.x),
                    getParent().localToGlobal(getPosition()).y + (int) (getGlobalScaleY() * l.y));
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
        int x = (int) (-pivotPoint.x * getGlobalScaleX());
        int y = (int) (-pivotPoint.y * getGlobalScaleY());
        Point origin = localToGlobal(new Point(x, y));
        return new Rectangle(origin.x, origin.y, (int) (getUnscaledWidth() * getGlobalScaleX()), (int) (getUnscaledHeight() * getGlobalScaleY()));
    }

    public boolean checkCollisions() {
        boolean collided = false;
        for (DisplayObject collidable : collidables) {
            if (collides(collidable)) {
                this.dispatchEvent(new CollisionEvent(CollisionEvent.COLLISION_EVENT, (PhysicsSprite) this, collidable));
                collided = true;
            }
        }
        return collided;
    }

    public boolean collides(DisplayObject collidable) {
        if (collidable instanceof DisplayObjectContainer) {
            for (DisplayObject child : ((DisplayObjectContainer) collidable).getChildren()) {
                if (this.collides(child)) {
                    return true;
                }
            }
        }
        return this.getHitbox().intersects(collidable.getHitbox());
    }

    public void registerCollidable(DisplayObject collidable) {
        if (!collidables.contains(collidable)) {
            collidables.add(collidable);
        }
    }

    public boolean removeCollidable(DisplayObject collidable) {
        return collidables.remove(collidable);
    }

    double getTweenableParam(TweenableParams param) {
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
            case NA:
                break;
        }
        return 0;
    }
}