package game.sokoban;

import game.sokoban.controllers.GameController;
import game.sokoban.controllers.MenuController;
import game.sokoban.elements.Hero;
import game.sokoban.elements.Block;
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
    private double errPos = 0.15*blockSize;
    private int WIDTH = 900, HEIGHT = 800;
    private int gWidth, gHeight;
    private MenuController menuController;
    private GameController gameController;
    private boolean isOpenWindow = false;

    private int numLvl = 0;
    private int[][] matrix;
    private Hero hero;
    private ImageView exit, chest, key;
    private ArrayList<Block> listBoxAndEnemy = new ArrayList<>(), listSpike = new ArrayList<>();

    Image imgFloor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0017.png")), blockSize, blockSize, false, true);
    Image imgWall1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0145.png")), blockSize, blockSize, false, true);
    Image imgWall2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0148.png")), blockSize, blockSize, false, true);
    Image imgWall3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0144.png")), blockSize, blockSize, false, true);
    Image imgWall4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0147.png")), blockSize, blockSize, false, true);
    Image imgWall5 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0150.png")), blockSize, blockSize, false, true);
    Image imgWall6 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0153.png")), blockSize, blockSize, false, true);
    Image imgWall7 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0162.png")), blockSize, blockSize, false, true);
    Image imgWall8 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0163.png")), blockSize, blockSize, false, true);
    Image imgColumn9 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0030.png")), blockSize, blockSize, false, true);
    Image imgBox = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0096.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0010.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgSpike = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0018.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0160.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgExit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0052.png")), blockSize, blockSize, false, true);
    Image imgExitEnd = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0161.png")), blockSize, blockSize, false, true);
    Image imgChest = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0057.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgKey = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0090.png")), 0.4*blockSize, 0.4*blockSize, false, true);

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
                        hero.move(hero.getX(), hero.getY() - 1, this);
                        break;
                    case S:
                        hero.move(hero.getX(), hero.getY() + 1, this);
                        break;
                    case A:
                        hero.move(hero.getX() - 1, hero.getY(), this);
                        break;
                    case D:
                        hero.move(hero.getX() + 1, hero.getY(), this);
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

    public void winLevel() {
        reverseOpenWindow();
        gameController.getWinWindow().toFront();
        gameController.getWinWindow().setVisible(true);
    }

    public void clearLvl(Pane field) {
        field.getChildren().clear();
        key = null;
        chest = null;
        listBoxAndEnemy.clear();
        listSpike.clear();

        gameController.getWinWindow().setVisible(false);
        gameController.getMenuWindow().setVisible(false);
    }

    public void loadLvl(Pane field, int num) {
        clearLvl(field);
        numLvl = num;
        if (numLvl == 6) startMenu(); //TODO win game
        else {
            File fileMap = new File("levelConfig/maps/" + numLvl + ".conf");
            field.getChildren().add(createGameField(fileMap));
            //create blocks and hero
            File fileBlock = new File("levelConfig/blocks/" + numLvl + ".conf");
            field.getChildren().add(createElements(fileBlock));
        }
    }

    public GridPane createGameField(File fileMap) {
        try {
            BufferedReader inLen = new BufferedReader((new FileReader(fileMap.getAbsoluteFile())));
            int i = 0;
            while (inLen.readLine() != null) {
                i++;
            }
            matrix = new int[i][];
            inLen.close();

            BufferedReader in = new BufferedReader((new FileReader(fileMap.getAbsoluteFile())));
            String str;
            int j = 0;
            while ((str = in.readLine()) != null)
                matrix[j++] = Arrays.stream(str.split(" ")).mapToInt(Integer::parseInt).toArray();
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
                switch (matrix[i][j]) {
                    case 0:
                        grid.add(new ImageView(imgFloor), i, j);
                        break;
                    case 1:
                        grid.add(new ImageView(imgWall1), i, j);
                        break;
                    case 2:
                        grid.add(new ImageView(imgWall2), i, j);
                        break;
                    case 3:
                        grid.add(new ImageView(imgWall3), i, j);
                        break;
                    case 4:
                        grid.add(new ImageView(imgWall4), i, j);
                        break;
                    case 5:
                        grid.add(new ImageView(imgWall5), i, j);
                        break;
                    case 6:
                        grid.add(new ImageView(imgWall6), i, j);
                        break;
                    case 7:
                        grid.add(new ImageView(imgWall7), i, j);
                        break;
                    case 8:
                        grid.add(new ImageView(imgWall8), i, j);
                        break;
                    case 9:
                        grid.add(new ImageView(imgColumn9), i, j);
                        break;
                }
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
                    case "box":
                        img = new ImageView(imgBox);
                        boxBox.getChildren().add(img);
                        listBoxAndEnemy.add(new Block(parseInt(strSplit[1]), parseInt(strSplit[2]), img, "Box"));
                        break;
                    case "enemy":
                        img = new ImageView(imgEnemy);
                        enemyBox.getChildren().add(img);
                        listBoxAndEnemy.add(new Block(parseInt(strSplit[1]), parseInt(strSplit[2]), img, "Enemy"));
                        break;
                    case "spike":
                        img = new ImageView(imgSpike);
                        spikeBox.getChildren().add(img);
                        listSpike.add(new Block(parseInt(strSplit[1]), parseInt(strSplit[2]), img, "Spike"));
                        break;
                    case "exit":
                        if (numLvl == 5) exit = new ImageView(imgExitEnd);
                        else exit = new ImageView(imgExit);
                        exit.setTranslateX(parseInt(strSplit[1]) * blockSize);
                        exit.setTranslateY(parseInt(strSplit[2]) * blockSize);
                        hero.setWinX(parseInt(strSplit[1]));
                        hero.setWinY(parseInt(strSplit[2]));
                        break;
                    case "chest":
                        chest = new ImageView(imgChest);
                        chest.setTranslateX(parseInt(strSplit[1]) * blockSize + errPos);
                        chest.setTranslateY(parseInt(strSplit[2]) * blockSize + errPos);
                        hero.setChestX(parseInt(strSplit[1]));
                        hero.setChestY(parseInt(strSplit[2]));
                        break;
                    case "key":
                        key = new ImageView(imgKey);
                        key.setTranslateX(parseInt(strSplit[1]) * blockSize + 2*errPos);
                        key.setTranslateY(parseInt(strSplit[2]) * blockSize + 2*errPos);
                        hero.setKeyX(parseInt(strSplit[1]));
                        hero.setKeyY(parseInt(strSplit[2]));
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
        elemBox.getChildren().addAll(exit, spikeBox, enemyBox, boxBox, hero.getImg());
        if (key != null) elemBox.getChildren().add(key);
        if (chest != null) elemBox.getChildren().add(chest);
        elemBox.setTranslateX((WIDTH - gWidth) / 2d);
        elemBox.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
        return elemBox;
    }

    public GameController getGameController() { return gameController; }
    public int getNumLvl() { return numLvl; }
    public Hero getHero() { return hero; }
    public void reverseOpenWindow() { isOpenWindow = !isOpenWindow; }
    public ArrayList<Block> getListBoxAndEnemy() { return listBoxAndEnemy; }
    public ArrayList<Block> getListSpike() { return listSpike; }
    public int[][] getMatrix() { return matrix; }
    public ImageView getKey() { return key; }
    public ImageView getChest() { return chest; }
}
