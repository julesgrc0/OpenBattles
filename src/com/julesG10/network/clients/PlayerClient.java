package com.julesG10.network.clients;

import com.julesG10.game.ClientManager;
import com.julesG10.game.map.World;
import com.julesG10.game.player.Player;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Vector2;

public class PlayerClient extends ClientManager<World> {
    private World world;

    public PlayerClient(World world, Client client)
    {
        super(world,client);
        this.world = world;
    }

    @Override
    public boolean onData(GameNetworkCodes code, String[] parts)
    {
        super.onData(code,parts);

        if(code == GameNetworkCodes.PLAYER_INIT)
        {
            this.world.players.get(0).setString(String.join("|",parts));
            return true;
        }
        else if(code == GameNetworkCodes.PLAYER_UPDATE)
        {
            int id = Integer.parseInt(parts[0]);
            if(id == this.world.players.get(0).id)
            {
                return true;
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

            return true;
        }
        else if(code == GameNetworkCodes.PLAYER_ADD)
        {
            int id = Integer.parseInt(parts[0]);
            if(id == this.world.players.get(0).id)
            {
                return true;
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

            return true;
        }
        else if(code == GameNetworkCodes.PLAYER_REMOVE)
        {
            int id = Integer.parseInt(parts[0]);
            if(id == this.world.players.get(0).id)
            {
                return true;
            }
            this.world.players.removeIf(player -> player.id == id);

            return true;
        }

        return false;
    }
}
