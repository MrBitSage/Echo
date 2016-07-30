package edu.virginia.game.test;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class CollisionTest extends Game implements IEventListener {

    Sprite mario = new Sprite("Mario", "Mario.png");
    Sprite coin = new Sprite("Coin", "coin.png");

    public CollisionTest(String gameId, int width, int height) {
        super(gameId, width, height);

        mario.addEventListener(this, CollisionEvent.COLLISION_EVENT);
        this.addChild(mario);

        coin.getPosition().translate(300, 50);
        coin.setScaleX(0.5);
        coin.setScaleY(0.5);
        this.addChild(coin);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);

        if (mario != null) {
            if (pressedKeys.contains("D")) {
                mario.getPosition().translate(5, 0);
            }
            if (pressedKeys.contains("A")) {
                mario.getPosition().translate(-5, 0);
            }
            mario.collidesWith(coin);
        }
    }

    public static void main(String[] args) {
        CollisionTest game = new CollisionTest("Collision Test", 800, 600);
        game.start();
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals(CollisionEvent.COLLISION_EVENT)) {
            CollisionEvent ev = (CollisionEvent) event;
            System.out.println(ev.getCollidedObject().getId());
        }
    }
}
