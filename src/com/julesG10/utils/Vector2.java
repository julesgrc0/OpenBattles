package com.julesG10.utils;

import com.julesG10.game.map.Block;

import java.util.Vector;

public class Vector2 {
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 mult(float a) {
        return new Vector2(this.x * a, this.y * a);
    }

    public Vector2 mult(Vector2 vector2) {
        return new Vector2(this.x * vector2.x, this.y * vector2.y);
    }

    public Vector2 min(Vector2 vector2) {
        return new Vector2(this.x - vector2.x, this.y - vector2.y);
    }

    public Vector2 add(Vector2 vector2) {
        return new Vector2(this.x + vector2.x, this.y + vector2.y);
    }

    public Vector2 div(float a)
    {
        return new Vector2(this.x/a,this.y/a);
    }

    public float x;
    public float y;

    public boolean equal(Vector2 v) {
        return this.x == v.x && this.y == v.y;
    }

    public float distance(Vector2 vec) {
        return (float) Math.sqrt(Math.pow(vec.x - this.x, 2) + Math.pow(vec.y - this.y, 2) * 1.0);
    }

    public Vector2 roundTo(Vector2 vec)
    {
        return new Vector2((int)(this.x/vec.x)*vec.x,(int)(this.y/ vec.y)*vec.y);
    }

    public Size toSize()
    {
        return new Size((int)this.x,(int)this.y);
    }
}
