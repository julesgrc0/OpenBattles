package com.julesG10.game;


import java.util.ArrayList;
import java.util.List;

public class World {
    public List<Chunk> chunks = new ArrayList<Chunk>();
    public Camera camera = new Camera();

    public World() {
        this.camera.active = false;
    }

    public World(List<Chunk> chunks) {
        this.chunks = chunks;
        this.camera.active = false;
    }

    public void render() {
        if(this.camera.active)
        {
            for (int i = 0; i < this.chunks.size(); i++) {
                if (this.camera.isInView(this.chunks.get(i).position.mult(Block.size.width * Chunk.size.width), Chunk.size.mult(Block.size))) {
                    for (Block block : this.chunks.get(i).blocks) {
                        block.render(this.camera,this.chunks.get(i).position.mult(Block.size.width * Chunk.size.width));
                    }
                }
            }
        }else{
            for (int i = 0; i < this.chunks.size(); i++) {
                    for (Block block : this.chunks.get(i).blocks) {
                        block.render(this.chunks.get(i).position.mult(Block.size.width * Chunk.size.width));
                    }
            }
        }
    }

}
