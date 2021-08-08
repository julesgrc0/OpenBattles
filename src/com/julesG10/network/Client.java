package com.julesG10.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class NetClient {

    public String adress;
    public int port;
    public Socket client;

    public NetClient(String address, int port) {
        this.adress = address;
        this.port = port;
    }

    public boolean connect() {
        try {
            this.client = new Socket(this.adress, this.port);
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    public String recieve() {
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

    public boolean send(String data) {
        try {
            OutputStream output = this.client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output);
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}