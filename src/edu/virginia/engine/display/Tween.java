package edu.virginia.engine.display;

import edu.virginia.engine.events.EventDispatcher;

import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class Tween extends EventDispatcher {

    DisplayObject object;

    ArrayList<TweenParam> params;

    public Tween(DisplayObject object) {
        this.object = object;
        params = new ArrayList<>();
    }

    Tween() {
    }

    public DisplayObject getObject() {
        return object;
    }

    public void animate(TweenableParams param, TweenTransitions.Functions tweenTransition, double endVal, double time) {
        TweenParam tweenParam = new TweenParam(param, tweenTransition, object.getTweenableParam(param), endVal, time);
        if (!params.contains(tweenParam)) {
            params.add(tweenParam);
        }
    }

    public void update() {
        params.stream()
        .filter(param -> !param.isComplete())
        .forEach(param -> param.update(object));
    }

    public boolean isComplete() {
        for (TweenParam param : params) {
            if (!param.isComplete()) return false;
        }
        return true;
    }
}
