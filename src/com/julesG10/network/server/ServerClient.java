package com.julesG10.network.server;


import com.julesG10.utils.Console;

import java.io.*;
import java.net.Socket;

public class ServerClient extends  Thread {
    protected Socket client;

    public ServerClient(Socket client) {
        super();
        this.client = client;
    }

    public static ServerClient build(Socket client)
    {
        return new ServerClient(client);
    }

    protected boolean send(String data) {
        try {
            OutputStream output = this.client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output);
            writer.write(data);
            writer.flush();
            return true;
        } catch (IOException e) {
            Console.log(e.getMessage());
            return false;
        }
    }


    protected String recieve() {
        try {
            InputStream input = this.client.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();
            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            return data.toString();
        } catch (IOException e) {
            Console.log(e.getMessage());
            return null;
        }
    }

    public void RunClient() {}

    @Override
    public void run() {
        super.run();
        this.RunClient();
    }
}