package com.julesG10.network.server.game;

import com.julesG10.network.server.Server;
import com.julesG10.utils.Console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Server {

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
        while (this.active)
        {
            try {
                Socket socket = server.accept();
                Console.log("New Client");

                GameServerClient client = new GameServerClient(socket);
                client.start();
            } catch (IOException e)
            {
                Console.log(e.getMessage());
            }
        }

        return true;
    }
}
