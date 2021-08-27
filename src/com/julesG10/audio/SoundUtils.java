package com.julesG10.audio;

import com.julesG10.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundUtils {

    private static int soundId = 0;

    public static Sound load(String path) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream(path));
            clip.open(inputStream);

            Sound sound = new Sound(clip, soundId);
            soundId++;

            return sound;
        } catch (Exception e) {
            return null;
        }
    }
}
