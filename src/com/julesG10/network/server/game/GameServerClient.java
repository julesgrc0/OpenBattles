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

        String data = this.recieve();
        while (data != null)
        {
            Console.log(data);
            data = this.recieve();
            /*if(data == null)
            {
                this.client.close();
                break;
            }
            else{
                if(!this.send(data))
                {
                    break;
                }
            }*/
        }
    }
}
