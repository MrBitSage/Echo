package edu.virginia.engine.display;

import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.Ticker;

import java.awt.*;

/**
 * Created by Jason on 7/24/2016.
 */
public class TweenParam {

    private TweenableParams paramToTween;
    private TweenTransitions.Functions tweenTransition;
    private double startVal;
    private double endVal;
    private double time;
    private long startTime;
    private Ticker ticker;

    public TweenParam(TweenableParams paramToTween, TweenTransitions.Functions tweenTransition, double startVal, double endVal, double time) {
        this.paramToTween = paramToTween;
        this.tweenTransition = tweenTransition;
        this.startVal = startVal;
        this.endVal = endVal;
        this.time = time;
        this.startTime = -1;
    }

    public void update(DisplayObject object) {
        if (ticker == null) ticker = GameClock.getInstance().getTicker(this);

        double percentTimeElapsed = ticker.getElapsedTime()/time;

        double percentDone = TweenTransitions.applyTransition(percentTimeElapsed, tweenTransition);

        double deltaVal = endVal - startVal;

        if (percentTimeElapsed < 1) {
            deltaVal *= percentDone;
        }

        switch (paramToTween) {
            case X:
                object.setPosition(new Point((int) (startVal + deltaVal), object.getPosition().y));
                break;
            case Y:
                object.setPosition(new Point(object.getPosition().x, (int) (startVal + deltaVal)));
                break;
            case SCALE_X:
                object.setScaleX(startVal + deltaVal);
                break;
            case SCALE_Y:
                object.setScaleY(startVal + deltaVal);
                break;
            case ROTATION:
                object.setRotation(startVal + deltaVal);
                break;
            case ALPHA:
                object.setAlpha((float) (startVal + deltaVal));
                break;
            default:
                break;
        }
    }

    public boolean isComplete() {
        if (ticker != null) return ticker.getElapsedTime() > time;
        return false;
    }

    public TweenableParams getParamToTween() {
        return paramToTween;
    }

    public double getStartVal() {
        return startVal;
    }

    public double getEndVal() {
        return endVal;
    }

    public double getTime() {
        return time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TweenParam) {
            if (((TweenParam) obj).getParamToTween() == this.getParamToTween()) {
                return true;
            }
        }
        return false;
    }
}
