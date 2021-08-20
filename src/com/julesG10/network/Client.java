package com.julesG10.network;

import com.julesG10.utils.Console;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    public String hostname;
    public int port;
    public Socket client;
    public boolean connected = false;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public boolean connect(int timeout) {
        try {
            this.client = new Socket();
            this.client.connect(new InetSocketAddress(this.hostname, this.port), timeout);
            this.connected = true;
            return true;
        } catch (IOException e) {
            Console.log(e.getMessage());
            return false;
        }
    }

    public String recieve() {
        if (this.connected) {
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
        return null;
    }

    public boolean send(String data) {
        if (this.connected) {
            try {
                OutputStream output = this.client.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(output);
                writer.write(data);
                writer.close();
                return true;
            } catch (IOException e) {
                Console.log(e.getMessage());
                return false;
            }
        }
        return false;
    }
}