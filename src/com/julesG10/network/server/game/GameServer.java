package com.julesG10.network.server.game;

import com.julesG10.network.server.Server;
import com.julesG10.utils.Console;
import com.julesG10.utils.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer extends Server {

    private List<Map.Entry<GamePlayer,GameServerClient>> clients = new ArrayList<>();

    public GameServer(boolean isPublic,int port)
    {
        super(isPublic,port);
    }

    @Override
    public boolean start() {
        super.start();

        this.active = true;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(this.address,this.port);
            server = new ServerSocket(inetSocketAddress.getPort(),50,inetSocketAddress.getAddress());
        } catch (IOException ignored) {
            return false;
        }

        Console.log("Server socket is running  "+server.getLocalSocketAddress());

        new Thread(() -> {
            while (active)
            {
                updateClients();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) { }
            }
        }).start();

        while (this.active)
        {
            try {
                Socket socket = server.accept();
                Console.log("New Client");

                this.addClient(socket);
                this.startLastClient();
                //this.updateClients();
            } catch (IOException e)
            {
                Console.log(e.getMessage());
            }
        }

        return true;
    }

    private void addClient(Socket socket)
    {
        GamePlayer player = new GamePlayer();
        GameServerClient client = new GameServerClient(socket);

        Map.Entry<GamePlayer, GameServerClient> entry = new Pair<>(player,client);
        clients.add(entry);
    }


    public void startLastClient()
    {
        clients.get(clients.size()-1).getValue().player = clients.get(clients.size()-1).getKey();
        clients.get(clients.size()-1).getValue().start();
    }

    public void updateClients()
    {
        clients.removeIf(entry -> !entry.getValue().isAlive() || !entry.getValue().isActive());

        for (Map.Entry<GamePlayer, GameServerClient> entry : clients)
        {
            entry.getValue().players = clients;
        }
    }
}
