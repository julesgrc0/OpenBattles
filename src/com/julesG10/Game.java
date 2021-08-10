package com.julesG10;

import com.julesG10.game.*;
import com.julesG10.game.map.BlockType;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
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

            this.CameraEffect(deltatime, GLFW_KEY_RIGHT, 0, -(deltatime * 400), true, timeAfterPress, wasPress,150);
            this.CameraEffect(deltatime, GLFW_KEY_LEFT, 1, (deltatime * 400), true, timeAfterPress, wasPress,150);
            this.CameraEffect(deltatime, GLFW_KEY_DOWN, 2, -(deltatime * 400), false, timeAfterPress, wasPress,150);
            this.CameraEffect(deltatime, GLFW_KEY_UP, 3, (deltatime * 400), false, timeAfterPress, wasPress,150);
        }
    }

    public void CameraEffect(float deltatime,int key,int index,float add,boolean x,float[] after,boolean[] was,float duration) {
        if (glfwGetKey(this.window, key) == GLFW_TRUE) {
            was[index] = true;
            if (x) {
                this.world.camera.position.x += add;
                this.world.players.get(0).position.x -= add;
            } else {
                this.world.camera.position.y += add;
                this.world.players.get(0).position.y -= add;
            }
        }
        /*else if (was[index]) {
            was[index] = false;
            after[index] += deltatime * 100;
            if (x) {
                this.world.camera.position.x += add > 0 ? (deltatime * (duration - after[index])) : -(deltatime * (100 - after[index]));
                this.world.players.get(0).position.x -=  add > 0 ? (deltatime * (duration - after[index])) : -(deltatime * (100 - after[index]));
            } else {
                this.world.camera.position.y += add > 0 ? (deltatime * (duration - after[index])) : -(deltatime * (100 - after[index]));
                this.world.players.get(0).position.y -=  add > 0 ? (deltatime * (duration - after[index])) : -(deltatime * (100 - after[index]));
            }

        } else if (after[index] != 0) {
            after[index] += deltatime * 100;
            float add_effect = add > 0 ? (deltatime * (duration - after[index])) : -(deltatime * (100 - after[index]));
            if (x) {
                this.world.camera.position.x += add_effect;
                this.world.players.get(0).position.x -= add_effect;
            } else {
                this.world.camera.position.y += add_effect;
                this.world.players.get(0).position.y -=  add_effect;
            }
            if (after[index] >= duration) {
                after[index] = 0;
            }
        }*/
    }
}
