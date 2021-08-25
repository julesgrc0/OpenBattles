package com.julesG10.network.server.game;

import com.julesG10.network.GameNetworkCodes;
import com.julesG10.network.server.ServerClient;
import com.julesG10.utils.Console;
import com.julesG10.utils.Pair;
import com.julesG10.utils.Vector2;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameServerClient extends ServerClient {
    public GamePlayer player;
    public int id;

    public List<Pair<GamePlayer,GameServerClient>> players = new ArrayList<>();

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
        this.init();

        while (this.client.isConnected())
        {
            String data = this.receive();
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

    public void init()
    {
        this.player.id = this.id;

        StringBuilder builder = new StringBuilder();
        builder.append(GameNetworkCodes.INIT.ordinal());
        builder.append("|");
        builder.append(this.id);

        this.send(builder.toString());
    }


    public void update()
    {
        this.send(GameNetworkCodes.PLAYER_CLEAR.ordinal()+"|");
        for (Map.Entry<GamePlayer, GameServerClient> entry : this.players)
        {
            GameServerClient client = entry.getValue();

            if(client.id != this.id)
            {
                this.send(entry.getKey().toString(GameNetworkCodes.PLAYER_ADD));
            }
        }
    }

    private boolean onData(String data)
    {
        String[] parts = data.split("\\|");

        GameNetworkCodes code = GameNetworkCodes.values()[(int)Float.parseFloat(parts[0])];
        if(code == GameNetworkCodes.EXIT)
        {
            return false;
        }else if(code == GameNetworkCodes.PLAYER_UPDATE)
        {
            int id = (int)Float.parseFloat(parts[1]);

            if(this.player.id == id)
            {
                Vector2 position = new Vector2(Float.parseFloat(parts[2]),Float.parseFloat(parts[3]));
                int life = (int)Float.parseFloat(parts[4]);
                long time = Long.parseLong(parts[5]);

                this.player.position = position;
                this.player.life = life;
                this.player.time = (long)((System.nanoTime() - time) * Math.pow(10,-6));

                for (Map.Entry<GamePlayer, GameServerClient> entry : this.players)
                {
                    if(entry.getValue().id != this.player.id)
                    {
                        this.send(this.player.toString(GameNetworkCodes.PLAYER_UPDATE));
                    }
                }
            }
        }else if(code == GameNetworkCodes.INIT)
        {
            int id = (int)Float.parseFloat(parts[1]);

            if(this.player.id == id)
            {
                Vector2 position = new Vector2(Float.parseFloat(parts[2]),Float.parseFloat(parts[3]));
                int life = (int)Float.parseFloat(parts[4]);
                long time = Long.parseLong(parts[5]);

                this.player.position = position;
                this.player.life = life;
                this.player.time = (long)((System.nanoTime() - time) * Math.pow(10,-6));
            }
        }

        return true;
    }
}
