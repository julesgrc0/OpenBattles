package com.julesG10.game.player;

import com.julesG10.game.Camera;
import com.julesG10.game.map.Block;
import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public Player()
    {

    }

    public Vector2 position = new Vector2(0,0);
    public int life;
    public static Size size = new Size(0,0);

    public Texture texture;
    public List<Texture[]> textures = new ArrayList<>();
    public int texture_index;
    public int id;
    private float time;

    private PlayerDirection direction = PlayerDirection.TOP;

    public void changeDirection(PlayerDirection dir)
    {
        if(this.direction != dir) {
            this.time = 20;
            this.direction = dir;
        }
    }

    public void update(float deltatime)
    {
        this.time += deltatime * 100;
        if(this.time >= 20)
        {
            this.time=0;
            this.texture_index++;
            if(this.texture_index >= textures.get(this.direction.ordinal()).length)
            {
                this.texture_index=0;
            }

            this.texture = textures.get(this.direction.ordinal())[this.texture_index];
        }
    }

    public Vector2 getBlockPosition()
    {
        return this.position.roundTo(Block.size.toVector2());
    }

    public void render(Camera camera)
    {
        this.texture.render(camera.position.add(position),size,0);
    }
}
