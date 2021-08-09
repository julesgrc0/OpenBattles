package com.julesG10;

import com.julesG10.game.Block;
import com.julesG10.game.Camera;
import com.julesG10.game.Chunk;
import com.julesG10.game.World;
import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

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
        }

        glfwTerminate();
    }

    private boolean init() {
        if (!glfwInit())
        {
            return false;
        }

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

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CURSOR_HIDDEN,GLFW_TRUE);
        glfwWindowHint( GLFW_DOUBLEBUFFER, GL_FALSE );

        glfwWindowHint (GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint (GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint (GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint (GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwSetWindowPos(window, (this.glfwVidMode.width() - this.size.width) / 2, (this.glfwVidMode.height() - this.size.height) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        //  VerticalSync
        glfwSwapInterval(GLFW_FALSE);

        glfwShowWindow(window);
        return true;
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glViewport(0, 0, this.size.width,this.size.height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0,this.size.width,this.size.height,0,1,-1);
        glMatrixMode(GL_MODELVIEW);

        if(!glIsEnabled(GL_BLEND))
        {
            glEnable(GL_BLEND);
        }
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

        if(!glIsEnabled(GL_TEXTURE_2D))
        {
            glEnable(GL_TEXTURE_2D);
        }

        Block.size = new Size(this.size.width/10,this.size.height/10);

        List<Chunk> chunkList = new ArrayList<>();
        Chunk tmp = new Chunk(new Vector2(0,0));
        Texture def = new Texture("C:\\jules-dev\\~\\image processing\\c\\bitmapImage.png");
        for (int x=0;x < Chunk.size.width;x++)
        {
            for (int y=0;y<Chunk.size.height;y++)
            {
                Block block = new Block();
                block.position = new Vector2(x * Block.size.width,y * Block.size.height);
                block.textures = new Texture[1];
                block.textures[0] = def;
                tmp.blocks[x * Chunk.size.width + y] = block;
            }
        }

        chunkList.add(tmp);
        World world = new World(chunkList);
        world.camera = new Camera( new Vector2(0,0),this.size);

        Game game = new Game(this.window,world);
        game.start();

        Timer timer = new Timer();
        while ( !glfwWindowShouldClose(window) )
        {
            glfwPollEvents();
            float deltatime = timer.restart();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            world.render();
            glfwSwapBuffers(window);

            System.out.print("\r"+ 1.0 / deltatime);
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
