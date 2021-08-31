package com.julesG10.game.player;

import com.julesG10.game.map.World;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Vector2;

public class PlayerClient {
    private World world;
    private Client client;

    public PlayerClient(World world, Client client)
    {
        this.world = world;
        this.client = client;
    }

    public void onData(GameNetworkCodes code, String[] parts)
    {
        if(code == GameNetworkCodes.PLAYER_INIT)
        {
            this.world.players.get(0).setString(String.join("|",parts));
        }
        else if(code == GameNetworkCodes.PLAYER_UPDATE)
        {
            int id = Integer.parseInt(parts[0]);
            if(id == this.world.players.get(0).id)
            {
                return;
            }

            boolean find = false;
            for (Player p : this.world.players)
            {
                if(p.id == id)
                {
                    find = true;
                    p.setString(String.join("|",parts));
                    break;
                }
            }


            if(!find)
            {
                Player player = new Player();
                player.textures = this.world.players.get(0).textures;
                player.texture = this.world.players.get(0).texture;
                player.setString(String.join("|",parts));

                this.world.players.add(player);
            }
        }
        else if(code == GameNetworkCodes.PLAYER_ADD)
        {
            int id = Integer.parseInt(parts[0]);
            if(id == this.world.players.get(0).id)
            {
                return;
            }

            boolean find = false;
            for (Player p : this.world.players)
            {
                if(p.id == id)
                {
                    find = true;
                    break;
                }
            }

            if(!find)
            {
                Player player = new Player();
                player.textures = this.world.players.get(0).textures;
                player.texture = this.world.players.get(0).texture;
                player.setString(String.join("|",parts));

                this.world.players.add(player);
            }
        }
        else if(code == GameNetworkCodes.PLAYER_REMOVE)
        {
            int id = Integer.parseInt(parts[0]);
            if(id == this.world.players.get(0).id)
            {
                return;
            }
            this.world.players.removeIf(player -> player.id == id);
        }
    }
}
