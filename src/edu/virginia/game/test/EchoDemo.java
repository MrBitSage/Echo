package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.GameClock;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class EchoDemo extends Game implements IEventListener{

    DisplayObjectContainer background;
    DisplayObjectContainer echoes;
    DisplayObjectContainer level;

    AnimatedSprite sprite;

    TweenJuggler juggler = TweenJuggler.getInstance();

    Echo testEcho;

    GameClock clock = GameClock.getInstance();

    public EchoDemo(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        background = new DisplayObjectContainer("BackGround", "background.png");
        echoes = new DisplayObjectContainer("Echoes");
        level = new DisplayObjectContainer("Level");

        this.addChild(background);
        this.addChild(echoes);
        this.addChild(level);

        sprite = new AnimatedSprite("Sprite Demo", "blue", 12);
        sprite.setScaleX(5);
        sprite.setScaleY(5);
        sprite.getPosition().translate(0, 200);
        sprite.addEventListener(this, CollisionEvent.COLLISION_EVENT);
        this.addChild(sprite);

        testEcho = new Echo("Echo", 3, 250, 6000, 1000, TweenTransitions.Functions.MUSTAFA);
        this.addEventListener(testEcho, Echo.ECHO_EVENT);
        echoes.addChild(testEcho);
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        clock.refresh();
        if (sprite != null) {
            double dT = GameClock.getInstance().getDeltaT() / 1000;
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
                this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this, 500, 300, "Echo"));
            }
        }
        if (juggler != null) {
            juggler.update();
        }
        super.update(pressedKeys);
        GameClock.getInstance().clockTic();
    }

    public static void main(String[] args) throws FileNotFoundException {
        EchoDemo game = new EchoDemo("Echo Demo", 800, 600);
        game.start();
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().endsWith(CollisionEvent.COLLISION_EVENT)) {
            CollisionEvent ce = (CollisionEvent) event;
            System.out.println(ce.getCollidedObject().getId());
        }
    }
}
