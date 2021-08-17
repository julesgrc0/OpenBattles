package com.julesG10.game.map;

import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

public class Chunk {

    public static final Size size = new Size(10, 10);

    public Block[] blocks;
    public Vector2 position;

    public Chunk(Block[] blocks, Vector2 position) {
        this.blocks = blocks.clone();
        this.position = position;
    }

    public Chunk(Vector2 position) {
        this.blocks = new Block[size.width * size.height];
        this.position = position;

       for (int x=0;x < size.width;x++)
       {
           for (int y=0;y<size.height;y++)
           {
               Block block = new Block();
               block.position = new Vector2(x * Block.size.width,y * Block.size.height);
               this.blocks[x * size.width + y] = block;
           }
        }
    }

    public Block getBlock(Vector2 position) {
        return this.blocks[(int) (position.x * size.width + position.y)];
    }

    public void setBlock(Vector2 position,Block block)
    {
        this.blocks[(int) (position.x * size.width + position.y)] = block;
    }
}
