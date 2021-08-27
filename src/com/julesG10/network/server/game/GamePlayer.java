package com.julesG10.network.server.game;

import com.julesG10.utils.Vector2;

public class GamePlayer {
    public Vector2 position = new Vector2(0, 0);
    public long time = 0;
    public int life = 0;
    public int id = 0;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append("|");
        builder.append(position.x);
        builder.append("|");
        builder.append(position.y);
        builder.append("|");
        builder.append(life);
        builder.append("|");
        builder.append(time);
        return builder.toString();
    }
}
