package com.julesG10.utils;

public class Vector2 {
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 mult(float a) {
        return new Vector2(this.x * a, this.y * a);
    }

    public float x;
    public float y;
}
