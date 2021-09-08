package com.julesG10.graphics;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int id;

    public Shader(String vertexSource,String fragmentSource)
    {
        int vertexShader = createShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentSource);

        id = glCreateProgram();
        glAttachShader(id, vertexShader);
        glAttachShader(id, fragmentShader);

        glLinkProgram(id);

        IntBuffer buffer = BufferUtils.createIntBuffer(0);
        glGetProgramiv(id, GL_LINK_STATUS, buffer);

        glDetachShader(id, vertexShader);
        glDetachShader(id, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private int createShader(int type,String src)
    {
        int id = glCreateShader(type);

        glShaderSource(id,  src);
        glCompileShader(id);

        IntBuffer buffer = BufferUtils.createIntBuffer(0);
        glGetShaderiv(id, GL_COMPILE_STATUS,buffer);

        return id;
    }
}
