package game.sokoban;

import game.sokoban.gameplay.Launcher;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client1 extends Application {
    @Override
    public void start(Stage stage) {
        new Launcher(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
