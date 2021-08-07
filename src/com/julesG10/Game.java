package com.julesG10;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends Thread {

    private long window;
    private long showUpdateRate;

    public Game(long window)
    {
        super();
        this.window = window;
    }

    public void run()
    {
        super.run();
        Timer timer = new Timer();
        while (!glfwWindowShouldClose(window))
        {
            float deltatime = timer.restart();
            if(glfwGetKey(window,GLFW_KEY_ESCAPE) == GLFW_TRUE)
            {
                glfwSetWindowShouldClose(window, true);
                break;
            }
        }
    }
}
