package com.julesG10.network.clients;

import com.julesG10.game.ClientManager;
import com.julesG10.game.map.Block;
import com.julesG10.game.map.BlockType;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChunkClient extends ClientManager<World> {

    private World world;

    public ChunkClient(World world, Client client) {
        super(world, client);
        this.world = world;
    }

    @Override
    public boolean onData(GameNetworkCodes code, String[] parts) {
        super.onData(code, parts);

        if(code == GameNetworkCodes.MAP_UPDATE)
        {
            Vector2 position = new Vector2(Float.parseFloat(parts[0]),Float.parseFloat(parts[1]));
            Chunk tmp = new Chunk(position);

            parts =Arrays.copyOfRange(parts,2,parts.length);
            List<String> partsTmp = new ArrayList<>(Arrays.asList(parts));

            for (Block block : tmp.blocks)
            {
                for(int i = 0;i<partsTmp.size();i++)
                {
                    String strBlock  = partsTmp.get(i);
                    String[] strBlockParts = strBlock.split(";");
                    Vector2 pos = new Vector2(Float.parseFloat(strBlockParts[0]),Float.parseFloat(strBlockParts[1]));

                    if (block.position.equal(pos)) {

                        block.type = BlockType.values()[Integer.getInteger(strBlockParts[2])];
                        block.texture_index = 0;

                        partsTmp.remove(i);
                        break;
                    }
                }
            }
            return true;
        }

        return false;
    }
}
