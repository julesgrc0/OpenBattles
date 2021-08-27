package com.julesG10.network.server;

import com.julesG10.utils.Console;

import java.io.*;
import java.net.Socket;

public class ServerClient extends Thread {
    protected Socket client;
    protected DataInputStream reader;
    protected DataOutputStream writer;

    public ServerClient(Socket client) {
        super();
        this.client = client;

        try {
            this.writer = new DataOutputStream(this.client.getOutputStream());
            this.reader = new DataInputStream(this.client.getInputStream());
        } catch (IOException e) {
            Console.log(e.getMessage());
        }
    }

    protected boolean send(String data) {
        try {
            writer.writeBytes(data);
            writer.write(0);
            writer.flush();
            return true;
        } catch (IOException e) {
            Console.log(e.getMessage());
            this.close();
            return false;
        }
    }

    protected void close() {
        try {
            this.reader.close();
            this.writer.close();
            this.client.close();
        } catch (IOException e) {
            Console.log(e.getMessage());
        }
    }

    protected String receive() {
        try {

            int character;
            StringBuilder data = new StringBuilder();
            while ((character = reader.read()) != -1) {
                if (character == 0) {
                    break;
                }
                data.append((char) character);
            }
            return data.toString();
        } catch (IOException e) {
            Console.log(e.getMessage());
            this.close();
            return null;
        }
    }

    public void RunClient() {
    }

    @Override
    public void run() {
        super.run();
        this.RunClient();
    }
}