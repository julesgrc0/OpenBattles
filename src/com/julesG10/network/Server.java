package com.julesG10.network;

import java.io.*;
import java.net.InetAddress;
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

class ServerClient extends  Thread {
    private Socket client;

    public ServerClient(Socket client) {
        super();
        this.client = client;
    }

    private boolean send(String data)
    {
        try {
            OutputStream output = this.client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output);
            writer.write(data);
            writer.flush();
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }


    private String recieve() {
        try {
            InputStream input = this.client.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();
            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            return data.toString();
        } catch (IOException ignored) {
            return null;
        }
    }

    public void run() {
        super.run();
        try {
            while (true) {
                String data = this.recieve();
                if(data == null)
                {
                    this.client.close();
                    break;
                }else{
                    if(!this.send(data))
                    {
                        break;
                    }
                }
            }
        } catch (IOException ignored) {}
    }
}