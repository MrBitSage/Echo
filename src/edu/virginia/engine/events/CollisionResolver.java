package edu.virginia.engine.events;

import edu.virginia.engine.display.DisplayObject;

import java.awt.*;

/**
 * Created by Jason.
 */
public class CollisionResolver implements IEventListener {

    public CollisionResolver() {
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals(CollisionEvent.COLLISION_EVENT)) {
            CollisionEvent ev = (CollisionEvent) event;
            DisplayObject sprite = (DisplayObject) ev.getSource();
            DisplayObject collidable = ev.getCollidedObject();
            Rectangle player = sprite.getHitbox();
            Rectangle intersection = player.intersection(collidable.getHitbox());

            double deltaX = 0, deltaY = 0;

            int belly = sprite.getHitbox().x+sprite.getHitbox().width;
            int feet = sprite.getHitbox().y+sprite.getHitbox().height;
            int back = sprite.getHitbox().x;
            int head = sprite.getHitbox().y;

            boolean collideBottom = feet >= collidable.getHitbox().y &&
                    head < collidable.getHitbox().y;

            boolean collideTop = head <= collidable.getHitbox().y+collidable.getHitbox().height &&
                    feet > collidable.getHitbox().y+collidable.getHitbox().height;

            boolean collideLeft = back <= collidable.getHitbox().x+collidable.getHitbox().width &&
                    belly > collidable.getHitbox().x+collidable.getHitbox().width &&
                    head > collidable.getHitbox().y;

            boolean collideRight = belly >= collidable.getHitbox().x &&
                    back < collidable.getHitbox().x;

            if ( collideBottom )
                deltaY = -intersection.height;
            else if ( collideTop )
                deltaY = intersection.height;
            else if ( collideLeft  )
                deltaX = intersection.width;
            else if ( collideRight )
                deltaX = -intersection.width;

            sprite.move(deltaX,deltaY);

        }
    }
}
