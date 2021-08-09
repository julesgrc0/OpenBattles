package com.julesG10;

import com.julesG10.game.Camera;
import com.julesG10.game.World;
import com.julesG10.network.Client;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;

import static org.lwjgl.glfw.GLFW.*;

enum GameClientCode{
    PLAYER_POSITION,

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
                    }
                }
            }
        });
        clientThread.start();

        boolean wasPress = false;
        float timeAfterPress = 0;
        Timer timer = new Timer();
        while (!glfwWindowShouldClose(window))
        {
            float deltatime = timer.restart();
            if(glfwGetKey(window,GLFW_KEY_ESCAPE) == GLFW_TRUE)
            {
                this.clientActive = false;
                glfwSetWindowShouldClose(window, true);
                break;
            }

            if(glfwGetKey(window,GLFW_KEY_LEFT) == GLFW_TRUE)
            {
                wasPress = true;
                this.world.camera.position.x -= deltatime * 400;
            }else if(wasPress)
            {
                wasPress = false;
                timeAfterPress += deltatime * 100;
                this.world.camera.position.x -= deltatime * (100 - timeAfterPress);
            }else if(timeAfterPress != 0)
            {
                timeAfterPress += deltatime * 100;
                this.world.camera.position.x -= deltatime * (100 - timeAfterPress);
                if(timeAfterPress >= 100)
                {
                    timeAfterPress = 0;
                }

            }
            if(glfwGetKey(window,GLFW_KEY_RIGHT) == GLFW_TRUE)
            {
                this.world.camera.position.x += deltatime * 400;
            }

        }
    }
}
