package com.julesG10.network.server;

import java.io.IOException;
import java.net.Socket;

public class GameServer extends ServerClient {
    public GameServer(Socket client) {
        super(client);
    }

    @Override
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
