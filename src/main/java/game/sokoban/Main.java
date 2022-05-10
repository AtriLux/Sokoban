package game.sokoban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private int WIDTH = 900;
    private int HEIGHT = 700;

    Controller controller;
    LvlChanger lvlChanger = new LvlChanger(WIDTH, HEIGHT);

    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = startGame();
        stage.setTitle("Sokoban v. 0.1");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public Scene startGame() throws IOException {
        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("game.fxml"));
        Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
        controller = fxml.getController();
        lvlChanger.loadLvl1(controller);
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}