package com.julesG10.game;

import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

enum BlockType
{

}

public class Block {
    public static Size size = new Size(0,0);

    public Block()
    {
    }

    public BlockType type;
    public Vector2 position;

    public int texture_index;
    public Texture[] textures;


    public void render()
    {
        this.textures[this.texture_index].render(this.position,Block.size,0);
    }

    public void render(Vector2 chunk)
    {
        this.textures[this.texture_index].render(this.position.add(chunk),Block.size,0);
    }

    public void render(Camera camera,Vector2 chunk)
    {
        this.textures[this.texture_index].render(camera.position.add(this.position.add(chunk)),Block.size,0);
    }

    public void update()
    {
        this.texture_index++;
        if(this.texture_index >= this.textures.length)
        {
            this.texture_index=0;
        }
    }
}
