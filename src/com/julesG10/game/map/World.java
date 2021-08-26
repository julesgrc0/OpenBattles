package com.julesG10.game.map;


import com.julesG10.game.Camera;
import com.julesG10.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class World {
    public List<Chunk> chunks = new ArrayList<>();
    public List<Player> players = new ArrayList<>();
    public Camera camera = new Camera();

    public World() {
        this.camera.active = false;
    }

    public World(List<Chunk> chunks) {
        this.chunks = chunks;
        this.camera.active = false;
    }

    public int addPlayer(Player p)
    {
        this.players.add(p);
        return this.players.size()-1;
    }

    public void render() {
        if(this.camera.active)
        {
            for (int i = 0; i < this.chunks.size(); i++) {
                if (this.camera.isInView(this.chunks.get(i).position.mult(Block.size.width * Chunk.size.width), Chunk.size.mult(Block.size)))
                {
                    for (Block block : this.chunks.get(i).blocks) {
                        block.render(this.camera,this.chunks.get(i).position.mult(Block.size.width * Chunk.size.width));
                    }
                }
            }

            Object[] objs = this.players.toArray();
            for (Object obj : objs)
            {
                Player p = (Player)obj;
                if(this.camera.isInView(p.position.add(this.camera.position),Player.size))
                {
                    p.render(this.camera);
                }
            }
        }
    }

}
