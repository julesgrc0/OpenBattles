package com.julesG10;

import com.julesG10.game.map.BlockType;
import com.julesG10.game.map.Chunk;
import com.julesG10.game.map.World;
import com.julesG10.game.player.Player;
import com.julesG10.game.player.PlayerDirection;
import com.julesG10.network.Client;
import com.julesG10.network.GameNetworkCodes;
import com.julesG10.utils.Console;
import com.julesG10.utils.Timer;
import com.julesG10.utils.Vector2;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends Thread {

    private long window;

    private boolean clientActive = true;
    public Client client;
    public World world;

    public Game(long window, World world, Client client) {
        super();
        this.window = window;
        this.world = world;
        this.client = client;
    }

    public void run() {
        super.run();
        while (!client.connect(5000)) {
        }
        Console.log("Client connected");

        Thread clientThread = new Thread(() -> {
            while (clientActive) {
                String data = client.receive();
                if (data != null) {
                    this.onData(data);
                } else {
                    clientActive = false;
                    int id = world.players.get(0).id;
                    world.players.removeIf(player -> player.id != id);
                }
            }
        });
        clientThread.start();

        Timer timer = new Timer();
        while (!glfwWindowShouldClose(window)) {
            float deltatime = timer.restart();
            glfwPollEvents();
            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_TRUE) {
                this.clientActive = false;
                glfwSetWindowShouldClose(window, true);
                break;
            }

            this.updateMove(deltatime);
        }
    }

    public void onData(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 1) {
            return;
        }

        int codeindex;
        try {
            codeindex = (int) Float.parseFloat(parts[0]);
            int len = GameNetworkCodes.values().length;
            if (codeindex >= len || codeindex < 0) {
                return;
            }
        } catch (NumberFormatException e) {
            return;
        }

        GameNetworkCodes code = GameNetworkCodes.values()[codeindex];
        parts = Arrays.copyOfRange(parts, 1, parts.length);
        if (parts.length == 0) {
            return;
        }

        if (code == GameNetworkCodes.MAP_UPDATE) {
            Vector2 chunk_position = new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
            for (Chunk c : this.world.chunks) {
                if (c.position.equal(chunk_position)) {
                    for (int i = 2; i < parts.length; i++) {
                        int index = (int) Float.parseFloat(parts[0]);
                        BlockType type = BlockType.values()[index];

                        if (c.blocks[0].type != type) {
                            c.blocks[0].texture_index = 0;
                            c.blocks[0].type = type;
                        }
                    }
                }
            }
        } else if (code == GameNetworkCodes.PLAYER_ADD) {
            this.addPlayerFromParts(parts);
        } else if (code == GameNetworkCodes.PLAYER_CLEAR) {
            int id = this.world.players.get(0).id;
            this.world.players.removeIf(player -> player.id != id);
        } else if (code == GameNetworkCodes.PLAYER_UPDATE) {
            int mainId = this.world.players.get(0).id;

            int id = (int) Float.parseFloat(parts[0]);
            if (id == mainId) {
                return;
            }
            boolean find = false;
            for (Player p : this.world.players) {
                if (p.id == id) {
                    find = true;
                    p.position = new Vector2(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
                    break;
                }
            }

            if (!find) {
                this.addPlayerFromParts(parts);
            }

        } else if (code == GameNetworkCodes.INIT) {
            this.world.players.get(0).id = (int) Float.parseFloat(parts[0]);
            this.sendPlayer(GameNetworkCodes.INIT);

        } else if (code == GameNetworkCodes.PLAYER_GENERAL_UPDATE) {
            int mainId = this.world.players.get(0).id;
            this.world.players.removeIf(player -> player.id != mainId);
            for (String pData : parts) {
                String[] pParts = pData.split(";");
                int id = (int) Float.parseFloat(pParts[2]);
                if (id != mainId) {
                    this.addPlayerFromParts(pParts);
                }

                /*
                 * int id = (int)Float.parseFloat(pParts[2]); boolean find = false;
                 * 
                 * for (Player p : this.world.players) { if(p.id == id) { find = true;
                 * p.position = new
                 * Vector2(Float.parseFloat(pParts[0]),Float.parseFloat(pParts[1])); break; } }
                 * 
                 * if(!find) { this.addFromParts(pParts); }
                 */
            }
        } else {
            Console.log("Unknow data  \"" + data + "\"");
        }
    }

    public void addPlayerFromParts(String[] parts) {
        Player player = new Player();
        player.position = new Vector2(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
        player.texture_index = 0;
        player.id = (int) Float.parseFloat(parts[0]);

        player.textures = this.world.players.get(0).textures;
        player.texture = this.world.players.get(0).texture;

        this.world.addPlayer(player);
    }

    public void sendPlayer(GameNetworkCodes code) {
        if (this.client.client.isConnected()) {
            StringBuilder builder = new StringBuilder();
            Player player = this.world.players.get(0);

            builder.append(code.ordinal());
            builder.append("|");
            builder.append(player.id);
            builder.append("|");
            builder.append(player.position.x);
            builder.append("|");
            builder.append(player.position.y);
            builder.append("|");
            builder.append(player.life);
            builder.append("|");
            builder.append(System.nanoTime());

            this.client.send(builder.toString());
        }
    }

    public void updateMove(float deltatime) {
        float add = deltatime * 400;
        float move = 0;
        boolean hasMove = false;
        Player player = this.world.players.get(0);

        if (glfwGetKey(this.window, GLFW_KEY_RIGHT) == GLFW_TRUE) {

            // this.world.camera.position.x -= add;
            player.position.x += add;
            player.changeDirection(PlayerDirection.RIGHT);
            move = add;
            hasMove = true;
        } else if (glfwGetKey(this.window, GLFW_KEY_LEFT) == GLFW_TRUE) {

            // this.world.camera.position.x += add;
            player.position.x -= add;
            player.changeDirection(PlayerDirection.LEFT);
            move = -add;
            hasMove = true;
        }

        if (glfwGetKey(this.window, GLFW_KEY_DOWN) == GLFW_TRUE) {
            if (move != 0) {
                player.position.x -= move / 2;
                // this.world.camera.position.x -= -move/2;
                add /= 2;
            }
            // this.world.camera.position.y -= add;
            player.position.y += add;
            player.changeDirection(PlayerDirection.BOTTOM);
            hasMove = true;
        } else if (glfwGetKey(this.window, GLFW_KEY_UP) == GLFW_TRUE) {
            if (move != 0) {
                player.position.x -= move / 2;
                // this.world.camera.position.x -= -move/2;
                add /= 2;

            }
            // this.world.camera.position.y += add;
            player.position.y -= add;
            player.changeDirection(PlayerDirection.TOP);
            hasMove = true;
        }

        if (this.world.camera.position.x + this.world.players.get(0).position.x != (Main.size.width - Player.size.width)
                / 2) {
            this.world.camera.position.x = (Main.size.width - Player.size.width) / 2
                    - this.world.players.get(0).position.x;
        }

        if (this.world.camera.position.y
                + this.world.players.get(0).position.y != (Main.size.height - Player.size.height) / 2) {
            this.world.camera.position.y = (Main.size.height - Player.size.height) / 2
                    - this.world.players.get(0).position.y;
        }

        if (hasMove) {
            player.update(deltatime);
            this.sendPlayer(GameNetworkCodes.PLAYER_UPDATE);
        }
    }
}
