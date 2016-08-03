package edu.virginia.engine.util;

/**
 * Created by Jason.
 */
public class Ticker {
    private long startTime;
    private double elapsedTime;

    public Ticker() {
        this.startTime = GameClock.getInstance().getLastTic();
        elapsedTime = GameClock.getInstance().getDeltaT();
    }

    public void incrementTicker() {
        this.elapsedTime += GameClock.getInstance().getDeltaT();
    }

    public void resetTicker() {
        this.startTime = GameClock.getInstance().getLastTic();
        this.elapsedTime = GameClock.getInstance().getDeltaT();
    }

    public double getElapsedTime() {
        return elapsedTime;
    }
}
