package com.julesG10;

import com.julesG10.game.map.BlockType;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
import com.julesG10.game.player.Player;
import com.julesG10.game.player.PlayerDirection;
import com.julesG10.network.Client;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;

import static org.lwjgl.glfw.GLFW.*;

enum GameClientCode{
    PLAYER_UPDATE,
    PLAYER_ADD,
    PLAYER_LEAVE,
    MAP_UPDATE
}

public class Game extends Thread {

    private long window;
    private long showUpdateRate;

    private boolean clientActive = true;
    public Client client;
    public World world;


    public Game(long window,World world)
    {
        super();
        this.window = window;
        this.world = world;
    }

    public void run()
    {
        super.run();
        Thread clientThread = new Thread(() -> {
            client = new Client("",22);
            if(client.connect(5000))
            {
                while (clientActive)
                {
                    String raw = client.recieve();
                    if(raw != null)
                    {
                        int code = raw.charAt(0)+raw.charAt(1);
                        String data = raw.substring(0,2) + raw.substring(3);
                        GameClientCode clientCode = GameClientCode.values()[code];
                        String[] update = data.split(";");
                        switch (clientCode)
                        {
                            case MAP_UPDATE:
                                Vector2 chunk_position = new Vector2(Float.parseFloat(update[0]),Float.parseFloat(update[1]));
                                for (Chunk c : this.world.chunks)
                                {
                                    if(c.position.equal(chunk_position))
                                    {
                                        for (int i=2;i<update.length;i++)
                                        {
                                            int index = Integer.getInteger(update[i]);
                                            BlockType type = BlockType.values()[index];

                                            if(c.blocks[0].type != type)
                                            {
                                                c.blocks[0].texture_index=0;
                                                c.blocks[0].type = type;
                                            }
                                        }
                                    }
                                }
                                break;
                            case PLAYER_ADD:
                                Player player = new Player();
                                player.position =  new Vector2(Float.parseFloat(update[0]),Float.parseFloat(update[1]));
                                player.texture_index = 0;
                                player.id = Integer.getInteger(update[2]);
                                this.world.addPlayer(player);
                                break;
                            case PLAYER_LEAVE:
                                int index=0;
                                for (Player p : this.world.players)
                                {
                                    if(p.id == Integer.getInteger(update[0]))
                                    {
                                        break;
                                    }
                                    index++;
                                }
                                this.world.players.remove(index);
                                break;
                            case PLAYER_UPDATE:
                                for (Player p : this.world.players)
                                {
                                    if(p.id == Integer.getInteger(update[0]))
                                    {
                                        p.position=  new Vector2(Float.parseFloat(update[0]),Float.parseFloat(update[1]));
                                        break;
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        });
        clientThread.start();

        boolean[] wasPress = new boolean[]{
                false, // x+
                false, // x-
                false, // y+
                false  //  y-
        };
        float[] timeAfterPress = new float[]{0,0,0,0};

        Timer timer = new Timer();
        while (!glfwWindowShouldClose(window)) {
            float deltatime = timer.restart();
            glfwPollEvents();
            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
                this.clientActive = false;
                glfwSetWindowShouldClose(window, true);
                break;
            }

            this.updateMove(deltatime);
        }
    }

    public void updateMove(float deltatime)
    {
        float add = deltatime * 400;
        float move = 0;
        boolean hasMove = false;

        if (glfwGetKey(this.window, GLFW_KEY_RIGHT) == GLFW_TRUE) {

            //this.world.camera.position.x -= add;
            this.world.players.get(0).position.x += add;
            this.world.players.get(0).changeDirection(PlayerDirection.RIGHT);
            move = add;
            hasMove=true;
        }else if (glfwGetKey(this.window,GLFW_KEY_LEFT ) == GLFW_TRUE) {

            //this.world.camera.position.x += add;
            this.world.players.get(0).position.x -= add;
            this.world.players.get(0).changeDirection(PlayerDirection.LEFT);
            move = -add;
            hasMove=true;
        }

        if (glfwGetKey(this.window, GLFW_KEY_DOWN) == GLFW_TRUE) {
            if(move != 0)
            {
                this.world.players.get(0).position.x -= move/2;
              //  this.world.camera.position.x -= -move/2;
                add /=2;
            }
            //this.world.camera.position.y -= add;
            this.world.players.get(0).position.y += add;
            this.world.players.get(0).changeDirection(PlayerDirection.BOTTOM);
            hasMove=true;
        }else if (glfwGetKey(this.window,GLFW_KEY_UP ) == GLFW_TRUE) {
            if(move != 0)
            {
                this.world.players.get(0).position.x -= move/2;
              //  this.world.camera.position.x -= -move/2;
                add /=2;

            }
            //this.world.camera.position.y += add;
            this.world.players.get(0).position.y -= add;
            this.world.players.get(0).changeDirection(PlayerDirection.TOP);
            hasMove=true;
        }

        if(this.world.camera.position.x + this.world.players.get(0).position.x != (Main.size.width - Player.size.width)/2)
        {
            this.world.camera.position.x = (Main.size.width - Player.size.width)/2 - this.world.players.get(0).position.x;
        }

        if(this.world.camera.position.y + this.world.players.get(0).position.y != (Main.size.height - Player.size.height)/2)
        {
            this.world.camera.position.y = (Main.size.height - Player.size.height)/2 - this.world.players.get(0).position.y;
        }

        if(hasMove)
        {
            this.world.players.get(0).update(deltatime);
        }

    }
}
