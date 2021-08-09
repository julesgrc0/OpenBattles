package com.julesG10.utils;

public class Size {
    public Size(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public Size mult(int a)
    {
        return new Size(this.width*a,this.height*a);
    }

    public Size mult(Size a)
    {
        return new Size(this.width*a.width,this.height*a.height);
    }

    public int width;
    public int height;
}
