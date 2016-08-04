package edu.virginia.engine.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    Clip sound;
    AudioInputStream input;
    String id;

    private void obtainFileInfo(String id, String filename) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.id = id;
        String file = ("resources" + File.separator + filename);
        File file_location = new File(file);
        input = AudioSystem.getAudioInputStream(file_location);
        AudioFormat format = input.getFormat();
        DataLine.Info data = new DataLine.Info(Clip.class, format);
        sound = (Clip) AudioSystem.getLine(data);
    }

    public void loadSoundEffect(String id, String filename) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        obtainFileInfo(id, filename);
        sound.open(input);
    }

    public void playSoundEffect(String id) throws IOException {
        sound.setMicrosecondPosition(0);
        sound.start();
        input.close();
    }

    public void loadMusic(String id, String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        obtainFileInfo(id, filename);
        sound.open(input);
    }

    public void playMusic(String id) throws IOException {
        sound.start();
        sound.loop(sound.getFrameLength());
        input.close();
    }

    public void stopMusic(String id){
        sound.stop();
    }

    public boolean soundLoaded() {
        return sound != null;
    }
}