package com.julesG10.graphics;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int id;


    public static String load(String path)
    {
        if(path.endsWith(".glsl") || path.endsWith(".vert") || path.endsWith(".frag"))
        {
            try {
                return  Files.readAllLines(Path.of(path)).toString();
            } catch (IOException ignored) {}
        }
        return null;
    }

    public Shader(String vertexSource,String fragmentSource)
    {
        int vertexShader = createShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentSource);
        if(vertexShader == -1 || fragmentShader == -1)
        {
            return;
        }

        id = glCreateProgram();
        glAttachShader(id, vertexShader);
        glAttachShader(id, fragmentShader);

        glLinkProgram(id);
        int comp = glGetProgrami(id, GL_LINK_STATUS);
        if(comp == GL_FALSE)
        {
            return;
        }
        /*glDetachShader(id, vertexShader);
        glDetachShader(id, fragmentShader);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        */
    }


    private int createShader(int type,String source)
    {
        int shader = glCreateShader(type);

        if (shader == 0)
        {
            return -1;
        }

        glShaderSource(shader, source);
        glCompileShader(shader);

        int comp = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (comp == GL_FALSE)
        {
            return -1;
        }

        return shader;
    }
}
