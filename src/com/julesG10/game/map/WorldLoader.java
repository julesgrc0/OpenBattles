package com.julesG10.game.map;

import com.julesG10.utils.Vector2;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class WorldLoader {

    public WorldLoader()
    {

    }

    public World load(String filename)
    {
        try {
            byte[] content = Files.readAllBytes(Paths.get(filename));
            return this.load(content);
        } catch (IOException e) {
            return null;
        }
    }

    public World load(byte[] bytes)
    {
        String content = bytes.toString();
        String[] lines = content.split("\n");
        World world = new World();

        final String[] lineIds = {"[CHUNK]","[CAMERA]"};

        for (String line : lines)
        {
            if(line.startsWith(lineIds[0]))
            {
                line = line.substring(lineIds[1].length());
                String[] data = line.split(";");

                Chunk chunk = new Chunk(new Vector2(Float.parseFloat(data[0]),Float.parseFloat(data[1])));
                data = Arrays.copyOfRange(data, 2, data.length);

                for (int i= 0;i<data.length;i++)
                {
                    String[] blockData = data[i].split("\\|");
                    Block block = new Block();
                    block.type = BlockType.values()[Integer.getInteger(blockData[0])];
                    Vector2 blockPosition = new Vector2(Float.parseFloat(blockData[1]),Float.parseFloat(blockData[2]));

                    chunk.setBlock(blockPosition,block);
                }
            }else if(line.startsWith(lineIds[1]))
            {
                line = line.substring(lineIds[1].length());
                String[] data = line.split(";");

                world.camera.active = true;
                world.camera.position = new Vector2(Float.parseFloat(data[0]),Float.parseFloat(data[1]));
            }
        }
        return world;
    }

    public void generate(World world,String path)
    {
        try {
            FileWriter writer = new FileWriter(path);
            for (Chunk c : world.chunks)
            {
                writer.write("[CHUNK]"+c.position.x+";"+c.position.y);
                for (Block b : c.blocks)
                {
                writer.write(";"+b.type+"|"+b.position.x+"|"+b.position.y);
                }
                writer.write("\n");
            }

            writer.write("[CAMERA]"+world.camera.position.x+";"+world.camera.position.y);
            writer.close();
        } catch (IOException e) {}
    }
}
