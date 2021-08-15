package com.julesG10.network;


import java.io.*;
import java.net.Socket;

class ServerClient extends  Thread {
    private Socket client;

    public ServerClient(Socket client) {
        super();
        this.client = client;
    }

    protected boolean send(String data) {
        try {
            OutputStream output = this.client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output);
            writer.write(data);
            writer.flush();
            return true;
        } catch (IOException e) {
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
        } catch (IOException ignored) {
            return null;
        }
    }

    public void run() {
        super.run();
    }
}