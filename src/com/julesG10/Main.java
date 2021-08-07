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
    private Size size;
    private GLFWVidMode glfwVidMode;
    public static final String TITLE = "Open Battles";

    public void run() {
        if(this.init())
        {
            this.loop();

            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
            glfwTerminate();
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

        long monitor = glfwGetPrimaryMonitor();
        this.glfwVidMode = glfwGetVideoMode(monitor);

        glfwWindowHint(GLFW_RED_BITS, this.glfwVidMode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, this.glfwVidMode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, this.glfwVidMode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, this.glfwVidMode.refreshRate());
        this.size = new Size(this.glfwVidMode.width(),this.glfwVidMode.height());

        window = glfwCreateWindow(this.size.width, this.size.height,TITLE, monitor, NULL);

        if (window == NULL)
        {
            return false;
        }
        glfwSetWindowMonitor(window,monitor,0,0,this.size.width,this.size.height,this.glfwVidMode.refreshRate());

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
        Game game = new Game(this.window);
        game.start();

        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        if(!glIsEnabled(GL_TEXTURE_2D))
        {
            glEnable(GL_TEXTURE_2D);
        }
        Texture test = new Texture("C:/jules-dev/release/Tamagotchi/Tamagotchi/x64/Release/assets/backgrounds/ground_0.png");

        float tileSize = 0.2f;


        Timer timer = new Timer();

        while ( !glfwWindowShouldClose(window) )
        {
            glfwPollEvents();
            float deltatime = timer.restart();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            if(test.isValid()) {
                test.bind();

                glBegin(GL_QUADS);

                glTexCoord2f(0, 0);
                glVertex2f(-(tileSize), (tileSize));


                glTexCoord2f(1, 0);
                glVertex2f((tileSize), (tileSize));

                glTexCoord2f(1, 1);
                glVertex2f((tileSize), -(tileSize));


                glTexCoord2f(0, 1);
                glVertex2f(-(tileSize), -(tileSize));

                glEnd();
            }

            glfwSwapBuffers(window);

            System.out.print("\r"+String.valueOf(1.0/deltatime));
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }


    /*
     glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            {
                glfwSetWindowShouldClose(window, true);
            }
        });
    */
}