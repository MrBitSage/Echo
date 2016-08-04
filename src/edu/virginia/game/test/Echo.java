package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.Ticker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class Echo extends DisplayObjectContainer implements IEventListener {

    public static final String ECHO_EVENT = "Echo";
    public static final String ECHO_PING = "Echo Ping";
    private static final String FILE_NAME = "echo_default-1.png";

    int numRings;
    double radius;
    double duration;
    double spawnDelay;
    TweenTransitions.Functions easeFunction;

    ArrayList<Sprite> rings;
    BufferedImage ringImage;

    boolean firstRun;
    Ticker ticker;

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
            ring.setPivotPoint(new Point(ring.getUnscaledWidth() / 2, ring.getUnscaledHeight() / 2));
            ring.setAlpha(0);
            this.addChild(ring);
            rings.add(ring);
        }

        firstRun = true;
        ticker = GameClock.getInstance().getTicker(this);
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
            animation.animate(TweenableParams.SCALE_X, easeFunction, 2 * radius / ring.getUnscaledWidth(), duration);
            animation.animate(TweenableParams.SCALE_Y, easeFunction, 2 * radius / ring.getUnscaledHeight(), duration);
            animation.animate(TweenableParams.ALPHA, easeFunction, 0, duration);
            TweenJuggler.getInstance().add(animation);
        }
    }

    public boolean echoReady() {
        return firstRun || (ticker.getElapsedTime() > (numRings - 1) * spawnDelay + duration);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals(ECHO_EVENT) && this.echoReady()) {
            EchoEvent ev = (EchoEvent) event;
            if (ev.getEcho().equals(this.getId())) {
                this.echo(ev.getX(), ev.getY());
                this.dispatchEvent(new Event(ECHO_PING, this));
                ticker.resetTicker();
            }
        }
    }
}
