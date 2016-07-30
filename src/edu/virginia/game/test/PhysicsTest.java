package edu.virginia.game.test;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class PhysicsTest extends Game {

    public static final double GRAVITY = 100;

    PhysicsSprite sprite = new PhysicsSprite("Sprite", "blue", 12);

    private boolean jumpReady = true;

    public PhysicsTest(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        sprite.setScaleX(5);
        sprite.setScaleY(5);
        sprite.getPosition().translate(0, 200);
        sprite.setxSpeedCap(500);
        sprite.setyAccel(GRAVITY);
        sprite.setySpeedCap(5000);
        this.addChild(sprite);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
        if (sprite != null) {
            if (pressedKeys.contains("A") ^ pressedKeys.contains("D")) {
                if (pressedKeys.contains("A")) {
                    sprite.animate("left");
                    sprite.setxAccel(-200);
                }
                if (pressedKeys.contains("D")) {
                    sprite.animate("right");
                    sprite.setxAccel(200);
                }
            } else {
                sprite.animate("default");
                sprite.setxVel(0);
            }
            if (sprite.getPosition().x < 200) {
                if (sprite.getPosition().y > 600) {
                    sprite.setPosition(new Point(sprite.getPosition().x, 600));
                    if (!pressedKeys.contains("Space") && !jumpReady) {
                        jumpReady = true;
                    }
                }
            }
            if (sprite.getPosition().x >= 200 && sprite.getPosition().x < 600) {
                if (sprite.getPosition().y > 700) {
                    sprite.setPosition(new Point(sprite.getPosition().x, 700));
                }
            }
            if (pressedKeys.contains("Space") && jumpReady) {
                jumpReady = false;
                sprite.setyVel(-1600);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        PhysicsTest game = new PhysicsTest("Test", 1200, 980);
        game.start();
    }
}
