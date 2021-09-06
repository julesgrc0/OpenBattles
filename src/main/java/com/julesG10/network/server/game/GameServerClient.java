package com.julesG10.network.server.game;

import com.julesG10.game.player.Player;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.network.server.ServerClient;
import com.julesG10.utils.Console;
import com.julesG10.utils.Pair;
import com.julesG10.utils.Vector2;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameServerClient extends ServerClient {
    public boolean hasUpdate = false;
    public String updateContent;
    public int id;

    public GameServerClient(Socket client) {
        super(client);
    }

    public boolean Active() {
        return this.client.isConnected() && this.isAlive();
    }

    @Override
    public void RunClient() {
        super.RunClient();

        Player pTmp = new Player();
        pTmp.id = this.id;
        this.send(GameNetworkCodes.PLAYER_INIT.ordinal() + "|" + pTmp.toString());

        while (this.Active()) {
            String data = this.receive();
            if (data == null) {
                Console.log("Client Leave");
                break;
            } else if (!this.onData(data)) {
                break;
            }
        }
        this.close();
    }


    private boolean onData(String data) {
        if (data == null) {
            return false;
        }

        String[] parts = data.split("\\|");
        GameNetworkCodes code = GameNetworkCodes.values()[Integer.parseInt(parts[0])];
        parts = Arrays.copyOfRange(parts, 1, parts.length);

        if (code == GameNetworkCodes.PLAYER_UPDATE) {
            this.hasUpdate = true;
            this.updateContent = data;
        } else if (code == GameNetworkCodes.PING) {
            this.send(GameNetworkCodes.PING.ordinal() + "|" + System.nanoTime());
        }
        return true;
    }

    public void update(GameServer srv) {
        if (this.hasUpdate && this.updateContent != null) {
            srv.sendClients(this.updateContent);
            this.hasUpdate = false;
            this.updateContent = null;
        }
    }


    public void add(GameServer srv, int clientId) {
        Player pTmp = new Player();
        pTmp.id = clientId;

        /*srv.sendClientIf((GameServerClient c)->{
            if(c.id != clientId)
            {
                return true;
            }else{
                return false;
            }
        },GameNetworkCodes.PLAYER_ADD.ordinal() + "|" + pTmp.toString());*/

        srv.sendClients(GameNetworkCodes.PLAYER_ADD.ordinal() + "|" + pTmp.toString());
    }

    public void rem(GameServer srv, int clientId) {
        srv.sendClients(GameNetworkCodes.PLAYER_REMOVE.ordinal() + "|" + clientId);
    }

    public boolean isClosed() {
        return this.client.isClosed();
    }

}
