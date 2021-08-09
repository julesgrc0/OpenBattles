package com.julesG10;

import com.julesG10.game.Camera;
import com.julesG10.game.Player;
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
        else if (was[index]) {
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
        }
    }
}
