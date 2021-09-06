package com.julesG10.game;

import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

public class Camera {
    public Camera() {

    }

    public Camera(Vector2 position, Size size) {
        this.position = position;
        this.size = size;
    }

    public boolean isInView(Vector2 position, Size size) {
        return this.position.x < position.x + size.width && this.position.x + this.size.width > position.x
                && this.position.y < position.y + size.height && this.position.y + this.size.height > position.y;
    }

    public boolean active = true;
    public Vector2 position;
    public Size size;
}
