package edu.virginia.game.test;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.CollisionResolver;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class CollisionTest extends Game {

    PhysicsSprite blue = new PhysicsSprite("Blue", "blue", 12);
    Sprite coin = new Sprite("Coin", "coin.png");

    GameClock clock = GameClock.getInstance();
    CollisionResolver collisionResolver = new CollisionResolver();

    public CollisionTest(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        blue.addEventListener(collisionResolver, CollisionEvent.COLLISION_EVENT);
        blue.move(50, 0);
        blue.setScaleX(5);
        blue.setScaleY(5);
        blue.setxSpeedCap(500);
        blue.setySpeedCap(100);
        blue.setyAccel(100);
        blue.registerCollidable(coin);
        this.addChild(blue);

        coin.move(0, 400);
        coin.setScaleX(6);
        coin.setScaleY(0.5);
        this.addChild(coin);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        if (clock != null && blue != null && coin != null) {
            clock.refresh();
            if (pressedKeys.contains("A") ^ pressedKeys.contains("D")) {
                if (pressedKeys.contains("D")) {
                    blue.setxAccel(50);
                }
                if (pressedKeys.contains("A")) {
                    blue.setxAccel(-50);
                }
            } else {
                blue.setxAccel(0);
                blue.setxVel(0);
            }

            super.update(pressedKeys);
            clock.clockTic();
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        for (DisplayObject child : getChildren()) {
            drawHitBox(g, child);
        }
        if (blue != null && blue.collides(coin)) {
            System.out.println("What?");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        CollisionTest game = new CollisionTest("Collision Test", 800, 600);
        game.start();
    }
}
