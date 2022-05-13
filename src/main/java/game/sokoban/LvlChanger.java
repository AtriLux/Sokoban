package game.sokoban;

import game.sokoban.elements.Hero;
import game.sokoban.elements.blocks.Box;
import game.sokoban.elements.blocks.Enemy;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class LvlChanger {

    private int blockSize = 60;
    private int WIDTH, HEIGHT;
    private int gWidth, gHeight;
    private int numLvl = 0;
    private int[][] matrix;
    private Hero hero;
    private Controller gameController;
    private ArrayList<Box> listBox = new ArrayList<>();
    private ArrayList<Enemy> listEnemy = new ArrayList<>();

    Image imgFloor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/floor2.png")), blockSize, blockSize, false, true);
    Image imgWall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/wall2.jpg")), blockSize, blockSize, false, true);
    Image imgTable = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/table.png")), blockSize, blockSize, false, true);
    Image imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy.png")), 0.75*blockSize, blockSize, false, true);
    Image imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hero.png")), 0.75*blockSize, blockSize, false, true);
    Image imgCandy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/candy.png")), blockSize, blockSize, false, true);

    LvlChanger(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    public void startGame(Stage stage) {
        try {
            FXMLLoader fxml = new FXMLLoader(Main.class.getResource("game.fxml"));
            Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
            stage.setScene(scene);
            gameController = fxml.getController();
            loadLvl(gameController.getGameField(), 1);
            initKey(scene, gameController);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initKey(Scene scene, Controller controller) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            Hero hero = getHero();
            KeyCode code = evt.getCode();
            switch (code) {
                case W:
                    hero.move(hero.getX(), hero.getY() - 1, getMatrix(), getListBox(), getListEnemy());
                    break;
                case S:
                    hero.move(hero.getX(), hero.getY() + 1, getMatrix(), getListBox(), getListEnemy());
                    break;
                case A:
                    hero.move(hero.getX() - 1, hero.getY(), getMatrix(), getListBox(), getListEnemy());
                    break;
                case D:
                    hero.move(hero.getX() + 1, hero.getY(), getMatrix(), getListBox(), getListEnemy());
                    break;
                case R:
                    loadLvl(controller.getGameField(), getNumLvl());
                    break;
            }
        });
    }

    public void clearLvl(Pane field) {
        field.getChildren().clear();
        listEnemy.clear();
        listBox.clear();
    }

    public void loadLvl(Pane field, int num) {
        clearLvl(field);
        numLvl = num;
        switch (numLvl) {
            case 1:
                levelOne(field);
                break;
        }
    }

    public void levelOne(Pane field) {
        //create game field
        matrix = new int[10][];
        File fileMap = new File("levelConfig/maps/1.conf");
        field.getChildren().add(createGameField(fileMap));
        //create blocks and hero
        File fileBlock = new File("levelConfig/blocks/1.conf");
        field.getChildren().add(createElements(fileBlock));
    }

    public GridPane createGameField(File fileMap) {
        try {
            BufferedReader in = new BufferedReader((new FileReader(fileMap.getAbsoluteFile())));
            String str;
            int i = 0;
            while ((str = in.readLine()) != null) {
                matrix[i++] = Arrays.stream(str.split(" ")).mapToInt(Integer::parseInt).toArray();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //create blocks and add into grid
        int mHeight = matrix.length;
        int mWidth = matrix[0].length;
        GridPane grid = new GridPane();
        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                if (matrix[i][j] == 0)
                    grid.add(new ImageView(imgFloor), i, j);
                if (matrix[i][j] == 1)
                    grid.add(new ImageView(imgWall), i, j);
            }
        }
        //delete padding between cells
        grid.setVgap(0);
        grid.setHgap(-1);
        //grid to center
        gWidth = grid.getColumnCount() * blockSize;
        gHeight = grid.getRowCount() * blockSize;
        grid.setTranslateX((WIDTH - gWidth) / 2d);
        grid.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
        return grid;
    }

    public Pane createElements(File fileBlock) {
        Pane elemBox = new Pane();
        try {
            BufferedReader in = new BufferedReader((new FileReader(fileBlock.getAbsoluteFile())));
            String str;
            ImageView img;
            while ((str = in.readLine()) != null) {
                String[] strSplit = str.split(" ");
                switch (strSplit[0]) {
                    case "table":
                        img = new ImageView(imgTable);
                        elemBox.getChildren().add(img);
                        listBox.add(new Box(parseInt(strSplit[1]), parseInt(strSplit[2]), img));
                        break;
                    case "enemy":
                        img = new ImageView(imgEnemy);
                        elemBox.getChildren().add(img);
                        listEnemy.add(new Enemy(parseInt(strSplit[1]), parseInt(strSplit[2]), img));
                        break;
                    case "candy":
                        img = new ImageView(imgCandy);
                        img.setTranslateX(parseInt(strSplit[1]) * blockSize);
                        img.setTranslateY(parseInt(strSplit[2]) * blockSize);
                        elemBox.getChildren().add(img);
                        hero.setWinX(parseInt(strSplit[1]));
                        hero.setWinY(parseInt(strSplit[2]));
                        break;
                    case "hero":
                        img = new ImageView(imgHero);
                        elemBox.getChildren().add(img);
                        hero = new Hero(parseInt(strSplit[1]), parseInt(strSplit[2]), img);
                        break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        elemBox.setTranslateX((WIDTH - gWidth) / 2d);
        elemBox.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
        return elemBox;
    }

    public Hero getHero() { return hero; }

    public int[][] getMatrix() { return matrix; }

    public ArrayList<Box> getListBox() { return listBox; }

    public ArrayList<Enemy> getListEnemy() { return listEnemy; }

    public int getNumLvl() { return numLvl; }
}
