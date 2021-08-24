package com.julesG10;

import com.julesG10.game.map.BlockType;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
import com.julesG10.game.player.Player;
import com.julesG10.game.player.PlayerDirection;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Size;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends Thread {

    private long window;
    private long showUpdateRate;

    private boolean clientActive = true;
    public Client client;
    public World world;


    public Game(long window,World world,Client client)
    {
        super();
        this.window = window;
        this.world = world;
        this.client = client;
    }

    public void run()
    {
        super.run();
        Thread clientThread = new Thread(() -> {
            if(client.connect(5000))
            {
                while (clientActive)
                {
                    String data = client.receive();
                    if(data != null) {
                        this.onData(data);
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

    public Vector2 mousePosition()
    {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, posX, posY);
        return new Vector2((float)posX.get(),(float)posY.get());
    }

    public Vector2 getPositionItemCamera(Vector2 vec, Vector2 gameItemPosition, Vector2 cameraPosition,Vector2 centerCamera, Size round)
    {
        vec = vec.add(gameItemPosition);
        vec = vec.min(centerCamera);
        vec = vec.roundTo(round.toVector2());
        return cameraPosition.add(vec);
    }

    public void onData(String data)
    {
        String[] parts = data.split("\\|");

        GameNetworkCodes code = GameNetworkCodes.values()[(int)Float.parseFloat(parts[0])];
        parts =  Arrays.copyOfRange(parts, 1, parts.length);

        if(code == GameNetworkCodes.MAP_UPDATE)
        {
            Vector2 chunk_position = new Vector2(Float.parseFloat(parts[0]),Float.parseFloat(parts[1]));
            for (Chunk c : this.world.chunks)
            {
                if(c.position.equal(chunk_position))
                {
                    for (int i=2;i<parts.length;i++)
                    {
                        int index = Integer.getInteger(parts[i]);
                        BlockType type = BlockType.values()[index];

                        if(c.blocks[0].type != type)
                        {
                            c.blocks[0].texture_index=0;
                            c.blocks[0].type = type;
                        }
                    }
                }
            }
        }else if(code == GameNetworkCodes.PLAYER_ADD)
        {
            Player player = new Player();
            player.position =  new Vector2(Float.parseFloat(parts[0]),Float.parseFloat(parts[1]));
            player.texture_index = 0;
            player.id = (int)Float.parseFloat(parts[2]);
            this.world.addPlayer(player);
        }else if(code == GameNetworkCodes.PLAYER_CLEAR)
        {
            Player player = this.world.players.get(0);
            this.world.players.clear();
            this.world.players.add(player);
        }else if(code == GameNetworkCodes.PLAYER_UPDATE)
        {
            for (Player p : this.world.players)
            {
                if(p.id == (int)Float.parseFloat(parts[0]))
                {
                    p.position=  new Vector2(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]));
                    break;
                }
            }
        }else if(code == GameNetworkCodes.INIT)
        {
            this.world.players.get(0).id = (int)Float.parseFloat(parts[0]);
        }
    }

    public void sendPlayer()
    {
        if(this.client.client.isConnected())
        {
            StringBuilder builder = new StringBuilder();
            Player player = this.world.players.get(0);

            builder.append(GameNetworkCodes.PLAYER_UPDATE.ordinal());
            builder.append("|");
            builder.append(player.id);
            builder.append("|");
            builder.append(player.position.x);
            builder.append("|");
            builder.append(player.position.y);
            builder.append("|");
            builder.append(player.life);
            builder.append("|");
            builder.append(System.nanoTime());

            this.client.send(builder.toString());
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
            this.sendPlayer();
        }
    }
}
