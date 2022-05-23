package game.sokoban.controllers;

import game.sokoban.LvlChanger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    private Button exitBtn;

    @FXML
    public void initialize(LvlChanger lvlChanger) {
        startBtn.setOnAction(e -> lvlChanger.startGame(1));
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
        exitBtn.setOnAction(e -> System.exit(0));
    }
}
