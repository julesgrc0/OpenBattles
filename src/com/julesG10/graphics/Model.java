package com.julesG10.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;



public class Model {
    private int count = 0;
    private int v_id = 0;
    private int t_id = 0;
    private int i_id = 0;

    public Model(float[] vertices,float[] texture_coord,int[] indices)
    {
        this.count = indices.length;

        this.v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER,this.createBuffer(vertices),GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        this.t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER,this.createBuffer(texture_coord),GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        this.i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    public void render()
    {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexPointer(3,GL_FLOAT,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        glTexCoordPointer(2,GL_FLOAT,0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        //glDrawArrays(GL_TRIANGLES,0,count);
        glDrawElements(GL_TRIANGLES,count,GL_UNSIGNED_INT,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private FloatBuffer createBuffer(float[] data)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}
