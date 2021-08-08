package com.julesG10.graphics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Shader {

    private boolean valid = false;

    public Shader(String filename,boolean isVertex)
    {
        Path path = new File(filename).toPath();
        try {
            List<String> content = Files.readAllLines(path);

            this.valid = true;
        } catch (IOException e) {
            this.valid = false;
        }
    }

    public boolean isValid()
    {
        return this.valid;
    }
}
