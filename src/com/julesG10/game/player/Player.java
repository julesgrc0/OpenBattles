package com.julesG10.game.player;

import com.julesG10.game.Camera;
import com.julesG10.game.map.Block;
import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public Player() {


    }
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append("|");
        builder.append(position.x);
        builder.append("|");
        builder.append(position.y);
        builder.append("|");
        builder.append(life);
        return builder.toString();
    }

    public void setString(String str)
    {
        String[] parts = str.split("\\|");
        if(parts.length == 5)
        {
            this.id = Integer.parseInt(parts[0]);
            this.position.x = Float.parseFloat(parts[1]);
            this.position.y = Float.parseFloat(parts[2]);
            this.life = Integer.parseInt(parts[3]);
        }
    }


    public Vector2 position = new Vector2(0, 0);
    public int life = 0;
    public static Size size = new Size(0, 0);

    public Texture texture;
    public List<Texture[]> textures = new ArrayList<>();
    public int texture_index = 0;
    public int id = 0;
    private float time = 0;

    private PlayerDirection direction = PlayerDirection.TOP;

    public void changeDirection(PlayerDirection dir) {
        if (this.direction != dir) {
            this.time = 20;
            this.direction = dir;
        }
    }

    public void update(float deltatime) {
        this.time += deltatime * 100;
        if (this.time >= 20) {
            this.time = 0;
            this.texture_index++;
            if (this.texture_index >= textures.get(this.direction.ordinal()).length) {
                this.texture_index = 0;
            }

            this.texture = textures.get(this.direction.ordinal())[this.texture_index];
        }
    }

    public Vector2 getBlockPosition() {
        return this.position.roundTo(Block.size.toVector2());
    }

    public void render(Camera camera) {
        this.texture.render(camera.position.add(position), size, 0);
    }

}
