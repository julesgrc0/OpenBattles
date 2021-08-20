package com.julesG10.network.server;

import com.julesG10.utils.Console;

import java.io.*;
import java.net.*;

public class Server<T extends ServerClient> {
    private int port;
    private String address;
    private ServerSocket server;
    public boolean active = false;

    public Server(boolean isPublic,int port)
    {
        if(isPublic)
        {
            this.address = "";
        }else{
            try {
                this.address = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                Console.log("Fail to get localhost address");
                this.address = "127.0.0.1";
            }
        }
        Console.log("Set server address to "+this.address);
        this.port = port;
    }

    public boolean start()
    {
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
                Console.log("New client");
                T.build(socket).start();
            } catch (IOException e)
            {
                Console.log(e.getMessage());
            }
        }

        return true;
    }
}
