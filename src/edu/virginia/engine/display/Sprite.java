package edu.virginia.engine.display;

import java.awt.image.BufferedImage;

/**
 * Nothing in this class (yet) because there is nothing specific to a Sprite yet that a DisplayObject
 * doesn't already do. Leaving it here for convenience later. you will see!
 */
public class Sprite extends DisplayObjectContainer {

    public Sprite(String id) {
        super(id);
    }

    public Sprite(String id, String imageFileName) {
        super(id, imageFileName);
    }

    public Sprite(String id, BufferedImage displayImage) {
        super(id, displayImage);
    }
}