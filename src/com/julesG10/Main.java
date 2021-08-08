package com.julesG10;

import com.julesG10.graphics.Texture;
import com.julesG10.utils.Size;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

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
        }

        glfwTerminate();
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


        Texture[] test = new Texture[]{
                new Texture("C:/jules-dev/release/Tamagotchi/Tamagotchi/x64/Release/assets/backgrounds/ground_0.png"),
                new Texture("C:/jules-dev/release/Tamagotchi/Tamagotchi/x64/Release/assets/backgrounds/ground_1.png"),
                new Texture("C:/jules-dev/release/Tamagotchi/Tamagotchi/x64/Release/assets/backgrounds/ground_2.png")
        };
        int i=0;
        float time=0;
        float angle = 0;
        int tileCountH  =  10;//this.size.height/test[0].getSize().height;
        int tileCountW = 12;//this.size.width/test[0].getSize().width;
        Size tileSize = new Size(this.size.width/tileCountW,this.size.height/tileCountH);

        Timer timer = new Timer();
        float cam_x = 0,cam_y = 0;
        while ( !glfwWindowShouldClose(window) )
        {
            glfwPollEvents();
            float deltatime = timer.restart();
            time += deltatime*1000;


            if(glfwGetKey(window,GLFW_KEY_LEFT) == GLFW_TRUE)
            {
                cam_x -= deltatime * 80;
            }

            if(glfwGetKey(window,GLFW_KEY_RIGHT) == GLFW_TRUE)
            {
                cam_x += deltatime * 80;
            }

            if(glfwGetKey(window,GLFW_KEY_UP) == GLFW_TRUE)
            {
                cam_y -= deltatime * 80;
            }

            if(glfwGetKey(window,GLFW_KEY_DOWN) == GLFW_TRUE)
            {
                cam_y += deltatime * 80;
            }
            angle+=deltatime * 10f;
            if(angle >= 360)
            {
                angle = 0;
            }

            if(time > 100)
            {
               
                time=0;
                i++;
                if(i>=test.length)
                {
                    i=0;
                }
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for (int x=0;x<tileCountW;x++)
            {
                for(int y=0;y<tileCountH;y++)
                {

                    test[i].render(new Vector2(x*tileSize.width + cam_x,y*tileSize.height+cam_y),tileSize,angle);
                }
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
