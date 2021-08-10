package com.julesG10;

import com.julesG10.game.*;
import com.julesG10.game.map.Block;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private long window;
    public static Size size;
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
        size = new Size(this.glfwVidMode.width(),this.glfwVidMode.height());

        window = glfwCreateWindow(size.width, size.height,TITLE, monitor, NULL);
        if (window == NULL)
        {
            return false;
        }
        glfwSetWindowMonitor(window,monitor,0,0,size.width,size.height,this.glfwVidMode.refreshRate());

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

        Block.size = new Size(size.width/10,size.height/10);
        Player.size = Block.size;

        World world = new World();
        world.addPlayer(new Player());
        world.camera = new Camera(new Vector2((Main.size.width - Player.size.width)/2,(Main.size.height-Player.size.height)/2),this.size);

        Game game = new Game(this.window,world);
        game.start();


        List<Chunk> chunkList = new ArrayList<>();
        Chunk tmp = new Chunk(new Vector2(0,0));
        Map<Integer,Texture[]> gameTextures = new HashMap<>();
        Texture[] tmpTextures = new Texture[1];
        tmpTextures[0] = new Texture("C:\\jules-dev\\~\\image processing\\c\\bitmapImage.png");
        gameTextures.put(0,tmpTextures);

        tmpTextures = new Texture[7];
        for (int i=0;i<tmpTextures.length;i++)
        {
            tmpTextures[i] = new Texture("C:\\jules-dev\\release\\Tamagotchi\\Tamagotchi\\x64\\Release\\assets\\type_1\\angry\\angry_"+i+".png");
        }
        gameTextures.put(1,tmpTextures);

        world.players.get(0).textures = gameTextures.get(1);

        for (int x=0;x < Chunk.size.width;x++)
        {
            for (int y=0;y<Chunk.size.height;y++)
            {
                Block block = new Block();
                block.position = new Vector2(x * Block.size.width,y * Block.size.height);
                block.textures = gameTextures.get(0);
                tmp.blocks[x * Chunk.size.width + y] = block;
            }
        }
        chunkList.add(tmp);

        world.chunks = chunkList;

        Timer timer = new Timer();
        while ( !glfwWindowShouldClose(window) )
        {
            glfwPollEvents();
            float deltatime = timer.restart();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            world.render();
            glfwSwapBuffers(window);

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
