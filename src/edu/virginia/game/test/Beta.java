package edu.virginia.game.test;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.TweenJuggler;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.CollisionResolver;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class Beta extends Game {

    private static final double GRAVITY = 100;

    PhysicsSprite player = new PhysicsSprite("Player", "blue", 12);
    DisplayObject levelBlock = new DisplayObject("Floor", "background.png");
    DisplayObject levelBlock2 = new DisplayObject("Floor2", "background.png");

    GameClock clock = GameClock.getInstance();
    TweenJuggler juggler = TweenJuggler.getInstance();

    CollisionResolver resolver = new CollisionResolver();

    public Beta(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        player.setScaleX(2);
        player.setScaleY(2);
        player.move(50, 20);
        player.setxSpeedCap(500);
        player.setyAccel(GRAVITY);
        player.setySpeedCap(900);
        player.registerCollidable(levelBlock2);
        player.registerCollidable(levelBlock);
        player.addEventListener(resolver, CollisionEvent.COLLISION_EVENT);

        levelBlock.move(0, 100);
        levelBlock.setScaleX(400 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(300 / levelBlock.getUnscaledHeight());

        levelBlock2.move(400, 350);
        levelBlock2.setScaleX(400 / levelBlock.getUnscaledWidth());
        levelBlock2.setScaleY(250 / levelBlock.getUnscaledHeight());

        this.addChild(levelBlock);
        this.addChild(levelBlock2);
        this.addChild(player);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        if (clock != null && player != null && levelBlock != null && juggler != null) {
            clock.refresh();
            if (pressedKeys.contains("A") ^ pressedKeys.contains("D")) {
                if (pressedKeys.contains("A")) {
                    player.animate("left");
                    player.setxAccel(-200);
                }
                if (pressedKeys.contains("D")) {
                    player.animate("right");
                    player.setxAccel(200);
                }
            } else {
                player.animate("default");
                player.setxAccel(0);
                player.setxVel(0);
            }
            if (pressedKeys.contains("Space")) {
                player.setyVel(-600);
            }
            juggler.update();
            super.update(pressedKeys);
            clock.clockTic();
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(new Color(255,0,0));
        for (DisplayObject child : getChildren()) {
            drawHitBox(g, child);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Beta game = new Beta("Beta Test", 800, 600);
        game.start();
    }
}
