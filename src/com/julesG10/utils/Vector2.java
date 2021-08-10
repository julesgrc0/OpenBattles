package com.julesG10.utils;

public class Vector2 {
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 mult(float a) {
        return new Vector2(this.x * a, this.y * a);
    }

    public Vector2 mult(Vector2 vector2)
    {
        return new Vector2(this.x *vector2.x, this.y * vector2.y);
    }

    public Vector2 min(Vector2 vector2) {
        return new Vector2(this.x - vector2.x, this.y - vector2.y);
    }

    public Vector2 add(Vector2 vector2) {
        return new Vector2(this.x + vector2.x, this.y + vector2.y);
    }

    public float x;
    public float y;

   public boolean equal(Vector2 v)
   {
       return this.x == v.x && this.y == v.y;
   }
}
