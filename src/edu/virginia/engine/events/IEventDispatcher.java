package edu.virginia.engine.events;

/**
 * Created by Jason on 7/20/2016.
 */
public interface IEventDispatcher {

    public void addEventListener(IEventListener listener, String eventType);

    public boolean removeEventListener(IEventListener listener, String eventType);

    public void dispatchEvent(Event event);

    public boolean hasEventListener(IEventListener listener, String eventType);
}
