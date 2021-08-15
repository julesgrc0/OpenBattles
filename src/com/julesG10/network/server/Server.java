package com.julesG10.network.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private Socket socket;
    private ServerSocket server;
    public boolean active = false;

    public Server(boolean isPublic,int port)
    {
        this.port = port;
    }

    public boolean start()
    {
        this.active = true;
        try {
            server = new ServerSocket(this.port);
        } catch (IOException ignored) {
           return false;
        }

        while (this.active)
        {
            try {
                socket = server.accept();
                new ServerClient(socket).start();
            } catch (IOException ignored) {}
        }

        return true;
    }
}
