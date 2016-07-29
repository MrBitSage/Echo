package edu.virginia.game.test;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

/**
 * Created by Jason.
 */
public class EchoEvent extends Event {

    private int x;
    private int y;

    public EchoEvent(String eventType, IEventDispatcher source, int x, int y) {
        super(eventType, source);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
