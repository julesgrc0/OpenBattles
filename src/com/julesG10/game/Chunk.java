package com.julesG10.game;

import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

public class Chunk {

    public static final Size size = new Size(10, 10);

    private Block[] blocks;
    public Vector2 position;

    public Chunk(Block[] blocks, Vector2 position) {
        this.blocks = blocks.clone();
        this.position = position;
    }

    public Chunk(Vector2 position) {
        this.blocks = new Block[size.width * size.height];
        this.position = position;

        for (int i = 0; i < this.blocks.length; i++) {
            this.blocks[i] = new Block();
        }
    }

    public Block[] getBlocks() {
        return this.blocks;
    }

    public void setBlock(Block block) {
        int index = (int) (block.position.x * size.width * block.position.y);
        if (!(index >= this.blocks.length)) {
            this.blocks[index] = block;
        }
    }
}
