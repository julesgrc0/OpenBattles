package com.julesG10.game.map;

import com.julesG10.game.Camera;
import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

public class Block {
    public static Size size = new Size(0, 0);
    public static final String[] blockNames = {"grass"};

    public static BlockType getTypeFromName(String name)
    {
        int index = 0;
        for (int i=0;i < blockNames.length; i++)
        {
            if(blockNames[i] == name)
            {
                index = i;
            }
        }

        return BlockType.values()[index];
    }

    public static String getNameFromType(BlockType type)
    {
        return blockNames[type.ordinal()];
    }

    public Block() {
    }

    public BlockType type;
    public Vector2 position;

    public int texture_index;
    public Texture[] textures;

    public void render() {
        this.textures[this.texture_index].render(this.position, Block.size, 0);
    }

    public void render(Vector2 chunk) {
        this.textures[this.texture_index].render(this.position.add(chunk), Block.size, 0);
    }

    public void render(Camera camera, Vector2 chunk) {
        this.textures[this.texture_index].render(camera.position.add(this.position.add(chunk)), Block.size, 0);
    }

    public void update(float deltatime,boolean player)
    {
            if (this.texture_index+1 >= this.textures.length) {
                this.texture_index = 0;
            }
            else{
                this.texture_index++;
            }
    }
}
