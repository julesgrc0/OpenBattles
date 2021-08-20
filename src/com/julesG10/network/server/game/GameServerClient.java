package com.julesG10.network.server;

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

        Console.log("Run client");
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
        } catch (IOException e)
        {
            Console.log(e.getMessage());
        }
    }
}
