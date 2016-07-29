package edu.virginia.engine.display;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason on 7/14/2016.
 */
public class AnimatedSprite extends Sprite {

    private HashMap<String, Animation> animations;
    private Animation animation;
    private BufferedImage defaultImage;
    private double fps;

    private GameClock dT;

    public AnimatedSprite(String id, String fileName, double fps) throws FileNotFoundException {
        super(id);
        loadAnimations(fileName);
        this.fps = fps;

        dT = new GameClock();
    }

    private void loadAnimations(String fileName) throws FileNotFoundException {
        FileReader spriteData = new FileReader(new File("resources" + File.separator + fileName + ".json"));
        BufferedImage spriteImage = readImage(fileName + ".png");

        animations = new HashMap<>();
        animation = new Animation();

        Gson gson = new Gson();
        Type dataType = new TypeToken<Collection<HashMap<String, Object>>>() {
        }.getType();
        Collection<HashMap<String, Object>> frames = gson.fromJson(spriteData, dataType);
        for (HashMap<String, Object> frame : frames) {
            String frameName = (String) frame.get("filename");
            Map<String, Double> dimm = (Map<String, Double>) frame.get("frame");
            int x = dimm.get("x").intValue();
            int y = dimm.get("y").intValue();
            int w = dimm.get("w").intValue();
            int h = dimm.get("h").intValue();
            BufferedImage frameImage = spriteImage.getSubimage((int) x, y, w, h);
            if ((boolean) frame.get("default")) {
                this.defaultImage = frameImage;
                this.setImage(frameImage);
            }
            if ((boolean) frame.get("flipped")) {
                frameImage = createFlipped(frameImage);
            }
            String animationName = frameName.substring(frameName.indexOf('_') + 1, frameName.indexOf('-'));
            if (!animations.containsKey(animationName)) {
                animations.put(animationName, new Animation());
            }
            animations.get(animationName).addFrame(frameImage);
        }

    }

    private BufferedImage createFlipped(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        return createTransformed(image, at);
    }

    private BufferedImage createTransformed(
            BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public void animate(String animation) {
        if (animations.containsKey(animation)) {
            if (this.animation != animations.get(animation)) {
                this.animation.end();
                this.animation = animations.get(animation);
            }
        }
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    @Override
    public void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
        if (dT != null && dT.getElapsedTime() > 1000 / fps) {
            BufferedImage nextFrame = animation.nextFrame();
            if (nextFrame == null) {
                this.setImage(defaultImage);
            } else {
                this.setImage(nextFrame);
            }
            dT.resetGameClock();
        }
    }

    private class Animation {

        private ArrayList<BufferedImage> frames;
        int currentFrame;

        public Animation() {
            frames = new ArrayList<>();
            currentFrame = 0;
        }

        public void addFrame(BufferedImage frame) {
            frames.add(frame);
        }

        public BufferedImage nextFrame() {
            if (frames.size() > 0) {
                if (currentFrame + 1 == frames.size()) {
                    currentFrame = 0;
                } else {
                    currentFrame++;
                }
                return frames.get(currentFrame);
            }
            return null;
        }

        public void end() {
            currentFrame = 0;
        }
    }
}
