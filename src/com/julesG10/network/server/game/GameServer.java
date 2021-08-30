package com.julesG10.network.server.game;

import com.julesG10.network.server.Server;
import com.julesG10.network.server.ServerClient;
import com.julesG10.utils.Console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Server {

    public int clientId=0;

    public GameServer(boolean isPublic, int port) {
        super(isPublic, port);
    }

    @Override
    public boolean start() {
        super.start();

        this.active = true;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(this.address, this.port);
            server = new ServerSocket(inetSocketAddress.getPort(), 50, inetSocketAddress.getAddress());
        } catch (IOException ignored) {
            return false;
        }

        Console.log("Server socket is running  " + server.getLocalSocketAddress());

        Thread updateThread = new Thread(() -> {
            while (active)
            {
                updateClients();
            }
        });

        updateThread.start();

        while (this.active) {
            try {
                Socket socket = server.accept();

                GameServerClient client = new GameServerClient(socket);
                client.id =  this.clientId++;
                this.newClient();

                clientList.add(client);
                client.start();
                Console.log("Open Thread "+client.getName()+"/"+client.getId());

                Console.log("New Client");
            } catch (IOException e) {
                Console.log(e.getMessage());
            }
        }

        return true;
    }

    public void updateClients()
    {
        for (int i = 0;i<clientList.size();i++) {

            GameServerClient client = (GameServerClient) this.clientList.get(i);

            if (client.isClosed()) {
                Console.log("Close Thread "+client.getName()+"/"+client.getId());
                int id = client.id;
                clientList.remove(i);
                i--;

                removeClient(id);
            }else {
                client.update(this);
            }
        }
    }

    public void newClient()
    {
        for (ServerClient serverClient : clientList) {
            GameServerClient client = (GameServerClient) serverClient;
            client.add(this, clientId);
        }
    }

    public void removeClient(int clientId)
    {
        for (ServerClient serverClient : clientList) {
            GameServerClient client = (GameServerClient) serverClient;
            client.rem(this, clientId);
        }
    }

}
