package game.sokoban.gameplay.controllers;

import game.sokoban.gameplay.services.clientServer.Message;
import game.sokoban.gameplay.Launcher;
import game.sokoban.gameplay.LvlChanger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;

public class GameController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pane gameField;
    @FXML
    private Label statusTurns;
    @FXML
    private Label statusTimer;
    @FXML
    private Label statusHelp;
    @FXML
    private Pane winWindow;
    @FXML
    private Button resetWinBtn;
    @FXML
    private Button saveWinBtn;
    @FXML
    private Pane recordWindow;
    @FXML
    private VBox recordTable;
    @FXML
    private TextField nickTextField;
    @FXML
    private Button undoRecordBtn;
    @FXML
    private Button saveRecordBtn;
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
    @FXML
    private Pane giveupWindow;
    @FXML
    private Button noOnlineBtn;
    @FXML
    private Button yesOnlineBtn;
    @FXML
    private Pane winOnlineWindow;
    @FXML
    private Button saveOnlineWinBtn;
    @FXML
    private Button exitOnlineWinBtn;

    public AnchorPane getAnchorPane() { return anchorPane; }
    public Pane getGameField() { return gameField; }
    public Pane getWinWindow() { return winWindow; }
    public Pane getMenuWindow() { return menuWindow; }
    public Pane getGiveupWindow() { return giveupWindow; }
    public Pane getWinOnlineWindow() { return winOnlineWindow; }
    public Label getStatusTimer() { return statusTimer; }
    public Label getStatusHelp() { return statusHelp; }
    public Button getSaveWinBtn() { return saveWinBtn; }

    @FXML
    public void initialize(LvlChanger lvlChanger, Launcher launcher) {
        nextWinBtn.setOnAction(e -> {
            lvlChanger.loadLvl(gameField,lvlChanger.getNumLvl()+1);
            lvlChanger.reverseOpenWindow();
        });
        saveWinBtn.setOnAction(e -> {
            saveRecordBtn.setDisable(false);
            lvlChanger.clearRecordTable(recordTable);
            lvlChanger.getDB().writeToTable(recordTable, lvlChanger.getNumLvl());
            recordWindow.toFront();
            recordWindow.setVisible(true);
        });
        undoRecordBtn.setOnAction(e -> {
            if (saveRecordBtn.isDisable()) {
                saveWinBtn.setDisable(true);
                saveOnlineWinBtn.setDisable(true);
            }
            recordWindow.setVisible(false);
        });
        saveRecordBtn.setOnAction(e -> {
            if (!nickTextField.getText().equals("")) {
                lvlChanger.saveRecordDB(nickTextField.getText(), recordTable);
                saveRecordBtn.setDisable(true);
            }
            else nickTextField.setText("введите имя");
        });
        nickTextField.setOnMouseClicked(e -> {
            if (nickTextField.getText().equals("введите имя (без пробелов)!")) nickTextField.setText("");
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
        yesOnlineBtn.setOnAction(e -> {
            try {
                if (statusHelp.getTextFill() == Color.WHITE) {
                    OutputStream os = launcher.getSocket().getOutputStream();
                    os.write(new Message(launcher.getIdOpponent(), 4).getBytes());
                    os.flush();
                }
                lvlChanger.startMenu();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        noOnlineBtn.setOnAction(e -> {
            giveupWindow.setVisible(false);
            lvlChanger.reverseOpenWindow();
        });
        saveOnlineWinBtn.setOnAction(e -> {
            saveRecordBtn.setDisable(false);
            lvlChanger.clearRecordTable(recordTable);
            lvlChanger.getDB().writeToTable(recordTable, lvlChanger.getNumLvl());
            recordWindow.toFront();
            recordWindow.setVisible(true);
        });
        exitOnlineWinBtn.setOnAction(e -> lvlChanger.startMenu());
    }

    public void setStatusHelp(boolean isOpenHelp, int num) {
        if (isOpenHelp) statusHelp.setText("R - заново, H - помощь, WASD - управление");
        else
            try {
                File file = new File("levelConfig/help.txt");
                BufferedReader in = new BufferedReader((new FileReader(file.getAbsoluteFile())));
                int i = 1;
                String str;
                while ((str = in.readLine()) != null) {
                    if (i == num) {
                        statusHelp.setText(str);
                        break;
                    }
                    i++;
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void setTurnsLeft(int count) { statusTurns.setText("Ходов осталось: " + count);}
}
