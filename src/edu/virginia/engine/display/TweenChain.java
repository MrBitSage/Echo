package edu.virginia.engine.display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by Jason.
 */
public class TweenChain extends Tween {

    TreeMap<Integer, ArrayList<TweenParam>> paramsMap;
    Iterator it;

    public TweenChain(DisplayObject object) {
        this.object = object;
        params = null;
        paramsMap = new TreeMap<>();
        it = null;
    }

    public void addLink(int index) {
        if (!paramsMap.containsKey(index)) {
            paramsMap.put(index, new ArrayList<>());
            params = paramsMap.get(index);
        }
    }

    public ArrayList<TweenParam> removeLink(int index) {
        if (paramsMap.containsKey(index)) {
            return paramsMap.remove(index);
        }
        return null;
    }

    public boolean alterLink(int index) {
        if (paramsMap.containsKey(index)) {
            params = paramsMap.get(index);
            return true;
        }
        return false;
    }

    @Override
    public void animate(TweenableParams param, TweenTransitions.Functions tweenTransition, double endVal, double time) {
        if (it == null) {
            if (params == null) {
                paramsMap.put(0, new ArrayList<>());
                params = paramsMap.get(0);
            }
            super.animate(param, tweenTransition, endVal, time);
        }
    }

    @Override
    public void update() {
        if (it == null) {
            it = paramsMap.keySet().iterator();
            params = (ArrayList<TweenParam>) paramsMap.get(it.next());
        } else if (super.isComplete() && it.hasNext()) {
            params = (ArrayList<TweenParam>) paramsMap.get(it.next());
        }
        super.update();
    }

    @Override
    public boolean isComplete() {
        return !it.hasNext() && super.isComplete();
    }
}
