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


    protected boolean send(String data) {
        try {
            DataOutputStream writer = new DataOutputStream(this.client.getOutputStream());

            writer.writeBytes(data);
            writer.flush();
            return true;
        } catch (IOException e) {
            Console.log(e.getMessage());
            return false;
        }
    }


    protected String recieve() {
        try {
            DataInputStream reader = new DataInputStream(this.client.getInputStream());

            int character;
            StringBuilder data = new StringBuilder();
            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            return data.toString(); //reader.readAllBytes().toString();
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