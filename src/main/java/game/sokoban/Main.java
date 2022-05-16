package game.sokoban;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        LvlChanger lvlChanger = new LvlChanger(stage);
        lvlChanger.startMenu();
        stage.setTitle("Sokoban v. 0.4");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}