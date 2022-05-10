package game.sokoban;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.StackPane;

public class Controller {

    @FXML
    private StackPane field;

    @FXML
    private MenuBar menu;

    @FXML
    private Label status;

    public StackPane getField() { return field; }

    @FXML
    void initialize() {

    }
}
