package com.julesG10.utils;

import java.nio.DoubleBuffer;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.BufferUtils;

public class CameraUtils {
    public static Vector2 mousePosition(long window) {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, posX, posY);
        return new Vector2((float) posX.get(), (float) posY.get());
    }

    public static Vector2 getPositionItemCamera(Vector2 vec, Vector2 gameItemPosition, Vector2 cameraPosition,
            Vector2 centerCamera, Size round) {
        vec = vec.add(gameItemPosition);
        vec = vec.min(centerCamera);
        vec = vec.roundTo(round.toVector2());
        return cameraPosition.add(vec);
    }
}