package com.julesG10.network.server;

import com.julesG10.utils.Console;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Server<T extends ServerClient> {
    protected int port;
    protected String address;
    protected ServerSocket server;
    protected List<T> clientList = new ArrayList<>();
    public boolean active = false;


    public Server(boolean isPublic, int port) {
        if (isPublic) {
            this.address = "";
        } else {
            try {
                this.address = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                Console.log("Fail to get IPv4 address");
                this.address = "127.0.0.1";
            }
        }
        Console.log("Set server address to " + this.address);
        this.port = port;
    }

    public boolean start() {
        return false;
    }

    public boolean sendClients(String data) {
        for (T client : clientList) {

            if (!client.send(data)) {
                return false;
            }
        }

        return true;
    }

    public boolean sendClientIf(SendIf<T> condition,String data)
    {
        for (T client : clientList) {

            if(condition.sendIf(client))
            {
                if (!client.send(data)) {
                    return false;
                }
            }
        }

        return true;
    }
}
