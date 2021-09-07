package com.julesG10.graphics;

import com.julesG10.utils.Size;
import com.julesG10.utils.Vector2;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class Texture {
    private Size size;
    private int id;
    private ByteBuffer pixels;
    private boolean valid = false;

    private float[] vertices = new float[] {
            -1,-1,0,
            0,1,1,
            1,-1,0
    };
   // private Model model = new Model(vertices);

    public Texture()
    {

    }

    public void setString(String pixels,Size size)
    {
        this.pixels = StandardCharsets.UTF_8.encode(pixels);
        this.size = size;
        this.id = glGenTextures();
        this.bind();

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.size.width, this.size.height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                this.pixels);
        glGenerateMipmap(GL_TEXTURE_2D);

        this.unbind();
        this.valid = true;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.size.width);
        builder.append("|");
        builder.append(this.size.height);
        builder.append("|");
        builder.append(StandardCharsets.UTF_8.decode(pixels));

        return builder.toString();
    }

    public Texture(String path) {
        BufferedImage buffer;
        try {
            buffer = ImageIO.read(new File(path));
            this.size = new Size(buffer.getWidth(), buffer.getHeight());

            int[] raw = new int[this.size.width * this.size.height * 4];
            raw = buffer.getRGB(0, 0, this.size.width, this.size.height, null, 0, this.size.width);
            this.pixels = BufferUtils.createByteBuffer(this.size.width * this.size.height * 4);

            for (int i = 0; i < this.size.width; i++) {
                for (int k = 0; k < this.size.height; k++) {
                    int pixel = raw[i * this.size.width + k];

                    pixels.put((byte) ((pixel >> 16) & 0xFF));
                    pixels.put((byte) ((pixel >> 8) & 0xFF));
                    pixels.put((byte) ((pixel & 0xFF)));
                    pixels.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            pixels.flip();

            this.id = glGenTextures();
            this.bind();

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.size.width, this.size.height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                    this.pixels);
            glGenerateMipmap(GL_TEXTURE_2D);

            this.unbind();
            this.valid = true;
        } catch (IOException ignored) {
            this.valid = false;
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void render(Vector2 position, Size size, float angle) {
        if (this.isValid()) {
            this.bind();
            glPushMatrix();
            glTranslatef(position.x, position.y, 0);
            glRotatef(angle, 0, 0, 1);
            glBegin(GL_QUADS);

            glTexCoord2f(0, 0);
            glVertex2f(0, 0);

            glTexCoord2f(1, 0);
            glVertex2f(size.width, 0);

            glTexCoord2f(1, 1);
            glVertex2f(size.width, size.height);

            glTexCoord2f(0, 1);
            glVertex2f(0, size.height);

            glEnd();
            glPopMatrix();

            this.unbind();

        }

    }

    public Size getSize() {
        return this.size;
    }

    public ByteBuffer getPixels() {
        return this.pixels;
    }

    public boolean isValid() {
        return this.valid;
    }

    public int getId() {
        return this.id;
    }
}
