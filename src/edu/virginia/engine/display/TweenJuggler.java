package edu.virginia.engine.display;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Jason on 7/24/2016.
 */
public class TweenJuggler {
    private static TweenJuggler ourInstance = new TweenJuggler();

    public static TweenJuggler getInstance() {
        return ourInstance;
    }

    private ArrayList<Tween> tweens;

    private TweenJuggler() {
        tweens = new ArrayList<>();
    }

    public void add(Tween tween) {
        tweens.add(tween);
    }

    public void update() {
        tweens.forEach(Tween::update);
        tweens = tweens.stream()
                .filter(tween -> !tween.isComplete())
                .collect(Collectors.toCollection(ArrayList<Tween>::new));
    }
}
