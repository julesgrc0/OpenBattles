package com.julesG10.game;

import com.julesG10.game.map.World;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;

public class ClientManager<T> {

    protected T data;
    protected Client client;

    public ClientManager(T data, Client client)
    {
        this.data = data;
        this.client = client;
    }

    public boolean onData(GameNetworkCodes code, String[] parts)
    {
        return false;
    }
}
