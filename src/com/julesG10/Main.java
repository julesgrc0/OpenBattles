package com.julesG10;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private long window;
    private Size size = new Size(400,400);
    private final String TITLE = "Open Battles";

    public void run() {
        if(this.init())
        {
            this.loop();
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);

            glfwTerminate();
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        }
    }

    private boolean init() {
        if (!glfwInit())
        {
            return false;
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CURSOR_HIDDEN,GLFW_TRUE);


        window = glfwCreateWindow(this.size.width, this.size.height, this.TITLE, NULL, NULL);
        if ( window == NULL )
        {
            return false;
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            {
                glfwSetWindowShouldClose(window, true);
            }
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }
        glfwMakeContextCurrent(window);

        //  VerticalSync
        glfwSwapInterval(GLFW_FALSE);

        glfwShowWindow(window);
        return true;
    }

    private void loop() {
        GL.createCapabilities();

        float deltatime =0;
        long start= System.nanoTime();

        while ( !glfwWindowShouldClose(window) )
        {
            deltatime = (float) ((System.nanoTime() - start) * Math.pow(10,-9));
            start = System.nanoTime();

            glfwSetWindowTitle(window,this.TITLE+" "+String.valueOf(1.0/deltatime)+" FPS");

            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
