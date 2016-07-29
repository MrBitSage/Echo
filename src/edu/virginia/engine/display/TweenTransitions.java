package edu.virginia.engine.display;

/**
 * Created by Jason on 7/24/2016.
 */
public class TweenTransitions {

    public enum Functions {
        CUBIC_I,
        CUBIC_O,
        CUBIC_I_O,
        SINE_I_O
    }

    public static double applyTransition(double percentDone, Functions easingFunction) {
        switch (easingFunction) {
            case CUBIC_I:
                return cubicInTransition(percentDone);
            case CUBIC_O:
                return cubicOutTransition(percentDone);
            case CUBIC_I_O:
                return cubicInOutTransition(percentDone);
            case SINE_I_O:
                return sineInOutTransition(percentDone);
        }
        return percentDone;
    }

    private static double cubicInTransition(double percentDone) {
        return Math.pow(percentDone, 3);
    }

    private static double cubicOutTransition(double percentDone) {
        return Math.pow(percentDone, ((double) 1) / ((double) 3));
    }

    private static double cubicInOutTransition(double percentDone) {
        return 2 * Math.pow(percentDone, 3) - 3 * Math.pow(percentDone, 2) + 2 * percentDone;
    }

    private static double sineInOutTransition(double percentDone) {
        return Math.sin(Math.PI * (percentDone - 0.5)) / 2 + 0.5;
    }
}
