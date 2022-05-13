package game.sokoban;

import game.sokoban.elements.Hero;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Pane startField;

    @FXML
    private Pane gameField;

    @FXML
    private MenuBar menu;

    @FXML
    private Label status;

    @FXML
    private Button startBtn;

    public Pane getGameField() { return gameField; }

    public Pane getStartField() { return startField; }

    public Button getStartBtn() { return startBtn; }

    @FXML
    void initialize(Stage stage, LvlChanger lvlChanger, int WIDTH, int HEIGHT) {
        startBtn.setOnAction(e -> {
            lvlChanger.startGame(stage);
        });
    }


}
