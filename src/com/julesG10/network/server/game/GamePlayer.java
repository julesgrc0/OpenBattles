package com.julesG10.network.server.game;

import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Vector2;

public class GamePlayer {
    public Vector2 position;
    public long time;
    public int life;

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(GameNetworkCodes.PLAYER_UPDATE);
        builder.append(position.x);
        builder.append(position.y);
        builder.append(life);
        builder.append(time);
        return builder.toString();
    }
}
