package com.julesG10.network.clients;

import com.julesG10.game.ClientManager;
import com.julesG10.graphics.Texture;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Size;

public class TextureClient extends ClientManager<Texture> {

    public TextureClient(Texture data, Client client) {
        super(data, client);
    }

    @Override
    public boolean onData(GameNetworkCodes code, String[] parts) {
        super.onData(code, parts);

        if(code == GameNetworkCodes.TEXTURE_UPDATE)
        {
            Size s = new Size(Integer.getInteger(parts[0]),Integer.getInteger(parts[1]));
            this.data.setString(parts[2],s);

            return true;
        }

        return false;
    }
}
