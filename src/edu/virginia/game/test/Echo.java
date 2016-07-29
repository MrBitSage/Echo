package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class Echo extends DisplayObjectContainer {

    public static final String FILE_NAME = "echo_default-1.png";

    int numRings;
    double radius;
    double duration;
    double spawnDelay;
    TweenTransitions.Functions easeFunction;

    ArrayList<Sprite> rings;
    BufferedImage ringImage;

    GameClock timer;
    boolean firstRun;

    public Echo(String id, int numRings, double radius, double duration, double spawnDelay, TweenTransitions.Functions easeFunction) {
        super(id);
        this.numRings = numRings;
        this.radius = radius;
        this.duration = duration;
        this.spawnDelay = spawnDelay;
        this.easeFunction = easeFunction;

        rings = new ArrayList<>();
        ringImage = readImage(FILE_NAME);

        for (int i = 0; i < numRings; i++) {
            Sprite ring = new Sprite("Ring-" + i, ringImage);
            ring.setPivotPoint(new Point(ring.getUnscaledWidth()/2, ring.getUnscaledHeight()/2));
            ring.setAlpha(0);
            this.addChild(ring);
            rings.add(ring);
        }

        timer = new GameClock();
        firstRun = true;

        timer.resetGameClock();
    }

    public void echo(int x, int y) {
        firstRun = false;
        for (Sprite ring : rings) {
            ring.setPosition(new Point(x, y));
            ring.setScaleX(0.01);
            ring.setScaleY(0.01);
            ring.setAlpha(1);
            TweenChain animation = new TweenChain(ring);
            animation.animate(TweenableParams.NA, TweenTransitions.Functions.SINE_I_O, 1, spawnDelay * rings.indexOf(ring));
            animation.addLink(1);
            animation.animate(TweenableParams.SCALE_X, TweenTransitions.Functions.SINE_I_O, 2 * radius / ring.getUnscaledWidth(), duration);
            animation.animate(TweenableParams.SCALE_Y, TweenTransitions.Functions.SINE_I_O, 2 * radius / ring.getUnscaledHeight(), duration);
            animation.animate(TweenableParams.ALPHA, TweenTransitions.Functions.SINE_I_O, 0, duration);
            TweenJuggler.getInstance().add(animation);
        }
        timer.resetGameClock();
    }

    public boolean echoReady() {
        return firstRun || (timer != null && timer.getElapsedTime() > (numRings - 1) * spawnDelay + duration);
    }

}
