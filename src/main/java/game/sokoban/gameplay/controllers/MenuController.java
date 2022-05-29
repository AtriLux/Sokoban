package game.sokoban.gameplay.controllers;

import game.sokoban.clientServer.Message;
import game.sokoban.gameplay.Launcher;
import game.sokoban.gameplay.LvlChanger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class MenuController {

    @FXML
    private Pane btnPane;

    @FXML
    private Pane lvlMenu;

    @FXML
    private Button startBtn;

    @FXML
    private Button lvlBtn;

    @FXML
    private Button lvlBtn1;

    @FXML
    private Button lvlBtn2;

    @FXML
    private Button lvlBtn3;

    @FXML
    private Button lvlBtn4;

    @FXML
    private Button lvlBtn5;

    @FXML
    private Button undoBtn;

    @FXML
    private Button onlineBtn;

    @FXML
    private Button recordBtn;

    @FXML
    private Pane recordPane;

    @FXML
    private Button recordBtn1;

    @FXML
    private Button recordBtn2;

    @FXML
    private Button recordBtn3;

    @FXML
    private Button recordBtn4;

    @FXML
    private Button recordBtn5;

    @FXML
    private VBox recordTable;

    @FXML
    private Button undoRecordBtn;

    @FXML
    private Pane onlinePane;

    @FXML
    private Label onlineLabel;

    @FXML
    private Button undoOnlineBtn;

    @FXML
    private Button readyOnlineBtn;

    @FXML
    private Button exitBtn;

    @FXML
    public void initialize(LvlChanger lvlChanger, Launcher launcher) {
        startBtn.setOnAction(e -> lvlChanger.startGame(0));
        recordBtn.setOnAction(e -> {
            lvlChanger.clearRecordTable(recordTable);
            recordPane.setVisible(true);
            btnPane.setVisible(false);
        });
        recordBtn1.setOnAction(e -> lvlChanger.getDB().writeToTable(recordTable, 1));
        recordBtn2.setOnAction(e -> lvlChanger.getDB().writeToTable(recordTable, 2));
        recordBtn3.setOnAction(e -> lvlChanger.getDB().writeToTable(recordTable, 3));
        recordBtn4.setOnAction(e -> lvlChanger.getDB().writeToTable(recordTable, 4));
        recordBtn5.setOnAction(e -> lvlChanger.getDB().writeToTable(recordTable, 5));
        undoRecordBtn.setOnAction(e -> {
            recordPane.setVisible(false);
            btnPane.setVisible(true);
        });
        lvlBtn.setOnAction(e -> {
            btnPane.setVisible(false);
            lvlMenu.setVisible(true);
        });
        lvlBtn1.setOnAction(e -> lvlChanger.startGame(1));
        lvlBtn2.setOnAction(e -> lvlChanger.startGame(2));
        lvlBtn3.setOnAction(e -> lvlChanger.startGame(3));
        lvlBtn4.setOnAction(e -> lvlChanger.startGame(4));
        lvlBtn5.setOnAction(e -> lvlChanger.startGame(5));
        undoBtn.setOnAction(e -> {
            btnPane.setVisible(true);
            lvlMenu.setVisible(false);
        });
        onlineBtn.setOnAction(e -> {
            try {
                if (!isOnline) {
                    readyOnlineBtn.setDisable(true);
                    onlineLabel.setText("Ожидание игрока...");
                }
                onlinePane.setVisible(true);
                btnPane.setVisible(false);
                OutputStream os = launcher.getSocket().getOutputStream();
                ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                buffer.putInt(1);
                os.write(buffer.array());
                os.flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        readyOnlineBtn.setOnAction(e -> {
            try {
                readyOnlineBtn.setDisable(true);
                OutputStream os = launcher.getSocket().getOutputStream();
                os.write(new Message(launcher.getIdOpponent(),2).getBytes());
                os.flush();
            }
            catch (IOException ex) {
            ex.printStackTrace();
            }
        });
        undoOnlineBtn.setOnAction(e -> {
            onlinePane.setVisible(false);
            btnPane.setVisible(true);
        });
        exitBtn.setOnAction(e -> System.exit(0));
    }

    boolean isOnline = false;

    public void setOnlineOpponent() {
        isOnline = true;
        Platform.runLater(() -> onlineLabel.setText("Игрок найден!"));
        readyOnlineBtn.setDisable(false);
    }

    public void setOnlineReady() {
        Platform.runLater(() -> onlineLabel.setText("Игрок готов!"));
    }

    public void setDisableOnlineBtn() { onlineBtn.setDisable(true); }
}
