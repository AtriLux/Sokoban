package game.sokoban;

import game.sokoban.elements.Hero;
import game.sokoban.elements.blocks.Block;
import game.sokoban.elements.blocks.Box;
import game.sokoban.elements.blocks.Enemy;
import game.sokoban.elements.blocks.Spike;
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
import static javafx.scene.input.KeyCode.ESCAPE;

public class LvlChanger {

    private Stage stage;
    private int blockSize = Block.blockSize;
    private int WIDTH = 900, HEIGHT = 800;
    private int gWidth, gHeight;
    private int numLvl = 0;
    private int[][] matrix;
    private Hero hero;
    private ImageView candy;
    private MenuController menuController;
    private GameController gameController;
    private boolean isOpenWindow = false;

    private ArrayList<Box> listBox = new ArrayList<>();
    private ArrayList<Enemy> listEnemy = new ArrayList<>();
    private ArrayList<Spike> listSpike = new ArrayList<>();

    Image imgFloor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/floor3.png")), blockSize, blockSize, false, true);
    Image imgWall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/wall3.png")), blockSize, blockSize, false, true);
    Image imgTable = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/table.png")), blockSize, blockSize, false, true);
    Image imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy.png")), 0.7*blockSize, blockSize, false, true);
    Image imgSpike = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/puddle.png")), 0.9*blockSize, 0.6*blockSize, false, true);
    Image imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hero.png")), 0.7*blockSize, blockSize, false, true);
    Image imgCandy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/candy.png")), blockSize, blockSize, false, true);

    LvlChanger(Stage primaryStage) { stage = primaryStage; }

    public void startMenu() {
        try {
            FXMLLoader fxml = new FXMLLoader(Main.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
            menuController = fxml.getController();
            menuController.initialize(this);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame(int num) {
        try {
            FXMLLoader fxml = new FXMLLoader(Main.class.getResource("game.fxml"));
            Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
            stage.setScene(scene);
            gameController = fxml.getController();
            gameController.initialize(this);
            loadLvl(gameController.getGameField(), num);
            initKey(scene, gameController);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initKey(Scene scene, GameController controller) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            Hero hero = getHero();
            KeyCode code = evt.getCode();
            if (!isOpenWindow)
                switch (code) {
                    case W:
                        hero.move(hero.getX(), hero.getY() - 1, matrix, this, listBox, listEnemy, listSpike);
                        break;
                    case S:
                        hero.move(hero.getX(), hero.getY() + 1, matrix, this, listBox, listEnemy, listSpike);
                        break;
                    case A:
                        hero.move(hero.getX() - 1, hero.getY(), matrix, this, listBox, listEnemy, listSpike);
                        break;
                    case D:
                        hero.move(hero.getX() + 1, hero.getY(), matrix, this, listBox, listEnemy, listSpike);
                        break;
                    case R:
                        loadLvl(controller.getGameField(), getNumLvl());
                        break;
                }
            if (code == ESCAPE && !controller.getWinWindow().isVisible())
                if (controller.getMenuWindow().isVisible()) {
                    controller.getMenuWindow().setVisible(false);
                    reverseOpenWindow();
                }
                else {
                    controller.getMenuWindow().setVisible(true);
                    reverseOpenWindow();
                }
        });
    }

    public void winGame() {
        Pane pane = (Pane) gameController.getGameField().getChildren().get(1);
        pane.getChildren().remove(candy);

        reverseOpenWindow();
        gameController.getWinWindow().toFront();
        gameController.getWinWindow().setVisible(true);
    }

    public void clearLvl(Pane field) {
        field.getChildren().clear();
        gameController.getWinWindow().setVisible(false);
        gameController.getMenuWindow().setVisible(false);
        listEnemy.clear();
        listBox.clear();
        listSpike.clear();
    }

    public void loadLvl(Pane field, int num) {
        clearLvl(field);
        numLvl = num;
        switch (numLvl) {
            case 1:
                levelOne(field);
                break;
            case 2:
                levelTwo(field);
                break;
        }
    }

    public void levelOne(Pane field) {
        //create game field
        matrix = new int[9][];
        File fileMap = new File("levelConfig/maps/1.conf");
        field.getChildren().add(createGameField(fileMap));
        //create blocks and hero
        File fileBlock = new File("levelConfig/blocks/1.conf");
        field.getChildren().add(createElements(fileBlock));
    }

    public void levelTwo(Pane field) {
        matrix = new int[9][];
        File fileMap = new File("levelConfig/maps/2.conf");
        field.getChildren().add(createGameField(fileMap));
        File fileBlock = new File("levelConfig/blocks/2.conf");
        field.getChildren().add(createElements(fileBlock));
    }

    public GridPane createGameField(File fileMap) {
        try {
            BufferedReader in = new BufferedReader((new FileReader(fileMap.getAbsoluteFile())));
            String str;
            int i = 0;
            while ((str = in.readLine()) != null)
                matrix[i++] = Arrays.stream(str.split(" ")).mapToInt(Integer::parseInt).toArray();
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
        grid.setHgap(0);
        //grid to center
        gWidth = grid.getColumnCount() * blockSize;
        gHeight = grid.getRowCount() * blockSize;
        grid.setTranslateX((WIDTH - gWidth) / 2d);
        grid.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
        return grid;
    }

    public Pane createElements(File fileBlock) {
        Pane elemBox = new Pane(), boxBox = new Pane(), enemyBox = new Pane(), spikeBox = new Pane();
        try {
            BufferedReader in = new BufferedReader((new FileReader(fileBlock.getAbsoluteFile())));
            String str;
            ImageView img;
            while ((str = in.readLine()) != null) {
                String[] strSplit = str.split(" ");
                switch (strSplit[0]) {
                    case "table":
                        img = new ImageView(imgTable);
                        boxBox.getChildren().add(img);
                        listBox.add(new Box(parseInt(strSplit[1]), parseInt(strSplit[2]), img));
                        break;
                    case "enemy":
                        img = new ImageView(imgEnemy);
                        enemyBox.getChildren().add(img);
                        listEnemy.add(new Enemy(parseInt(strSplit[1]), parseInt(strSplit[2]), img));
                        break;
                    case "spike":
                        img = new ImageView(imgSpike);
                        spikeBox.getChildren().add(img);
                        listSpike.add(new Spike(parseInt(strSplit[1]), parseInt(strSplit[2]), img));
                        break;
                    case "candy":
                        candy = new ImageView(imgCandy);
                        candy.setTranslateX(parseInt(strSplit[1]) * blockSize);
                        candy.setTranslateY(parseInt(strSplit[2]) * blockSize);
                        hero.setWinX(parseInt(strSplit[1]));
                        hero.setWinY(parseInt(strSplit[2]));
                        break;
                    case "hero":
                        img = new ImageView(imgHero);
                        hero = new Hero(parseInt(strSplit[1]), parseInt(strSplit[2]), parseInt(strSplit[3]), img);
                        gameController.setTurnsLeft(parseInt(strSplit[3]));
                        break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        elemBox.getChildren().addAll(candy, spikeBox, enemyBox, boxBox, hero.getImg());
        elemBox.setTranslateX((WIDTH - gWidth) / 2d);
        elemBox.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
        return elemBox;
    }

    public GameController getGameController() { return gameController; }
    public int getNumLvl() { return numLvl; }
    public Hero getHero() { return hero; }
    public void reverseOpenWindow() { isOpenWindow = !isOpenWindow; }
}
