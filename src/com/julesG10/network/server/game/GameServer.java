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

    private List<Pair<GamePlayer,GameServerClient>> clients = new ArrayList<>();
    public int id = 0;
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

        Thread updateThread = new Thread(() -> {
            while (active)
            {
                checkClientsConnections();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) { }
            }
        });

        updateThread.start();

        while (this.active)
        {
            try {
                Socket socket = server.accept();
                Console.log("New Client");
                this.id++;
                this.addClient(socket);
                this.startLastClient();

                this.updateClients();
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

        Pair<GamePlayer, GameServerClient> pair = new Pair<>(player,client);
        clients.add(pair);
    }


    public void startLastClient()
    {
        GameServerClient client =  clients.get(clients.size()-1).getValue();
        client.player = clients.get(clients.size()-1).getKey();
        client.id = this.id;
        client.start();
    }

    public void checkClientsConnections()
    {
        clients.removeIf(entry -> {
            if(!entry.getValue().isAlive() || !entry.getValue().isActive())
            {
                entry.getValue().interrupt();
                return true;
            }else{
                return false;
            }
        });

       this.updateClients();
    }

    public void updateClients() {
        for (Pair<GamePlayer, GameServerClient> pair : clients) {
            pair.getValue().players = clients;
            pair.getValue().update();
        }
    }
}
