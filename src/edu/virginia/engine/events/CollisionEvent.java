package edu.virginia.engine.events;

import edu.virginia.engine.display.DisplayObject;

/**
 * Created by Jason.
 */
public class CollisionEvent extends Event {

    public static final String COLLISION_EVENT = "Collision Event";

    DisplayObject collidedObject;

    public CollisionEvent(String eventType, DisplayObject source, DisplayObject collidedObject) {
        super(eventType, source);
        this.collidedObject = collidedObject;
    }

    public DisplayObject getCollidedObject() {
        return collidedObject;
    }
}
