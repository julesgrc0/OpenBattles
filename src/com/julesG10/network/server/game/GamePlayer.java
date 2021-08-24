package com.julesG10.network.server.game;

import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Vector2;

public class GamePlayer {
    public Vector2 position;
    public long time;
    public int life;
    public int id;

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(GameNetworkCodes.PLAYER_UPDATE.ordinal());
        builder.append("|");
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
