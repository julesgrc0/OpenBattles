package com.julesG10;

import com.julesG10.game.*;
import com.julesG10.game.map.Block;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
import com.julesG10.game.map.WorldLoader;
import com.julesG10.game.player.Player;
import com.julesG10.graphics.Texture;
import com.julesG10.utils.AssetsManager;
import com.julesG10.utils.Size;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.File;
import java.nio.DoubleBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private long window;
    public static Size size;
    private GLFWVidMode glfwVidMode;

    public static final String TITLE = "Open Battles";
    private World world;

    private Size userSize = new Size(-1,-1);
    private boolean saveWorld = false;
    private boolean showFps = false;
    private boolean showGrid = false;
    private boolean fullscreen = true;

    private boolean clientMode = true;
    private String clientAddress;
    private int clientPort;

    private boolean serverMode = false;
    private boolean publicServer =false;
    private String serverAddress;
    private int serverPort;


    public void run()
    {
        if(this.init())
        {
            this.loop();

            if(this.saveWorld)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                LocalDateTime now = LocalDateTime.now();
                WorldLoader.generate(this.world,AssetsManager.getWorldDirectory() + File.separator + dtf.format(now) + ".map");
            }


            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }

        glfwTerminate();
    }

    private boolean init()
    {
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
        if(fullscreen)
        {
            window = glfwCreateWindow(size.width, size.height,TITLE, monitor, NULL);
            if (window == NULL)
            {
                return false;
            }
            glfwSetWindowMonitor(window,monitor,0,0,size.width,size.height,this.glfwVidMode.refreshRate());
        }else{
            if(this.userSize.width != -1 && this.userSize.height != -1)
            {
                size = this.userSize;
            }

            window = glfwCreateWindow(size.width, size.height,TITLE, NULL, NULL);
            if (window == NULL)
            {
                return false;
            }
        }


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

        AssetsManager.Init();
        return true;
    }

    private void initGame()
    {
        Block.size = new Size(size.width/10,size.height/10);
        Player.size = Block.size;
        this.world  = new World();

        world.addPlayer(new Player());
        world.camera = new Camera(new Vector2((Main.size.width - Player.size.width)/2,(Main.size.height-Player.size.height)/2),size);

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

        String[] playerAnimations = {"left","right","top","bottom"};
        for (int i=0;i<playerAnimations.length;i++)
        {
            Texture[] t  =AssetsManager.loadTextureDirectory("player"+ File.separator + playerAnimations[i]).clone();
            gameTextures.put((gameTextures.size()-1)+i,t);

            world.players.get(0).textures.add(t);
        }



        world.players.get(0).texture = gameTextures.get(2)[0];

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

        this.initGame();

        Game game = new Game(this.window,this.world);
        game.start();

        Timer timer = new Timer();
        while ( !glfwWindowShouldClose(window) )
        {
            glfwPollEvents();
            float deltatime = timer.restart();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            this.world.render();

            //Vector2 b = game.getPositionItemCamera(game.mousePosition(),world.players.get(0).position,world.camera.position,Main.size.toVector2().div(2),Block.size);

            if(this.showGrid)
            {
                this.renderBlockGrid(game);
            }

            if(this.showFps)
            {
                System.out.print("\rFPS "+(1.0/deltatime));
            }
            glfwSwapBuffers(window);

        }
    }

    public void renderBlockGrid(Game game)
    {
        World world = game.world;
        for (int x = 0;x <= Main.size.width;x+= Block.size.width)
        {
            for (int y = 0;y <= Main.size.height;y+= Block.size.height) {
                Vector2 b = game.getPositionItemCamera(new Vector2(x, y), world.players.get(0).position, world.camera.position, Main.size.toVector2().div(2), Block.size);
                glBegin(GL_LINE_LOOP);
                glVertex2f(b.x, b.y);
                glVertex2f(Block.size.width + b.x, b.y);
                glVertex2f(Block.size.width + b.x, Block.size.height + b.y);
                glVertex2f(b.x, Block.size.height + b.y);
                glEnd();
            }
        }
    }

    public void parseArgs(String[] args)
    {
        for (String arg : args)
        {
            if(arg.startsWith("-"))
            {
                String tmp = arg;
                tmp = tmp.substring(1);
                tmp = tmp.toLowerCase();
                String[] data = tmp.split("=");

                if(data.length == 2)
                {
                    switch (data[0])
                    {
                        case "fps":
                            this.showFps = (data[1].equals("true") ? true : false);
                            break;
                        case "grid":
                            this.showGrid = (data[1].equals("true")  ? true : false);
                            break;
                            /*
                            // Bug  Camera + Block size

                        case "fullscreen":
                            this.fullscreen = (data[1].equals("true")  ? true : false);
                            break;
                        case "size":
                            String[] sizeData = data[1].split("x");
                            if(sizeData.length == 2) {
                                this.userSize = new Size((int)Float.parseFloat(sizeData[0]),(int)Float.parseFloat(sizeData[1]));

                            }
                            break;
                            */
                        case "save":
                            this.saveWorld = (data[1].equals("true") ? true : false);
                            break;
                        case "server":
                            this.serverMode = true;
                            this.clientMode = false;
                            this.serverAddress = data[1];
                            break;
                        case "client":
                            this.serverMode = false;
                            this.clientMode = true;
                            this.clientAddress = data[1];
                            break;
                        case "port":
                            this.clientPort = Integer.getInteger(data[1]);
                            this.serverPort = Integer.getInteger(data[1]);
                            break;
                        case "public":
                            this.publicServer = (data[1].equals("true") ? true : false);
                            break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
       Main main = new Main();
       main.parseArgs(args); // -grid=false -fullscreen=false -size=500x500
       main.run();
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
