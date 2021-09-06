package com.julesG10.utils;

import com.julesG10.Main;
import com.julesG10.audio.Sound;
import com.julesG10.audio.SoundUtils;
import com.julesG10.graphics.Texture;
import org.w3c.dom.Text;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AssetsManager {
    public static File applicationPath;
    public static File assetsPath;

    public static void Init() {
        try {
            applicationPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (applicationPath.isFile()) {
                applicationPath = new File(applicationPath.getParent());
            }
            assetsPath = new File(applicationPath.getPath() + File.separator + "assets");
        } catch (URISyntaxException ignored) {
        }
    }

    public static String getWorldDirectory() {
        return applicationPath + File.separator + "save";
    }

    public static Texture[] loadTextureDirectory(String name) {
        List<Texture> textures = new ArrayList<>();
        File dir = new File(assetsPath.getPath() + File.separator + name);
        if (dir.listFiles() != null) {
            for (final File fileEntry : dir.listFiles()) {
                if (!fileEntry.isDirectory() && fileEntry.canRead()) {
                    if (fileEntry.getName().endsWith(".png") || fileEntry.getName().endsWith(".jpg")) {
                        textures.add(new Texture(fileEntry.getPath()));
                    }
                }
            }
        }

        return textures.toArray(new Texture[0]);
    }

    public static Sound[] loadSoundDirectory(String name) {
        List<Sound> soundList = new ArrayList<>();
        File dir = new File(assetsPath.getPath() + File.separator + name);
        if (dir.listFiles() != null) {
            for (final File fileEntry : dir.listFiles()) {
                if (!fileEntry.isDirectory() && fileEntry.canRead()) {
                    if (fileEntry.getName().endsWith(".mp3") || fileEntry.getName().endsWith(".wav")) {
                        soundList.add(SoundUtils.load(fileEntry.getPath()));
                    }
                }
            }
        }
        return soundList.toArray(new Sound[0]);
    }

}
