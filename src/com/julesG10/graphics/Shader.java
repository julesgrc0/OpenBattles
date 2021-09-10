package com.julesG10.graphics;

import com.julesG10.utils.Console;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int program;
    private int vertexShader;
    private int fragmentShader;
    private boolean valid = true;


    public String load(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null)
            {
                builder.append(line).append('\n');
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            Console.log(e.getMessage());
        }

        return null;
    }

    public Shader(String vertexPath,String fragmentPath)
    {
        this.program = glCreateProgram();

        this.vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(this.vertexShader,this.load(vertexPath));
        glCompileShader(this.vertexShader);

        if(glGetShaderi(this.vertexShader,GL_COMPILE_STATUS) != GL_TRUE)
        {
            Console.log(glGetShaderInfoLog(this.vertexShader));
            this.valid = false;
        }

        this.fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(this.fragmentShader,this.load(fragmentPath));
        glCompileShader(this.fragmentShader);

        if(glGetShaderi(this.fragmentShader,GL_COMPILE_STATUS) != GL_TRUE)
        {
            Console.log(glGetShaderInfoLog(this.fragmentShader));
            this.valid = false;
        }

        glAttachShader(this.program,this.vertexShader);
        glAttachShader(this.program,this.fragmentShader);

        glBindAttribLocation(this.program, 0 ,"vertices");

        glLinkProgram(this.program);
        if(glGetProgrami(this.program,GL_LINK_STATUS) != GL_TRUE)
        {
            Console.log(glGetProgramInfoLog(this.program));
            this.valid = false;
        }
        glValidateProgram(this.program);
        if(glGetProgrami(this.program,GL_VALIDATE_STATUS) != GL_TRUE)
        {
            Console.log(glGetProgramInfoLog(this.program));
            this.valid = false;
        }
    }

    public void setUniform(String name,int value)
    {
        int location = glGetUniformLocation(this.program,name);
        if(location != -1)
        {
            glUniform1i(location,value);
        }
    }

    public void bind()
    {
        glUseProgram(this.program);
    }

    public boolean isValid()
    {
        return this.valid;
    }
}
