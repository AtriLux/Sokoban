package game.sokoban.gameplay;

import game.sokoban.Client1;
import game.sokoban.gameplay.services.clientServer.Message;
import game.sokoban.gameplay.controllers.GameController;
import game.sokoban.gameplay.controllers.MenuController;
import game.sokoban.gameplay.elements.*;
import game.sokoban.gameplay.services.DataBase;
import game.sokoban.gameplay.services.Sounds;
import game.sokoban.gameplay.services.Timers;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static javafx.scene.input.KeyCode.ESCAPE;

public class LvlChanger {

    private final Launcher launcher;
    private final Stage stage;
    private final int blockSize = Block.blockSize;
    private final double errPos = 0.15*blockSize;
    private final int WIDTH = 900;
    private final int HEIGHT = 800;
    private int gWidth, gHeight;
    private MenuController menuController;
    private GameController gameController;
    private boolean isOpenWindow = false;
    private boolean isOpenHelp = false;
    private boolean isSingleGame = true;

    private Timers timer;
    private final DataBase DB = new DataBase();
    private final Sounds player = new Sounds();

    private int numLvl = 0;
    private char[][] matrix;
    private Hero hero;
    private ImageView chest, key;
    private final ArrayList<Block> listBoxAndEnemy = new ArrayList<>(), listSpike = new ArrayList<>();

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
    Image imgSpike = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0018.png")), 0.9*blockSize, 0.9*blockSize, false, true);
    Image imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0160.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgExit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0052.png")), blockSize, blockSize, false, true);
    Image imgExitEnd = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0161.png")), blockSize, blockSize, false, true);
    Image imgChest = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0057.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    Image imgKey = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0090.png")), 0.4*blockSize, 0.4*blockSize, false, true);

    LvlChanger(Stage primaryStage, Launcher launcher) {
        stage = primaryStage;
        stage.getIcons().add(imgChest);
        this.launcher = launcher;
    }

