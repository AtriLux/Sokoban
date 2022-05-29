package game.sokoban.clientServer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;

public class Server {
    ServerSocket server;
    UUID player; // waiting player
    Socket playerSocket; // waiting player's socket
    int countPlayers = 0; // count of waiting player

    {
        try {
            server = new ServerSocket(1984);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server() throws IOException {
        System.out.println("Server started");
        Map<UUID, Socket> sockets = new HashMap<>();

        Thread socketThread = new Thread(() -> {
            while (true) {
                try {
                    Socket newSocket = server.accept();
                    UUID newSocketId = UUID.randomUUID();

                    synchronized (sockets) {
                        sockets.put(newSocketId, newSocket);
                    }

                    synchronized (sockets) {
                        sockets.forEach((uuid, socket) -> {
                            try {
                                //send yourself your uuid
                                OutputStream os = socket.getOutputStream();
                                if (uuid == newSocketId) os.write(new Message(newSocketId, 0).getBytes());
                                os.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                                synchronized (sockets) {
                                    sockets.remove(uuid);
                                }
                            }
                        });
                    }

                    System.out.println("Accepted " + newSocketId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        socketThread.start();

        while (true) {
            synchronized (sockets) {
                sockets.forEach((uuid, socket) -> {
                    try {
                        BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
                        List<Byte> byteList = new ArrayList<>();
                        while (reader.available() > 0) {
                            byteList.add((byte) reader.read());
                        }
                        if (byteList.size() != 0) {
                            byte[] bytes = new byte[byteList.size()];
                            for (int i = 0; i < byteList.size(); i++) {
                                bytes[i] = byteList.get(i);
                            }
                                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                                int type = buffer.getInt();
                                byte[] newArray;
                                OutputStream os;
                                ByteBuffer buf;
                                switch (type) {
                                    case 1:
                                        System.out.println("Player " + uuid + " connected");
                                        if (player == null) {
                                            player = uuid;
                                            playerSocket = socket;
                                        } else {
                                            os = socket.getOutputStream();
                                            os.write(new Message(player, 1).getBytes());
                                            os.flush();
                                            os = playerSocket.getOutputStream();
                                            os.write(new Message(uuid, 1).getBytes());
                                            os.flush();
                                            player = null;
                                            playerSocket = null;
                                        }
                                        break;

                                    case 2:
                                        System.out.println("Player " + uuid + " ready");
                                        countPlayers++;
                                        newArray = new byte[buffer.array().length - buffer.position()];
                                        buffer.get(newArray, buffer.arrayOffset(), newArray.length);
                                        Message message = new Message(newArray);
                                        System.out.println("Opponent: " + message.getId());
                                        if (countPlayers == 2) {
                                            buf = ByteBuffer.allocate(Integer.BYTES * 2);
                                            buf.putInt(2);
                                            buf.putInt((int) (Math.random() * 3 + 1));
                                            os = socket.getOutputStream();
                                            os.write(buf.array());
                                            os.flush();
                                            os = sockets.get(message.getId()).getOutputStream();
                                            os.write(buf.array());
                                            os.flush();
                                        }
                                    break;

                                    case 3:
                                        System.out.println("Player " + uuid + " win");
                                        newArray = new byte[buffer.array().length - buffer.position()];
                                        buffer.get(newArray, buffer.arrayOffset(), newArray.length);
                                        Message loser = new Message(newArray);
                                        os = sockets.get(loser.getId()).getOutputStream();
                                        buf = ByteBuffer.allocate(Integer.BYTES);
                                        buf.putInt(3);
                                        os.write(buf.array());
                                        os.flush();
                                        break;

                                    case 4:
                                        System.out.println("Player " + uuid + " give up");
                                        newArray = new byte[buffer.array().length - buffer.position()];
                                        buffer.get(newArray, buffer.arrayOffset(), newArray.length);
                                        Message opponent = new Message(newArray);
                                        os = sockets.get(opponent.getId()).getOutputStream();
                                        buf = ByteBuffer.allocate(Integer.BYTES);
                                        buf.putInt(4);
                                        os.write(buf.array());
                                        os.flush();
                                        break;
                                }
                            }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
