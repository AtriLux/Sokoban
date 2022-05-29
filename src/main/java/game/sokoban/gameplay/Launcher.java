package game.sokoban.gameplay;

import game.sokoban.clientServer.Message;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Launcher {

    private boolean isSocketActive;
    private Socket socket;
    private UUID id, idOpponent;
    private final LvlChanger lvlChanger;

    public Launcher(Stage stage) {
        startSocket();
        javafx.scene.text.Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/NineteenNinetyThree.ttf")).toExternalForm(), 16f);
        stage.setTitle("Sokoban v. 0.8");
        stage.setResizable(false);
        lvlChanger = new LvlChanger(stage, this);
        lvlChanger.startMenu();
        stage.show();
    }

    public Socket getSocket() { return socket; }
    public UUID getIdOpponent() { return idOpponent; }
    public boolean isSocketActive() { return isSocketActive; }

    public void startSocket() {
        try {
            socket = new Socket("localhost", 1984);
            isSocketActive = true;
        } catch (IOException e) {
            isSocketActive = false;
            e.printStackTrace();
        }

        Thread socketThread = new Thread(() -> {
            while (true) {
                try {
                    BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
                    List<Byte> bytes = new ArrayList<>();
                    while (reader.available() > 0) {
                        bytes.add((byte) reader.read());
                    }

                    if (bytes.size() > 0) {
                        byte[] byteArray = new byte[bytes.size()];
                        for (int i = 0; i < bytes.size(); i++) {
                            byteArray[i] = bytes.get(i);
                        }
                        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
                        int type = buffer.getInt();
                        byte[] newArray;
                        switch (type) {
                            case 0: //own uuid
                                newArray = new byte[buffer.array().length - buffer.position()];
                                buffer.get(newArray, buffer.arrayOffset(), newArray.length);
                                Message uuid = new Message(newArray);
                                id = uuid.getId();

                                System.out.println("own: " + id);
                                break;

                            case 1: //opponent uuid
                                newArray = new byte[buffer.array().length - buffer.position()];
                                buffer.get(newArray, buffer.arrayOffset(), newArray.length);
                                Message message = new Message(newArray);
                                idOpponent = message.getId();
                                lvlChanger.getMenuController().setOnlineOpponent();
                                System.out.println("opponent: " + message.getId());
                                break;

                            case 2: //all players ready
                                lvlChanger.getMenuController().setOnlineReady();
                                lvlChanger.reverseSingleGame();
                                lvlChanger.startGame(buffer.getInt());
                                break;

                            case 3: //opponent win
                                Platform.runLater(() -> {
                                    lvlChanger.getGameController().getStatusHelp().setText("Соперник победил!");
                                    lvlChanger.getGameController().getStatusHelp().setTextFill(Color.INDIANRED);
                                });
                                break;

                            case 4: //opponent give up
                                Platform.runLater(() -> {
                                    lvlChanger.getGameController().getStatusHelp().setText("Соперник сдался!");
                                    lvlChanger.getGameController().getStatusHelp().setTextFill(Color.GREEN);
                                });
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        socketThread.start();
    }
}