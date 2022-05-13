package game.sokoban;

import game.sokoban.elements.Hero;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;

import static javafx.scene.input.KeyCode.*;

public class Main extends Application {

    private int WIDTH = 900;
    private int HEIGHT = 700;

    Controller controller;
    LvlChanger lvlChanger = new LvlChanger(WIDTH, HEIGHT);

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = startGame();
        stage.setTitle("Sokoban v. 0.2");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public Scene startGame() throws IOException {
        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("game.fxml"));
        Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
        controller = fxml.getController();
        lvlChanger.loadLvl1(controller);
        initKey(scene, lvlChanger.getHero());
        return scene;
    }

    public void initKey(Scene scene, Hero hero) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            KeyCode code = evt.getCode();
            switch (code) {
                case W:
                    hero.move(hero.getX(), hero.getY() - 1, lvlChanger.getMatrix(), lvlChanger.getListBox(), lvlChanger.getListEnemy());
                    break;
                case S:
                    hero.move(hero.getX(), hero.getY() + 1, lvlChanger.getMatrix(), lvlChanger.getListBox(), lvlChanger.getListEnemy());
                    break;
                case A:
                    hero.move(hero.getX() - 1, hero.getY(), lvlChanger.getMatrix(), lvlChanger.getListBox(), lvlChanger.getListEnemy());
                    break;
                case D:
                    hero.move(hero.getX() + 1, hero.getY(), lvlChanger.getMatrix(), lvlChanger.getListBox(), lvlChanger.getListEnemy());
                    break;
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}