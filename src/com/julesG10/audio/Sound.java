package com.julesG10.audio;

import javax.sound.sampled.Clip;

public class Sound {
    private int id;
    private Clip clip;
    private Thread soundThread;

    public Sound(Clip clip, int id) {
        this.clip = clip;
        this.id = id;
    }

    public void play(int loop) {
        if (!this.isPlaying()) {
            clip.loop(loop);

            this.soundThread = new Thread(() -> {
                clip.start();
            });

            this.soundThread.start();
        }
    }

    public void stop() {
        if (this.soundThread.isAlive()) {
            this.soundThread.interrupt();
        }
    }

    public void pause() {
        if (this.clip.isRunning() && this.soundThread.isAlive()) {
            this.clip.stop();
        }
    }

    public boolean isPlaying() {
        return this.clip.isRunning();
    }

    public int getId() {
        return this.id;
    }

    public long getTime() {
        return this.clip.getMicrosecondPosition();
    }

    public void setTime(long time) {
        this.clip.setMicrosecondPosition(time);
    }
}
