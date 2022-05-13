package game.sokoban;

import game.sokoban.elements.Hero;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

import static javafx.scene.input.KeyCode.*;

public class Main extends Application {

    private int WIDTH = 900;
    private int HEIGHT = 700;

    Controller menuController;
    LvlChanger lvlChanger = new LvlChanger(WIDTH, HEIGHT);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
        menuController = fxml.getController();
        menuController.initialize(stage, lvlChanger, WIDTH, HEIGHT);
        stage.setTitle("Sokoban v. 0.3");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}