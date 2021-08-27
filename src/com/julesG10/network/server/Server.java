package com.julesG10.network.server;

import com.julesG10.utils.Console;

import java.io.*;
import java.net.*;

public class Server {
    protected int port;
    protected String address;
    protected ServerSocket server;
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
}
