package edu.virginia.engine.display;

import edu.virginia.engine.util.GameClock;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jason.
 */
public class PhysicsSprite extends AnimatedSprite {

    private double xVel;
    private double yVel;
    private double xAccel;
    private double yAccel;
    private double xSpeedCap;
    private double ySpeedCap;

    private GameClock dT;

    public PhysicsSprite(String id, String fileName, double fps) throws FileNotFoundException {
        super(id, fileName, fps);
        init();
    }


    private void init() {
        xVel = 0;
        yVel = 0;
        xAccel = 0;
        yAccel = 0;
        xSpeedCap = 100;
        ySpeedCap = 100;

        dT = new GameClock();
    }

    @Override
    public void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
        if (dT != null) {
            double delta = dT.getElapsedTime() / 1000;

            this.getPosition().translate((int) (delta * xVel), (int) (delta * yVel));

            xVel += xAccel;
            yVel += yAccel;

            if (xVel > xSpeedCap) xVel = xSpeedCap;
            if (xVel < -xSpeedCap) xVel = -xSpeedCap;
            if (yVel > ySpeedCap) yVel = ySpeedCap;
            if (yVel < -ySpeedCap) yVel = -ySpeedCap;

            dT.resetGameClock();
        }
    }

    public double getxVel() {
        return xVel;
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    public double getyVel() {
        return yVel;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public double getxAccel() {
        return xAccel;
    }

    public void setxAccel(double xAccel) {
        this.xAccel = xAccel;
    }

    public double getyAccel() {
        return yAccel;
    }

    public void setyAccel(double yAccel) {
        this.yAccel = yAccel;
    }

    public double getxSpeedCap() {
        return xSpeedCap;
    }

    public void setxSpeedCap(double xSpeedCap) {
        this.xSpeedCap = xSpeedCap;
    }

    public double getySpeedCap() {
        return ySpeedCap;
    }

    public void setySpeedCap(double ySpeedCap) {
        this.ySpeedCap = ySpeedCap;
    }
}
