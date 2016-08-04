package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.CollisionResolver;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.SoundManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class Beta extends Game implements IEventListener {

    private static final double GRAVITY = 50;

    int levelIndex = 0;
    boolean standing = false;
    boolean jumpReady = false;
    String direction = "right";

    PhysicsSprite player = new PhysicsSprite("Player", "blue", 12);

    BufferedImage bg = readImage("background.png");
    DisplayObject levelBlock = new DisplayObject("Floor", bg),
            levelBlock2 = new DisplayObject("Floor2", bg),
            levelBlock3 = new DisplayObject("Floor3", bg),
            borderLeft = new DisplayObject("Border Left", bg),
            borderRight = new DisplayObject("Border Right", bg),
            borderTop = new DisplayObject("Border Top", bg);

    GameClock clock = GameClock.getInstance();
    TweenJuggler juggler = TweenJuggler.getInstance();
    CollisionResolver resolver = new CollisionResolver();


    DisplayObjectContainer background = new DisplayObjectContainer("Background", bg),
            echoes = new DisplayObjectContainer("Echoes"),
            level = new DisplayObjectContainer("Level");

    SoundManager music = new SoundManager();
    SoundManager ping = new SoundManager();

    DisplayObject crystal1 = new DisplayObject("crystal1", "crystal_type1.png"),
            crystal2 = new DisplayObject("crystal2", "crystal_type2.png");

    DisplayObject entrance = new DisplayObject("Entrance", "door4now.png"),
            exit = new DisplayObject("Exit", "door4now.png");

    Echo echoA = new Echo("Echo A", 7, 300, 1000, 200, TweenTransitions.Functions.SINE_I_O),
            echoB = new Echo("Echo B", 7, 300, 1000, 200, TweenTransitions.Functions.SINE_I_O);

    AnimatedSprite beaconA = new AnimatedSprite("Beacon", "beacon", 10),
            beaconB = new AnimatedSprite("Beacon", "beacon", 10),
            fog = new AnimatedSprite("Fog", "fogs", 12),
            ash = new AnimatedSprite("Ash", "ash", 10.3),
            ash2 = new AnimatedSprite("Ash2", "ash", 12);


    public Beta(String gameId, int width, int height) throws FileNotFoundException {
        super(gameId, width, height);

        background.setScaleX(100);
        background.setScaleY(100);
        this.addChild(background);
        this.addChild(echoes);
        this.addChild(level);

        //alternate levels here(?)
        loadLevel(levelIndex);

        //player
        initPlayer();
        borderPatrol();

        player.registerCollidable(levelBlock3);
        player.registerCollidable(levelBlock2);
        player.registerCollidable(levelBlock);
        player.registerCollidable(crystal1);
        player.registerCollidable(crystal2);
        player.addEventListener(resolver, CollisionEvent.COLLISION_EVENT);
        this.addChild(player);

        fog.setScaleX(2);
        fog.setScaleY(2);
        fog.setAlpha((float) 0.8);
        this.addChild(fog);

        playGameMusic();

        this.addEventListener(echoA, Echo.ECHO_EVENT);
        this.addEventListener(echoB, Echo.ECHO_EVENT);

        resolver.addEventListener(this, "Level Complete");
        resolver.addEventListener(this, "Standing");
        resolver.addEventListener(this, "Player Died");

        echoA.addEventListener(this, Echo.ECHO_PING);
        echoB.addEventListener(this, Echo.ECHO_PING);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Beta game = new Beta("Beta Test", 800, 600);
        game.start();
    }

    @Override
    protected void update(ArrayList<String> pressedKeys) {
        if (clock != null && player != null && levelBlock != null && juggler != null) {
            clock.refresh();

            handlePlayer(pressedKeys);
            checkBeacons();
            animateMapElements();

            juggler.update();
            super.update(pressedKeys);
            clock.clockTic();
        }
    }

    //basics
    private void borderPatrol() {
        borderLeft.setScaleX(2);
        borderLeft.setScaleY(600);
        borderLeft.setPosition(new Point(-10, 0));
        this.addChild(borderLeft);
        borderRight.setScaleX(2);
        borderRight.setScaleY(600);
        borderRight.setPosition(new Point(790, 0));
        this.addChild(borderRight);
        borderTop.setScaleX(600);
        borderTop.setScaleY(2);
        borderTop.setPosition(new Point(0, -20));
        this.addChild(borderTop);
        player.registerCollidable(borderLeft);
        player.registerCollidable(borderTop);
        player.registerCollidable(borderRight);
    }

    private void initPlayer() {
        player.setScaleX(2);
        player.setScaleY(2);
        player.moveTo(entrance.getPosition().x + player.getUnscaledWidth() * player.getGlobalScaleX() / 2,
                entrance.getPosition().y - player.getUnscaledHeight() * player.getGlobalScaleY() / 4);
        player.setAlpha((float) .4);
        player.setxSpeedCap(300);
        player.setyAccel(GRAVITY);
        player.setySpeedCap(900);
    }

    private void handlePlayer(ArrayList<String> pressedKeys) {
        //player movement
        if ((pressedKeys.contains("A") ^ pressedKeys.contains("D")) ||
                pressedKeys.contains("←") ^ (pressedKeys.contains("→"))) {
            if (pressedKeys.contains("A") || pressedKeys.contains("←")) {
                player.setxAccel(-100);
                direction = "left";
                if (standing) player.animate("left");
                else if (direction.equals("right")) player.animate("defaultRight");
                else if (direction.equals("left")) player.animate("defaultLeft");
            }
            if (pressedKeys.contains("D") || (pressedKeys.contains("→"))) {
                player.setxAccel(100);
                direction = "right";
                if (standing) player.animate("right");
                else if (direction.equals("right")) player.animate("defaultRight");
                else if (direction.equals("left")) player.animate("defaultLeft");
            }
        } else {
            if (direction.equals("right")) player.animate("defaultRight");
            else if (direction.equals("left")) player.animate("defaultLeft");
            player.setxAccel(0);
            player.setxVel(0);
        }
        if (pressedKeys.contains("Space")
                || pressedKeys.contains("␣")
                || pressedKeys.contains("↑")
                || pressedKeys.contains("W")) {
            if (jumpReady) {
                standing = false;
                jumpReady = false;
                player.setyVel(-900);
            }
        }
        if (!pressedKeys.contains("Space")
                && !pressedKeys.contains("␣")
                && !pressedKeys.contains("↑")
                && !pressedKeys.contains("W")) {
            if (standing) {
                jumpReady = true;
            }
        }
    }

    private void checkBeacons() {
        if (player.getHitbox().intersects(beaconA.getHitbox())
                && echoA.echoReady()) {
            this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this,
                    (int) beaconA.getHitbox().getCenterX(),
                    (int) beaconA.getHitbox().getCenterY(), "Echo A"));
        }

        if (player.getHitbox().intersects(beaconB.getHitbox())
                && echoB.echoReady()) {
            this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this,
                    (int) beaconB.getHitbox().getCenterX(),
                    (int) beaconB.getHitbox().getCenterY(), "Echo B"));
        }
    }

    private void animateMapElements() {
        if (beaconA != null && beaconB != null) {
            beaconA.animate("main");
            beaconB.animate("main");
        }
        if (fog != null) {
            fog.animate("main");
        }

        if (ash != null && ash2 != null) {
            ash.animate("main");
            ash2.animate("main");
        }
    }

    private void playGameMusic() {
        try {
            music.loadMusic("main_loop", "echo_music_loop.wav");
            music.playMusic("main_loop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playEchoPing() {
        try {
            if (!ping.soundLoaded()) ping.loadSoundEffect("ping", "echo_ping.wav");
            ping.playSoundEffect("ping");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //levels
    private void loadLevel(int lvl) {
        switch (lvl) {
            case 0:
                intro();
                break;
            case 1:
                level1();
                break;
            case 2:
                level2();
                break;
            case 3:
                level3();
                break;
            case 4:
                level4();
            default:
                levelIndex = 0;
                intro();
                break;
        }
    }

    private void clearLevel() {
        level.clearChildren();
        echoes.clearChildren();
    }

    //animated
    private void intro() {
        levelBlock.moveTo(0, 400);
        levelBlock.setScaleX(1000 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock);

        entrance.moveTo(10, 400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        entrance.setAlpha((float) 0.4);
        level.addChild(entrance);

        exit.setAlpha((float) 0.4);
        exit.moveTo(800 - exit.getUnscaledWidth() * exit.getGlobalScaleX() - 20,
                400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        level.addChild(exit);

        crystal1.setScaleY(0);
        crystal2.setScaleY(0);
    }

    //go right
    private void level1() {
        levelBlock.moveTo(0, 400);
        levelBlock.setScaleX(1000 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock);

        entrance.setAlpha((float) 0.4);
        entrance.moveTo(10, 400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        level.addChild(entrance);

        exit.moveTo(800 - exit.getUnscaledWidth() * exit.getGlobalScaleX() - 20,
                400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        exit.setAlpha((float) 0.4);
        level.addChild(exit);

        crystal1.setScaleY(0);
        crystal2.setScaleY(0);
    }

    // + obstacle
    private void level2() {
        levelBlock.moveTo(0, 400);
        levelBlock.setScaleX(1000 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock);

        levelBlock2.moveTo(300, 300);
        levelBlock2.setScaleX(100 / levelBlock.getUnscaledWidth());
        levelBlock2.setScaleY(150 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock2);

        levelBlock3.moveTo(levelBlock2.getHitbox().x + 70, 250);
        levelBlock3.setScaleX(125 / levelBlock.getUnscaledWidth());
        levelBlock3.setScaleY(200 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock3);

        entrance.moveTo(10, 400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        entrance.setAlpha((float) 0.4);
        level.addChild(entrance);

        exit.setAlpha((float) 0.4);
        exit.moveTo(800 - exit.getUnscaledWidth() * exit.getGlobalScaleX() - 20,
                400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        level.addChild(exit);

        crystal1.setScaleY(0);
        crystal2.setScaleY(0);

        beaconA.moveTo(levelBlock2.getHitbox().x - 80,
                levelBlock.getHitbox().y - beaconA.getUnscaledHeight());
        level.addChild(beaconA);

        echoes.addChild(echoA);
    }

    // + falling sometimes not ok
    private void level3() {
        // level 0.3
        levelBlock.moveTo(0, 400);
        levelBlock.setScaleX(200 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
//        levelBlock.setAlpha((float).001);
        level.addChild(levelBlock);

        levelBlock2.moveTo(300, 400);
        levelBlock2.setScaleX(200 / levelBlock.getUnscaledWidth());
        levelBlock2.setScaleY(200 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock2);

        levelBlock3.moveTo(600, 400);
        levelBlock3.setScaleX(400 / levelBlock.getUnscaledWidth());
        levelBlock3.setScaleY(200 / levelBlock.getUnscaledHeight());
        level.addChild(levelBlock3);

        crystal1.setPosition(new Point(levelBlock.getHitbox().x + levelBlock.getHitbox().width, 500));
        crystal1.setAlpha((float) .1);
        crystal1.setScaleX(.55);
        crystal1.setScaleY(.6);
        level.addChild(crystal1);

        crystal2.setPosition(new Point(levelBlock2.getHitbox().x + levelBlock2.getHitbox().width, 500));
        crystal2.setAlpha((float) .1);
        crystal2.setScaleX(.55);
        crystal2.setScaleY(.6);
        level.addChild(crystal2);

        ash.setPosition(new Point(
                crystal1.getPosition().x,
                crystal1.getPosition().y - 70)
        );
        ash.setAlpha((float) .21);
        ash.setScaleX(.55);
        level.addChild(ash);

        ash2.setPosition(new Point(
                crystal2.getPosition().x,
                crystal2.getPosition().y - 70)
        );
        ash2.setAlpha((float) .21);
        ash2.setScaleX(.55);
        level.addChild(ash2);

        beaconA.moveTo(levelBlock.getHitbox().x + levelBlock.getHitbox().width - 60,
                levelBlock.getHitbox().y - beaconA.getUnscaledHeight());
        level.addChild(beaconA);

        beaconB.moveTo(levelBlock2.getHitbox().x + levelBlock2.getHitbox().width - 125,
                levelBlock2.getHitbox().y - beaconA.getUnscaledHeight());
        level.addChild(beaconB);

        entrance.moveTo(10, 400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        entrance.setAlpha((float) 0.4);
        level.addChild(entrance);

        exit.setAlpha((float) 0.4);
        exit.moveTo(800 - exit.getUnscaledWidth() * exit.getGlobalScaleX() - 20,
                400 - entrance.getUnscaledHeight() * entrance.getGlobalScaleY());
        level.addChild(exit);

        echoes.addChild(echoA);
        echoes.addChild(echoB);
    }

    // + falling is ok
    private void level4() {
    }

    // + single platform with pitfall
    private void level5() {
    }

    // + platforms no pitfall
    private void level6() {
    }

    // + platforms & pitfalls
    private void level7() {
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
//        g.setColor(new Color(255, 2, 104));
//        for (DisplayObject child : getChildren()) {
//            drawHitBox(g, child);
//        }
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals("Level Complete")) {
            clearLevel();
            loadLevel(++levelIndex);
            initPlayer();
        }
        if (event.getEventType().equals("Standing")) {
            standing = true;
        }
        if (event.getEventType().equals("Player Died")) {
            clearLevel();
            levelIndex = 0;
            loadLevel(levelIndex);
            initPlayer();
        }
        if (event.getEventType().equals(Echo.ECHO_PING)) {
            playEchoPing();
        }
    }
}
