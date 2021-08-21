package com.julesG10.network;

import com.julesG10.utils.Console;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    public String hostname;
    public int port;
    public Socket client = new Socket();

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public boolean connect(int timeout) {
        try {
            this.client.connect(new InetSocketAddress(this.hostname, this.port), timeout);
            return true;
        } catch (IOException e) {
            Console.log(e.getMessage());
            return false;
        }
    }

    public String recieve() {
        if (this.client.isConnected()) {
            try {
                DataInputStream reader = new DataInputStream(this.client.getInputStream());

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
        if (this.client.isConnected()) {
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
        return false;
    }
}