package com.julesG10;

import com.julesG10.game.ClientManager;
import com.julesG10.game.map.Block;
import com.julesG10.game.map.Chunk;
import com.julesG10.network.clients.ChunkClient;
import com.julesG10.game.map.World;
import com.julesG10.game.player.Player;
import com.julesG10.network.clients.PlayerClient;
import com.julesG10.game.player.PlayerDirection;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.CameraUtils;
import com.julesG10.utils.Console;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends Thread {

    private long window;

    private boolean clientActive = true;
    public Client client;
    public World world;
    private List<ClientManager> clientManagerList = new ArrayList<>();
    private  Chunk currentChunk = null;

    public Game(long window, World world, Client client) {
        super();
        this.window = window;
        this.world = world;
        this.client = client;
    }

    public void run() {
        super.run();
        int tryCount = 0;
        while (client.connect(5000))
        {
            Console.log("Try connect "+tryCount+"/5");
            if(tryCount >= 5)
            {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored)
            {
                return;
            }
            tryCount++;
        }
        Console.log("Client connected");

        clientManagerList.add(new PlayerClient(this.world,this.client));
        clientManagerList.add(new ChunkClient(this.world,this.client));
        //clientManagerList.add(new TextureClient(,this.client));

        Thread clientThread = new Thread(() -> {
            while (clientActive) {
                String data = client.receive();
                if (data != null) {
                    this.onData(data);
                } else {
                    clientActive = false;
                    int id = world.players.get(0).id;
                    world.players.removeIf(player -> player.id != id);
                }
            }
        });
        clientThread.start();


        this.client.send(GameNetworkCodes.PING.ordinal()+"|");

        Timer timer = new Timer();
        while (!glfwWindowShouldClose(window)) {
            float deltatime = timer.restart();
            glfwPollEvents();
            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
                this.clientActive = false;
                glfwSetWindowShouldClose(window, true);
                break;
            }

            if(this.updateMove(deltatime))
            {
                this.updateBlock(deltatime);
            }

            //  System.out.print("\r"+this.client.getBytePerSecond()+" B/s");
        }
    }

    public void onData(String data)
    {


        String[] parts = data.split("\\|");
        GameNetworkCodes code = GameNetworkCodes.values()[Integer.parseInt(parts[0])];
        parts = Arrays.copyOfRange(parts,1,parts.length);

        for(ClientManager tClientManager : clientManagerList)
        {
            if(tClientManager.onData(code,parts))
            {
                break;
            }
        }

        if(code == GameNetworkCodes.PING)
        {
            float ping = (float)((System.nanoTime() - Long.parseLong(parts[0])) * Math.pow(10,-6));
            Console.log("Ping "+ping+" ms");
        }
    }

    public void updateBlock(float deltatime) {
       // Vector2 p_pos = world.players.get(0).position.add(Player.size.toVector2()).roundTo(Block.size.toVector2());
        Vector2 p_pos = world.players.get(0).position.roundTo(Block.size.toVector2());

        if(this.currentChunk != null)
        {
            if(this.isInChunk(p_pos,this.currentChunk.position))
            {
                for (Block b : currentChunk.blocks) {
                    if (b.position.add(currentChunk.position.mult(Block.size.toVector2())).equal(p_pos)) {
                        b.update(deltatime, true);
                        break;
                    }
                }

                return;
            }
        }

        for (Chunk c : world.chunks) {
            Vector2 c_pos = c.position.mult(Block.size.width * Chunk.size.width);
            if (this.isInChunk(p_pos, c_pos)) {
                this.currentChunk = c;
                for (Block b : c.blocks) {
                    if (b.position.add(c.position.mult(Block.size.toVector2())).equal(p_pos)) {
                        b.update(deltatime, true);
                        break;
                    }
                }
                break;
            }
        }
    }

    public boolean isInChunk(Vector2 playerPos,Vector2 c_pos)
    {
        if(playerPos.x >= c_pos.x && playerPos.x <= (c_pos.x + Chunk.size.width * Block.size.width))
        {
            if(playerPos.y >= c_pos.y && playerPos.y <= (c_pos.y + Chunk.size.height * Block.size.height))
            {
                return true;
            }
        }

        return false;
    }

    public boolean updateMove(float deltatime) {
        float add = deltatime * 400;
        float move = 0;
        boolean hasMove = false;
        Player player = this.world.players.get(0);

        if (glfwGetKey(this.window, GLFW_KEY_RIGHT) == GLFW_TRUE) {

            // this.world.camera.position.x -= add;
            player.position.x += add;
            player.changeDirection(PlayerDirection.RIGHT);
            move = add;
            hasMove = true;
        } else if (glfwGetKey(this.window, GLFW_KEY_LEFT) == GLFW_TRUE) {

            // this.world.camera.position.x += add;
            player.position.x -= add;
            player.changeDirection(PlayerDirection.LEFT);
            move = -add;
            hasMove = true;
        }

        if (glfwGetKey(this.window, GLFW_KEY_DOWN) == GLFW_TRUE) {
            if (move != 0) {
                player.position.x -= move / 2;
                // this.world.camera.position.x -= -move/2;
                add /= 2;
            }
            // this.world.camera.position.y -= add;
            player.position.y += add;
            player.changeDirection(PlayerDirection.BOTTOM);
            hasMove = true;
        } else if (glfwGetKey(this.window, GLFW_KEY_UP) == GLFW_TRUE) {
            if (move != 0) {
                player.position.x -= move / 2;
                // this.world.camera.position.x -= -move/2;
                add /= 2;

            }
            // this.world.camera.position.y += add;
            player.position.y -= add;
            player.changeDirection(PlayerDirection.TOP);
            hasMove = true;
        }

        if (this.world.camera.position.x + this.world.players.get(0).position.x != (Main.size.width - Player.size.width) / 2) {
            this.world.camera.position.x = (Main.size.width - Player.size.width) / 2
                    - this.world.players.get(0).position.x;
        }

        if (this.world.camera.position.y
                + this.world.players.get(0).position.y != (Main.size.height - Player.size.height) / 2) {
            this.world.camera.position.y = (Main.size.height - Player.size.height) / 2
                    - this.world.players.get(0).position.y;
        }

        if (hasMove) {
            player.update(deltatime);
            this.client.send(GameNetworkCodes.PLAYER_UPDATE.ordinal()+"|"+player.toString());
            return true;
        }

        return false;
    }
}
