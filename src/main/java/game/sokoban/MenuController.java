package game.sokoban;

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
    private Button lvlOneBtn;

    @FXML
    private Button lvlTwoBtn;

    @FXML
    private Button undoBtn;

    @FXML
    private Button exitBtn;

    @FXML
    void initialize(LvlChanger lvlChanger) {
        startBtn.setOnAction(e -> lvlChanger.startGame(1));
        lvlBtn.setOnAction(e -> {
            btnPane.setVisible(false);
            lvlMenu.setVisible(true);
        });
        lvlOneBtn.setOnAction(e -> lvlChanger.startGame(1));
        lvlTwoBtn.setOnAction(e -> lvlChanger.startGame(2));
        undoBtn.setOnAction(e -> {
            btnPane.setVisible(true);
            lvlMenu.setVisible(false);
        });
        exitBtn.setOnAction(e -> System.exit(0));
    }
}
