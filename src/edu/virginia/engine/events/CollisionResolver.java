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
            Rectangle playerOld = sprite.getHitbox();
            playerOld.translate(sprite.getPreviousPosition().x - sprite.getPosition().x, sprite.getPreviousPosition().y - sprite.getPosition().y);

            Rectangle object = collidable.getHitbox();

            Rectangle intersection = player.intersection(object);

            double deltaX = 0, deltaY = 0;
            if (collidedFromTop(playerOld, player, object)) {
                System.out.println("Top");
                deltaY = -(intersection.getHeight());
            }
            if (collidedFromBottom(playerOld, player, object)) {
                System.out.println("Bottom");
                deltaY = intersection.getHeight();
            }
            if (collidedFromLeft(playerOld, player, object)) {
                System.out.println("Left");
                deltaX = -(intersection.getWidth());
            }
            if (collidedFromRight(playerOld, player, object)) {
                System.out.println("Right");
                deltaX = intersection.getWidth();
            }
            sprite.move(deltaX, deltaY);
        }
    }

    private boolean collidedFromLeft(Rectangle old, Rectangle current, Rectangle object) {
        return old.getX() + old.getWidth() <= object.getX() &&
                current.getX() + current.getWidth() >= object.getX();
    }

    private boolean collidedFromRight(Rectangle old, Rectangle current, Rectangle object) {
        return old.getX() >= object.getX() + object.getWidth() &&
                current.getX() <= object.getX() + object.getWidth();
    }

    private boolean collidedFromTop(Rectangle old, Rectangle current, Rectangle object) {
        return old.getY() + old.getHeight() <= object.getY() &&
                current.getY() + current.getHeight() >= object.getY();
    }

    private boolean collidedFromBottom(Rectangle old, Rectangle current, Rectangle object) {
        return old.getY() >= object.getY() + object.getHeight() &&
                current.getY() <= object.getY() + object.getHeight();
    }
}
