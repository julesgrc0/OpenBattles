package com.julesG10;

import com.julesG10.game.ClientManager;
import com.julesG10.game.map.ChunkClient;
import com.julesG10.game.map.World;
import com.julesG10.game.player.Player;
import com.julesG10.game.player.PlayerClient;
import com.julesG10.game.player.PlayerDirection;
import com.julesG10.graphics.Texture;
import com.julesG10.graphics.TextureClient;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Console;
import com.julesG10.utils.Timer;

import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends Thread {

    private long window;

    private boolean clientActive = true;
    public Client client;
    public World world;
    private List<ClientManager> clientManagerList;

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

            this.updateMove(deltatime);

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

    public void updateMove(float deltatime) {
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
        }
    }
}
