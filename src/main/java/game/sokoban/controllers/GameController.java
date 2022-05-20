package game.sokoban.controllers;

import game.sokoban.LvlChanger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private Pane gameField;

    @FXML
    private Label statusTurns;

    @FXML
    private Label statusTimer;

    @FXML
    private Pane winWindow;

    @FXML
    private Button resetWinBtn;

    @FXML
    private Button nextWinBtn;

    @FXML
    private Pane menuWindow;

    @FXML
    private Button exitMenuBtn;

    @FXML
    private Button mainMenuBtn;

    @FXML
    private Button resetMenuBtn;

    @FXML
    private Button continueMenuBtn;


    public Pane getGameField() { return gameField; }

    public Pane getWinWindow() { return winWindow; }

    public Pane getMenuWindow() { return menuWindow; }

    public void setTurnsLeft(int count) { statusTurns.setText("Ходов осталось: " + count);}

    @FXML
    public void initialize(LvlChanger lvlChanger) {
        nextWinBtn.setOnAction(e -> {
            lvlChanger.loadLvl(gameField,lvlChanger.getNumLvl()+1);
            lvlChanger.reverseOpenWindow();
        });
        resetWinBtn.setOnAction(e -> {
            lvlChanger.loadLvl(gameField,lvlChanger.getNumLvl());
            lvlChanger.reverseOpenWindow();
        });
        continueMenuBtn.setOnAction(e -> {
            menuWindow.setVisible(false);
            lvlChanger.reverseOpenWindow();
        });
        resetMenuBtn.setOnAction(e -> {
            lvlChanger.loadLvl(gameField,lvlChanger.getNumLvl());
            lvlChanger.reverseOpenWindow();
        });
        mainMenuBtn.setOnAction(e -> {
            lvlChanger.startMenu();
            lvlChanger.reverseOpenWindow();
        });
        exitMenuBtn.setOnAction(e -> System.exit(0));
    }
}
