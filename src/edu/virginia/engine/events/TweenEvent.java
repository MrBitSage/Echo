package edu.virginia.engine.events;

import edu.virginia.engine.display.Tween;

/**
 * Created by Jason on 7/24/2016.
 */
public class TweenEvent extends Event {

    public static final String TWEEN_STARTED = "Tween Start";
    public static final String TWEEN_COMPLETED = "Tween Complete";

    public TweenEvent(String eventType, Tween source) {
        super(eventType, source);
    }

    public Tween getTween() {
        return (Tween) super.getSource();
    }
}
