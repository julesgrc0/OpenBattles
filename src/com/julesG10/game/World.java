package com.julesG10.game;


import java.util.ArrayList;
import java.util.List;

public class World {
    public List<Chunk> chunks = new ArrayList<Chunk>();
    private Camera camera = new Camera();

    public World() {
        this.camera.active = false;
    }

    public World(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    public void addCamera(Camera camera) {
        this.camera = camera;
    }

    public void render() {
        for (int i = 0; i < this.chunks.size(); i++) {
            if (this.camera.isInView(this.chunks.get(i).position.mult(Block.size.width * Chunk.size.width), Chunk.size)) {
                for (Block block : this.chunks.get(i).getBlocks()) {
                    block.render();
                }
            }
        }
    }

}