    public void startMenu() {
        try {
            player.playMainMusic();
            FXMLLoader fxml = new FXMLLoader(Client1.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
            menuController = fxml.getController();
            menuController.initialize(this, launcher);
            if (!launcher.isSocketActive()) menuController.setDisableOnlineBtn();
            if (!DB.isActive()) menuController.setDisableRecordBtn();
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame(int num) {
        try {
            player.playGameMusic();
            FXMLLoader fxml = new FXMLLoader(Client1.class.getResource("game.fxml"));
            Scene scene = new Scene(fxml.load(), WIDTH, HEIGHT);
            LvlChanger temp = this;
            Platform.runLater(() -> {
                stage.setScene(scene);
                gameController = fxml.getController();
                gameController.initialize(temp, launcher);
                loadLvl(gameController.getGameField(), num);
                initKey(scene, gameController);
            });

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
                        if (isSingleGame) timer.stop();
                        resetLvl(true);
                        break;
                    case H:
                        if (isSingleGame) {
                            controller.setStatusHelp(isOpenHelp, numLvl);
                            isOpenHelp = !isOpenHelp;
                        }
                        break;
                }
            if (code == ESCAPE && !controller.getWinWindow().isVisible()) {
                if (isOpenWindow) { // open window
                    if (isSingleGame) controller.getMenuWindow().setVisible(false); // single game
                    else controller.getGiveupWindow().setVisible(false); // online game
                } else { // close window
                    if (isSingleGame) controller.getMenuWindow().setVisible(true);
                    else controller.getGiveupWindow().setVisible(true);
                }
                reverseOpenWindow(); // reverse boolean marker
            }
        });
    }

    public void winLvl() {
        timer.stop();
        reverseOpenWindow();
        closeHelp();
        gameController.getSaveWinBtn().setDisable(numLvl == 0 || !DB.isActive()); // if lvl0 or DB isn't active
        if (isSingleGame) { // single
            gameController.getWinWindow().toFront();
            gameController.getWinWindow().setVisible(true);
        } else { // online
            try {
                gameController.getWinOnlineWindow().toFront();
                gameController.getWinOnlineWindow().setVisible(true);
                // send to server only if player wasn't win or lose before
                if (getGameController().getStatusHelp().getTextFill() == Color.WHITE) {
                    OutputStream os = launcher.getSocket().getOutputStream();
                    os.write(new Message(launcher.getIdOpponent(), 3).getBytes());
                    os.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveRecordDB(String nick, VBox table) {
        DB.add(nick, timer.getTime(), numLvl);
        DB.writeToTable(table, numLvl);
    }

    public void clearRecordTable(VBox table) {
        for (int i = 0; i < 10; i++) {
            HBox line = (HBox) table.getChildren().get(i + 1);
            Label nickname = (Label) line.getChildren().get(1);
            Label time = (Label) line.getChildren().get(2);
            nickname.setText("-");
            time.setText("-");
        }
    }

    public void resetLvl(boolean isRestart) {
        Rectangle rct = new Rectangle(0, 0, WIDTH, HEIGHT);
        rct.setFill(Color.WHITE);
        gameController.getAnchorPane().getChildren().add(rct);

        FadeTransition anim = new FadeTransition(Duration.millis(1500),rct);
        anim.setFromValue(0.0);
        anim.setToValue(1.0);
        anim.play();

        anim.setOnFinished(e -> {
            timer.stop();
            if (isRestart)
                restartLvl(gameController.getGameField());
            else loadLvl(gameController.getGameField(), numLvl);
            FadeTransition anim1 = new FadeTransition(Duration.millis(1500),rct);
            anim1.setFromValue(1.0);
            anim1.setToValue(0.0);
            anim1.play();
            anim1.setOnFinished(ev -> gameController.getAnchorPane().getChildren().remove(rct));
        });
    }

    public void clearLvl(Pane field) {
        field.getChildren().clear();
        key = null;
        chest = null;
        listBoxAndEnemy.clear();
        listSpike.clear();
        gameController.getStatusHelp().setTextFill(Color.WHITE);
        gameController.getWinWindow().setVisible(false);
        gameController.getMenuWindow().setVisible(false);
    }

    public void loadLvl(Pane field, int num) {
        clearLvl(field);
        numLvl = num;
        if (numLvl == 6) startMenu();
        else {
            //create map
            File fileMap = new File("levelConfig/maps/" + numLvl + ".conf");
            field.getChildren().add(createGameField(fileMap));
            //create blocks and hero
            File fileBlock = new File("levelConfig/blocks/" + numLvl + ".conf");
            field.getChildren().add(createElements(fileBlock));
            //create local level timer
            timer = new Timers(gameController.getStatusTimer(), this);
        }
    }

    public void restartLvl(Pane field) {
        clearLvl(field);
        File fileMap = new File("levelConfig/maps/" + numLvl + ".conf");
        field.getChildren().add(createGameField(fileMap));
        File fileBlock = new File("levelConfig/blocks/" + numLvl + ".conf");
        field.getChildren().add(createElements(fileBlock));
        if (isSingleGame) timer = new Timers(gameController.getStatusTimer(), this);
    }

    public GridPane createGameField(File fileMap) {
        try {
            //find matrix height
            BufferedReader inLen = new BufferedReader((new FileReader(fileMap.getAbsoluteFile())));
            int i = 0;
            while (inLen.readLine() != null) i++;
            matrix = new char[i][];
            inLen.close();
            //create matrix
            BufferedReader in = new BufferedReader((new FileReader(fileMap.getAbsoluteFile())));
            String str;
            int j = 0;
            while ((str = in.readLine()) != null)
                matrix[j++] = str.toCharArray();
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
                    case '0':
                        grid.add(new ImageView(imgFloor), i, j);
                        break;
                    case '1':
                        grid.add(new ImageView(imgWall1), i, j);
                        break;
                    case '2':
                        grid.add(new ImageView(imgWall2), i, j);
                        break;
                    case '3':
                        grid.add(new ImageView(imgWall3), i, j);
                        break;
                    case '4':
                        grid.add(new ImageView(imgWall4), i, j);
                        break;
                    case '5':
                        grid.add(new ImageView(imgWall5), i, j);
                        break;
                    case '6':
                        grid.add(new ImageView(imgWall6), i, j);
                        break;
                    case '7':
                        grid.add(new ImageView(imgWall7), i, j);
                        break;
                    case '8':
                        grid.add(new ImageView(imgWall8), i, j);
                        break;
                    case '9':
                        grid.add(new ImageView(imgColumn9), i, j);
                        break;
                    case 'e':
                        grid.add(new ImageView(imgExit), i, j);
                        break;
                    case 'E':
                        grid.add(new ImageView(imgExitEnd), i, j);
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
                        hero = new Hero(parseInt(strSplit[1]), parseInt(strSplit[2]), parseInt(strSplit[3]), img, this);
                        break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (key != null) elemBox.getChildren().add(key);
        if (chest != null) elemBox.getChildren().add(chest);
        elemBox.getChildren().addAll(spikeBox, enemyBox, boxBox, hero.getImg());
        elemBox.setTranslateX((WIDTH - gWidth) / 2d);
        elemBox.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
        return elemBox;
    }

    public void closeHelp() {
        gameController.setStatusHelp(true, numLvl);
        isOpenHelp = false;
    }
    public void reverseOpenWindow() { isOpenWindow = !isOpenWindow; }
    public void reverseSingleGame() { isSingleGame = !isSingleGame; }

    public boolean isSingleGame() { return isSingleGame; }
    public boolean isOpenWindow() { return isOpenWindow; }
    public GameController getGameController() { return gameController; }
    public MenuController getMenuController() { return menuController; }
    public int getNumLvl() { return numLvl; }
    public DataBase getDB() { return DB; }
    public Hero getHero() { return hero; }
    public ArrayList<Block> getListBoxAndEnemy() { return listBoxAndEnemy; }
    public ArrayList<Block> getListSpike() { return listSpike; }
    public char[][] getMatrix() { return matrix; }
    public ImageView getKey() { return key; }
    public ImageView getChest() { return chest; }
}
