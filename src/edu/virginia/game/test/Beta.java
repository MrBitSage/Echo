package edu.virginia.game.test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.CollisionEvent;
import edu.virginia.engine.events.CollisionResolver;
import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.SoundManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class Beta extends Game {

    private static final double GRAVITY = 100;

    PhysicsSprite player = new PhysicsSprite("Player", "blue", 12);

    DisplayObject levelBlock = new DisplayObject("Floor", "background.png"),
                  levelBlock2 = new DisplayObject("Floor2", "background.png"),
                  levelBlock3 = new DisplayObject("Floor3", "background.png"),
            borderLeft = new DisplayObject("Border Left", "background.png"),
            borderRight = new DisplayObject("Border Right", "background.png"),
            borderTop = new DisplayObject("Border Top", "background.png");

    GameClock clock = GameClock.getInstance();
    TweenJuggler juggler = TweenJuggler.getInstance();
    CollisionResolver resolver = new CollisionResolver();

    BufferedImage bg = readImage("background.png");
    DisplayObjectContainer background = new DisplayObjectContainer("Background", bg),
                           echoes = new DisplayObjectContainer("Echoes"),
                           level = new DisplayObjectContainer("Level");

    SoundManager soundguybill = new SoundManager();

    DisplayObject crystal1  = new DisplayObject("crystal1","crystal_type1.png"),
                  crystal2  = new DisplayObject("crystal2","crystal_type2.png");

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
        loadLevel(3);

        //player
        initPlayer();
        borderPatrol();

        player.registerCollidable(levelBlock3);
        player.registerCollidable(levelBlock2);
        player.registerCollidable(levelBlock);
        player.addEventListener(resolver, CollisionEvent.COLLISION_EVENT);
        this.addChild(player);

        fog.setScaleX(2);
        fog.setScaleY(2);
        fog.setAlpha((float)0.8);
        this.addChild(fog);

        playGameMusic();
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
    private void borderPatrol(){
        borderLeft.setScaleX(2);
        borderLeft.setScaleY(600);
        borderLeft.setPosition(new Point(-10,0));
        this.addChild(borderLeft);
        borderRight.setScaleX(2);
        borderRight.setScaleY(600);
        borderRight.setPosition(new Point(790,0));
        this.addChild(borderRight);
        borderTop.setScaleX(600);
        borderTop.setScaleY(2);
        borderTop.setPosition(new Point(0,-20));
        this.addChild(borderTop);
        player.registerCollidable(borderLeft);
        player.registerCollidable(borderTop);
        player.registerCollidable(borderRight);
    }
    private void initPlayer() {
        player.setScaleX(2);
        player.setScaleY(2);
        player.move(50, 20);
        player.setAlpha((float).4);
        player.setxSpeedCap(500);
        player.setyAccel(GRAVITY);
        player.setySpeedCap(900);
    }
    private void handlePlayer(ArrayList<String> pressedKeys){
        //player movement
        if ((pressedKeys.contains("A") ^ pressedKeys.contains("D")) ||
                pressedKeys.contains("←") ^ (pressedKeys.contains("→"))) {
            if (pressedKeys.contains("A") || pressedKeys.contains("←")) {
                player.animate("left");
                player.setxAccel(-200);
            }
            if (pressedKeys.contains("D") || (pressedKeys.contains("→"))) {
                player.animate("right");
                player.setxAccel(200);
            }
        } else {
            player.animate("default");
            player.setxAccel(0);
            player.setxVel(0);
        }
        if (pressedKeys.contains("Space")
                || pressedKeys.contains("␣")
                || pressedKeys.contains("↑")
                || pressedKeys.contains("W")) {
            player.setyVel(-600);
        }
    }
    private void checkBeacons(){
        if (player.getHitbox().intersects(beaconA.getHitbox())
                && echoA.echoReady()) {
            this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this,
                    (int)beaconA.getHitbox().getCenterX(),
                    (int)beaconA.getHitbox().getCenterY(), "Echo A"));
        }

        if (player.getHitbox().intersects(beaconB.getHitbox())
                && echoB.echoReady()) {
            this.dispatchEvent(new EchoEvent(Echo.ECHO_EVENT, this,
                    (int)beaconB.getHitbox().getCenterX(),
                    (int)beaconB.getHitbox().getCenterY(), "Echo B"));
        }
    }
    private void animateMapElements(){
        if (beaconA != null && beaconB != null) {
            beaconA.animate("main");
            beaconB.animate("main");
        }
        if (fog != null) {
            fog.animate("main");
        }

        if(ash != null && ash2 != null) {
            ash.animate("main");
            ash2.animate("main");
        }
    }
    private void playGameMusic() {
        try {
            soundguybill.loadMusic("main_loop","echo_music_loop.wav");
            soundguybill.playMusic("main_loop");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    //levels
    private void loadLevel( int lvl ) {
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
                break;
        }
    }

    //animated
    private void intro(){
        levelBlock.move(0, 400);
        levelBlock.setScaleX(1000 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock);
    }

    //go right
    private void level1(){
        levelBlock.move(0, 400);
        levelBlock.setScaleX(1000 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock);
    }

    // + obstacle
    private void level2(){
        levelBlock.move(0, 400);
        levelBlock.setScaleX(1000 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock);

        levelBlock2.move(300, 300);
        levelBlock2.setScaleX(100 / levelBlock.getUnscaledWidth());
        levelBlock2.setScaleY(150 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock2);

        levelBlock3.move(levelBlock2.getHitbox().x+70, 250);
        levelBlock3.setScaleX(125 / levelBlock.getUnscaledWidth());
        levelBlock3.setScaleY(200 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock3);

        beaconA.getPosition().translate(levelBlock2.getHitbox().x-80,
                levelBlock.getHitbox().y-beaconA.getUnscaledHeight());
        this.addChild(beaconA);


        this.addEventListener(echoA, Echo.ECHO_EVENT);
        echoes.addChild(echoA);
    }

    // + falling sometimes not ok
    private void level3(){
        // level 0.3
        levelBlock.move(0, 400);
        levelBlock.setScaleX(200 / levelBlock.getUnscaledWidth());
        levelBlock.setScaleY(200 / levelBlock.getUnscaledHeight());
        levelBlock.setAlpha((float).001);
        this.addChild(levelBlock);

        levelBlock2.move(300, 400);
        levelBlock2.setScaleX(200 / levelBlock.getUnscaledWidth());
        levelBlock2.setScaleY(200 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock2);

        levelBlock3.move(600, 400);
        levelBlock3.setScaleX(400 / levelBlock.getUnscaledWidth());
        levelBlock3.setScaleY(200 / levelBlock.getUnscaledHeight());
        this.addChild(levelBlock3);

        crystal1.setPosition(new Point(levelBlock.getHitbox().x+levelBlock.getHitbox().width,500));
        crystal1.setAlpha((float).1);
        crystal1.setScaleX(.55);
        crystal1.setScaleY(.6);
        this.addChild(crystal1);

        crystal2.setPosition(new Point(levelBlock2.getHitbox().x+levelBlock2.getHitbox().width,500));
        crystal2.setAlpha((float).1);
        crystal2.setScaleX(.55);
        crystal2.setScaleY(.6);
        this.addChild(crystal2);

        ash.setPosition(new Point(
                crystal1.getPosition().x,
                crystal1.getPosition().y-70)
        );
        ash.setAlpha((float) .21);
        ash.setScaleX(.55);
        this.addChild(ash);

        ash2.setPosition(new Point(
                crystal2.getPosition().x,
                crystal2.getPosition().y-70)
        );
        ash2.setAlpha((float) .21);
        ash2.setScaleX(.55);
        this.addChild(ash2);

        beaconA.getPosition().translate(levelBlock.getHitbox().x+levelBlock.getHitbox().width-60,
                levelBlock.getHitbox().y-beaconA.getUnscaledHeight());
        this.addChild(beaconA);

        beaconB.getPosition().translate(levelBlock2.getHitbox().x+levelBlock2.getHitbox().width-125,
                levelBlock2.getHitbox().y-beaconA.getUnscaledHeight());
        this.addChild(beaconB);

        this.addEventListener(echoA, Echo.ECHO_EVENT);
        echoes.addChild(echoA);
        this.addEventListener(echoB, Echo.ECHO_EVENT);
        echoes.addChild(echoB);
    }

    // + falling is ok
    private void level4(){}

    // + single platform with pitfall
    private void level5(){}

    // + platforms no pitfall
    private void level6(){}

    // + platforms & pitfalls
    private void level7(){}




    @Override
    public void draw(Graphics g) {
        super.draw(g);
//        g.setColor(new Color(255, 2, 104));
//        for (DisplayObject child : getChildren()) {
//            drawHitBox(g, child);
//        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Beta game = new Beta("Beta Test", 800, 600);
        game.start();
    }
}
