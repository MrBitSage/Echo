package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.util.GameClock;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class EchoDemo extends Game {

    DisplayObjectContainer background;
    DisplayObjectContainer echoes;
    DisplayObjectContainer level;
    AnimatedSprite sprite;

    GameClock tic;

    TweenJuggler juggler = TweenJuggler.getInstance();

    Echo testEcho;

    public EchoDemo(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        background = new DisplayObjectContainer("BackGround", "background.png");
        echoes = new DisplayObjectContainer("Echoes");
        level = new DisplayObjectContainer("Level");

        this.addChild(background);
        this.addChild(echoes);
        this.addChild(level);

        sprite = new AnimatedSprite("Sprite Demo", "blueman", 12);
        sprite.setScaleX(5);
        sprite.setScaleY(5);
        sprite.getPosition().translate(0, 200);
        this.addChild(sprite);

        testEcho = new Echo("Echo", 10, 200, 1500, 300, TweenTransitions.Functions.SINE_I_O);
        this.addEventListener(testEcho, Echo.ECHO_EVENT);
        echoes.addChild(testEcho);

        tic = new GameClock();
        tic.resetGameClock();
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
        if (sprite != null && tic != null) {
            double dT = tic.getElapsedTime() / 1000;
            double dist = 500 * dT;
            if (pressedKeys.contains("A") ^ pressedKeys.contains("D")) {
                if (pressedKeys.contains("A")) {
                    sprite.animate("left");
                    sprite.getPosition().translate((int) -dist, 0);
                }
                if (pressedKeys.contains("D")) {
                    sprite.animate("right");
                    sprite.getPosition().translate((int) dist, 0);
                }
            } else {
                sprite.animate("default");
            }
            if (Math.abs(sprite.getPosition().getX() - 400) < 20 && testEcho.echoReady()) {
                this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this, 500, 300));
            }
            tic.resetGameClock();
        }
        if (juggler != null) {
            juggler.update();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        EchoDemo game = new EchoDemo("Echo Demo", 800, 600);
        game.start();
    }
}
