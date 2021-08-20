package com.julesG10.network.server.game;

import com.julesG10.network.server.ServerClient;
import com.julesG10.utils.Console;

import java.io.IOException;
import java.net.Socket;

public class GameServerClient extends ServerClient {
    public GameServerClient(Socket client) {
        super(client);
    }

    @Override
    public void RunClient() {
        super.RunClient();

        try {
            while (!client.isClosed() && client.isConnected())
            {
                String data = this.recieve();
                Console.log(data);
                if(data == null)
                {
                    this.client.close();
                    break;
                }
                /*else{
                    if(!this.send(data))
                    {
                        break;
                    }
                }*/
            }
        } catch (IOException e)
        {
            Console.log(e.getMessage());
        }
    }
}
