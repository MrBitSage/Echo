package edu.virginia.engine.events;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jason on 7/20/2016.
 */
public class EventDispatcher implements IEventDispatcher {

    private HashMap<String, ArrayList<IEventListener>> listeners;

    public EventDispatcher() {
        this.listeners = new HashMap<>();
    }

    @Override
    public void addEventListener(IEventListener listener, String eventType) {
        if (!listeners.containsKey(eventType)) {
            listeners.put(eventType, new ArrayList<>());
        }
        listeners.get(eventType).add(listener);
    }

    @Override
    public boolean removeEventListener(IEventListener listener, String eventType) {
        if (listeners.containsKey(eventType)) {
            boolean success = listeners.get(eventType).remove(listener);
            if (listeners.get(eventType).size() == 0) {
                listeners.remove(eventType);
            }
            return success;
        }
        return false;
    }

    @Override
    public void dispatchEvent(Event event) {
        if (listeners.containsKey(event.getEventType())) {
            for (IEventListener listener : listeners.get(event.getEventType())) {
                listener.handleEvent(event);
            }
        }
    }

    @Override
    public boolean hasEventListener(IEventListener listener, String eventType) {
        if (listeners.containsKey(eventType)) {
            if (listeners.get(eventType).contains(listener)) {
                return true;
            }
        }
        return false;
    }
}
