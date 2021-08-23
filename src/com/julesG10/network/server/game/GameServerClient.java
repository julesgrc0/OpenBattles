package com.julesG10.network.server.game;

import com.julesG10.network.server.ServerClient;
import com.julesG10.utils.Console;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameServerClient extends ServerClient {
    public GamePlayer player;
    public List<Map.Entry<GamePlayer,GameServerClient>> players = new ArrayList<>();

    public GameServerClient(Socket client) {
        super(client);
    }

    public boolean isActive()
    {
        return this.client.isConnected();
    }

    @Override
    public void RunClient() {
        super.RunClient();

        while (this.client.isConnected())
        {
            String data = this.recieve();
            if(data == null)
            {
                Console.log("Client Leave");
                break;
            }
            else if(!this.onData(data))
            {
                break;
            }
        }
        this.close();
    }

    private boolean onData(String data)
    {
        byte[] bytes = data.getBytes();


        return true;
    }
}
