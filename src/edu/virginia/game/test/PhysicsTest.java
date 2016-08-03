package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.CollisionResolver;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class PhysicsTest extends Game {

    public static final double GRAVITY = 100;

    DisplayObjectContainer background;
    DisplayObjectContainer echoes;
    DisplayObjectContainer level;

    BufferedImage bg = readImage("background.png");
    DisplayObject levelBlockA;
    DisplayObject levelBlockB;

    PhysicsSprite blue = new PhysicsSprite("Sprite", "blue", 12);

    TweenJuggler juggler = TweenJuggler.getInstance();
    GameClock clock = GameClock.getInstance();
    CollisionResolver collisionResolver = new CollisionResolver();

    private boolean jumpReady = true;

    Echo echoA;
    Echo echoB;
    AnimatedSprite beaconA;
    AnimatedSprite beaconB;

    AnimatedSprite fog;

    public PhysicsTest(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        background = new DisplayObjectContainer("Background");
        background.setScaleX(100);
        background.setScaleY(100);
        echoes = new DisplayObjectContainer("Echoes");
        level = new DisplayObjectContainer("Level");

        this.addChild(background);
        this.addChild(echoes);
        this.addChild(level);

        levelBlockA = new DisplayObject("Level Block A", bg);
        levelBlockA.setScaleX(400 / levelBlockA.getUnscaledWidth());
        levelBlockA.setScaleY(400 / levelBlockA.getUnscaledHeight());
        levelBlockA.move(0, 600);

        levelBlockB = new DisplayObject("Level Block B", bg);
        levelBlockB.setScaleX(900 / levelBlockA.getUnscaledWidth());
        levelBlockB.setScaleY(300 / levelBlockA.getUnscaledHeight());
        levelBlockB.move(400, 700);

        level.addChild(levelBlockA);
        level.addChild(levelBlockB);

        blue.setScaleX(5);
        blue.setScaleY(5);
        blue.move(blue.getUnscaledWidth() * blue.getScaleX() / 2 + 20, 200);
        blue.getPivotPoint().translate(blue.getUnscaledWidth() / 2, blue.getUnscaledHeight());
        blue.setAlpha((float) 0.6);
        blue.setxSpeedCap(500);
        blue.setyAccel(GRAVITY);
        blue.setySpeedCap(5000);
        blue.registerCollidable(levelBlockA);
        blue.addEventListener(collisionResolver, CollisionEvent.COLLISION_EVENT);
        this.addChild(blue);

        echoA = new Echo("Echo A", 5, 600, 850, 200, TweenTransitions.Functions.SINE_I_O);
        this.addEventListener(echoA, Echo.ECHO_EVENT);
        echoes.addChild(echoA);

        echoB = new Echo("Echo B", 5, 600, 850, 200, TweenTransitions.Functions.SINE_I_O);
        this.addEventListener(echoB, Echo.ECHO_EVENT);
        echoes.addChild(echoB);

        beaconA = new AnimatedSprite("Beacon", "beacon", 10);
        beaconA.getPosition().translate(200, 600);
        beaconA.getPivotPoint().translate(beaconA.getUnscaledWidth() / 2, beaconA.getUnscaledHeight());
        level.addChild(beaconA);

        beaconB = new AnimatedSprite("Beacon", "beacon", 10);
        beaconB.getPosition().translate(650, 700);
        beaconB.getPivotPoint().translate(beaconA.getUnscaledWidth() / 2, beaconA.getUnscaledHeight());
        level.addChild(beaconB);

        fog = new AnimatedSprite("Fog", "fogs", 12);
        fog.setScaleX(2);
        fog.setScaleY(2);
        fog.setAlpha((float) 0.6);
        this.addChild(fog);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        if (clock != null) {
            clock.refresh();
            super.update(pressedKeys);
            if (blue != null) {
                if (pressedKeys.contains("A") ^ pressedKeys.contains("D")) {
                    if (pressedKeys.contains("A")) {
                        blue.animate("left");
                        blue.setxAccel(-200);
                    }
                    if (pressedKeys.contains("D")) {
                        blue.animate("right");
                        blue.setxAccel(200);
                    }
                } else {
                    blue.animate("default");
                    blue.setxAccel(0);
                    blue.setxVel(0);
                }
                if (pressedKeys.contains("Space") && jumpReady) {
                    jumpReady = false;
                    blue.setyVel(-600);
                }
                if (Math.abs(blue.getPosition().getX() - 200) < 20) {
                    this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this, 200, 550, "Echo A"));
                }
                if (Math.abs(blue.getPosition().getX() - 650) < 20) {
                    this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this, 650, 650, "Echo B"));
                }
            }
            if (juggler != null) {
                juggler.update();
            }
            if (beaconA != null && beaconB != null) {
                beaconA.animate("main");
                beaconB.animate("main");
            }
            if (fog != null) {
                fog.animate("main");
            }
            clock.clockTic();
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(new Color(255, 255, 255));
        for (DisplayObject child : getChildren()) {
            drawHitBox(g, child);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        PhysicsTest game = new PhysicsTest("Test", 1200, 980);
        game.start();
    }
}
