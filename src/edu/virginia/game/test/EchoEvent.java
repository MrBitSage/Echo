package edu.virginia.game.test;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

/**
 * Created by Jason.
 */
public class EchoEvent extends Event {

    private int x;
    private int y;
    private String echo;

    public EchoEvent(String eventType, IEventDispatcher source, int x, int y, String echo) {
        super(eventType, source);
        this.x = x;
        this.y = y;
        this.echo = echo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getEcho() {
        return echo;
    }
}
