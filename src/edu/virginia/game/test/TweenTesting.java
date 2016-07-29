package edu.virginia.game.test;

import edu.virginia.engine.display.*;

import java.util.ArrayList;

/**
 * Created by Jason on 7/26/2016.
 */
public class TweenTesting extends Game {

    Sprite mario = new Sprite("Mario", "Mario.png");
    TweenJuggler juggler = TweenJuggler.getInstance();

    boolean firstOccurence = true;

    public TweenTesting(String gameId, int width, int height) {
        super(gameId, width, height);

        mario.getPosition().translate(500, 500);
        mario.getPivotPoint().translate(mario.getUnscaledWidth() / 2, mario.getUnscaledHeight() / 2);
        this.addChild(mario);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);

        if (pressedKeys.contains("T") && firstOccurence && juggler != null) {
            TweenChain t = new TweenChain(mario);
            t.animate(TweenableParams.SCALE_X, TweenTransitions.Functions.SINE_I_O, 2, 500);
            t.animate(TweenableParams.SCALE_Y, TweenTransitions.Functions.SINE_I_O, 2, 500);
            t.addLink(2);
            t.animate(TweenableParams.X, TweenTransitions.Functions.SINE_I_O, 200, 1000);
            t.animate(TweenableParams.ALPHA, TweenTransitions.Functions.CUBIC_O, 0, 750);
            juggler.add(t);
            firstOccurence = false;
        }
        if (!pressedKeys.contains("T") && !firstOccurence) {
            firstOccurence = true;
        }
        if (juggler != null) {
            juggler.update();
        }
    }

    public static void main(String[] args) {
        TweenTesting game = new TweenTesting("Tween Testing", 1000, 1000);
        game.start();
    }
}
