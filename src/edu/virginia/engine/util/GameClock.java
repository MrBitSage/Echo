package edu.virginia.engine.util;

import java.util.HashMap;

public class GameClock {
    private static final GameClock INSTANCE = new GameClock();

    public static GameClock getInstance() {
        return INSTANCE;
    }

    private long startTime;
    private long lastTic;
    private double deltaT;
    private HashMap<Object, Ticker> tickers;

    private GameClock() {
        startTime = System.nanoTime();
        lastTic = startTime;
        deltaT = 0;
        tickers = new HashMap<>();
    }

    public void refresh() {
        deltaT = (System.nanoTime() - lastTic) / 1000000.0;
        tickers.values().forEach(Ticker::incrementTicker);
    }

    public void clockTic() {
        lastTic = System.nanoTime();
        deltaT = 0;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getClockRuntime() {
        return (lastTic - startTime) / 1000000.0;
    }

    public long getLastTic() {
        return lastTic;
    }

    private void addNewTicker(Object o) {
        if (!tickers.containsKey(o)) {
            tickers.put(o, new Ticker());
        }
    }

    public Ticker getTicker(Object o) {
        if (!tickers.containsKey(o)) {
            addNewTicker(o);
        }
        return tickers.get(o);
    }
}