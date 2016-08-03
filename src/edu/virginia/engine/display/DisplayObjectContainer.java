package edu.virginia.engine.display;

import edu.virginia.engine.events.CollisionEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Jason on 7/15/2016.
 */
public class DisplayObjectContainer extends DisplayObject {

    private ArrayList<DisplayObject> children;

    public DisplayObjectContainer(String id) {
        super(id);
        children = new ArrayList<>();
    }

    public DisplayObjectContainer(String id, String fileName) {
        super(id, fileName);
        children = new ArrayList<>();
    }

    public DisplayObjectContainer(String id, BufferedImage displayImage) {
        super(id, displayImage);
        children = new ArrayList<>();
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        for (DisplayObject child : children) {
            child.update(pressedKeys);
        }
        super.update(pressedKeys);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        if (isVisible()) {
            applyTransformations((Graphics2D) g);
            for (DisplayObject child : children) {
                child.draw(g);
            }
            reverseTransformations((Graphics2D) g);
        }
    }

    public ArrayList<DisplayObject> getChildren() {
        return children;
    }

    public DisplayObject getChild(String id) {
        for (DisplayObject child : children) {
            if (child.getId().equals(id)) {
                return child;
            }
        }
        return null;
    }

    public void addChild(DisplayObject child) {
        if (!contains(child)) {
            child.setParent(this);
            children.add(child);
        }
    }

    public void addChild(int index, DisplayObject child) {
        if (!contains(child)) {
            child.setParent(this);
            children.add(index, child);
        }
    }

    public void addChildren(ArrayList<DisplayObject> children) {
        this.children.addAll(children);
    }

    public boolean removeChild(DisplayObject child) {
        child.setParent(null);
        return children.remove(child);
    }

    public DisplayObject removerChild(String id) {
        int index = -1;
        for (DisplayObject child : children) {
            if (child.getId().equalsIgnoreCase(id)) {
                index = children.indexOf(child);
            }
        }
        if (index == -1) {
            return null;
        } else {
            children.get(index).setParent(null);
            return children.get(index);
        }
    }

    public DisplayObject removeChild(int index) {
        children.get(index).setParent(null);
        return children.remove(index);
    }

    public void clearChildren() {
        children.forEach(child -> child.setParent(null));
        children.clear();
    }

    public boolean contains(DisplayObject child) {
        return children.contains(child);
    }

    @Override
    public boolean checkCollisions() {
        boolean collided = false;
        if (!super.checkCollisions()) {
            for (DisplayObject child : children) {
                for (DisplayObject collidable : collidables) {
                    if (child.collides(collidable)) {
                        this.dispatchEvent(new CollisionEvent(CollisionEvent.COLLISION_EVENT, (PhysicsSprite) this, collidable));
                        collided = true;
                    }
                }
            }
            return collided;
        }
        return true;
    }
}
