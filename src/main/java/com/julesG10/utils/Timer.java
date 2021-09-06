package com.julesG10.utils;

public class Timer {
    private float deltatime;
    private long start;

    public Timer() {
        this.deltatime = 0;
        this.start = System.nanoTime();
    }

    public float restart() {
        this.deltatime = (float) ((System.nanoTime() - start) * Math.pow(10, -9));
        this.start = System.nanoTime();
        return this.deltatime;
    }
}
