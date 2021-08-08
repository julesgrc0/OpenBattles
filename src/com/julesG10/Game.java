package com.julesG10;

import com.julesG10.network.Client;
import com.julesG10.utils.Timer;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends Thread {

    private long window;
    private long showUpdateRate;

    private boolean clientActive = true;
    public Client client;


    public Game(long window)
    {
        super();
        this.window = window;
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
                    String data = client.recieve();
                    if(data != null)
                    {

                    }
                }
            }
        });
        clientThread.start();

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
        }
    }
}
