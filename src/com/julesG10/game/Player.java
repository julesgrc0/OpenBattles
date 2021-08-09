package com.julesG10.game;

import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

public class Player {
    public Player()
    {

    }

    public Vector2 position = new Vector2(0,0);
    public static Size size = new Size(0,0);
    public Texture[] textures;
    public int texture_index;

    public void update()
    {

    }

    public void render(Camera camera)
    {
        this.textures[this.texture_index].render(camera.position.add(this.position),size,0);
    }
}
