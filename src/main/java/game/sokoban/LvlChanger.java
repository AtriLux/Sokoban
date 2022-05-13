package game.sokoban;

import game.sokoban.elements.Hero;
import game.sokoban.elements.blocks.Box;
import game.sokoban.elements.blocks.Enemy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class LvlChanger {

    private int sizeBlock = 60;
    private int WIDTH;
    private int HEIGHT;
    private int[][] matrix;
    private Hero hero;
    private ArrayList<Box> listBox = new ArrayList<>();
    private ArrayList<Enemy> listEnemy = new ArrayList<>();

    Image imgFloor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/floor2.png")), sizeBlock, sizeBlock, false, true);
    Image imgWall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/wall2.jpg")), sizeBlock, sizeBlock, false, true);
    Image imgTable = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/table.png")), sizeBlock, sizeBlock, false, true);
    Image imgBrokenTable = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/table2.png")), sizeBlock, sizeBlock, false, true);
    Image imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/enemy.png")), 0.75*sizeBlock, sizeBlock, false, true);
    Image imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hero.png")), 0.75*sizeBlock, sizeBlock, false, true);

    LvlChanger(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    public void loadLvl1(Controller controller) {
        //game field
        matrix = new int[10][];
        matrix[0] = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        matrix[1] = new int[]{1, 0, 0, 0, 0, 0, 0, 1};
        matrix[2] = new int[]{1, 0, 0, 0, 0, 0, 0, 1};
        matrix[3] = new int[]{1, 0, 1, 1, 0, 0, 0, 1};
        matrix[4] = new int[]{1, 0, 1, 1, 0, 1, 0, 1};
        matrix[5] = new int[]{1, 0, 1, 1, 0, 0, 0, 1};
        matrix[6] = new int[]{1, 0, 0, 0, 0, 0, 1, 1};
        matrix[7] = new int[]{1, 0, 0, 0, 0, 0, 0, 1};
        matrix[8] = new int[]{1, 0, 0, 0, 0, 1, 0, 1};
        matrix[9] = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        //create blocks into grid
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
        //add to field
        controller.getField().getChildren().add(grid);
        //grid to center
        int gWidth = grid.getColumnCount() * sizeBlock;
        int gHeight = grid.getRowCount() * sizeBlock;
        grid.setTranslateX((WIDTH - gWidth) / 2d);
        grid.setTranslateY((HEIGHT - 50 - gHeight) / 2d);

        //add box to containers
        Pane elemBox = new Pane();
        File file = new File("blocksLvl1.conf");
        try {
            BufferedReader in = new BufferedReader((new FileReader(file.getAbsoluteFile())));
            String str;
            while ((str = in.readLine()) != null) {
                String[] strSplit = str.split(" ");
                if (strSplit[0].equals("table")) {
                    ImageView img = new ImageView(imgTable);
                    elemBox.getChildren().add(img);
                    listBox.add(new Box(parseInt(strSplit[1]),parseInt(strSplit[2]),img));
                }
                if (strSplit[0].equals("enemy")) {
                    ImageView img = new ImageView(imgEnemy);
                    elemBox.getChildren().add(img);
                    listEnemy.add(new Enemy(parseInt(strSplit[1]),parseInt(strSplit[2]),img));
                }
                if (strSplit[0].equals("hero")) {
                    ImageView img = new ImageView(imgHero);
                    elemBox.getChildren().add(img);
                    hero = new Hero(parseInt(strSplit[1]),parseInt(strSplit[2]),img);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //add container to gameField
        controller.getField().getChildren().add(elemBox);
        elemBox.setTranslateX((WIDTH - gWidth) / 2d);
        elemBox.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
    }

    public Hero getHero() { return hero; }

    public int[][] getMatrix() { return matrix; }

    public ArrayList<Box> getListBox() { return listBox; }

    public ArrayList<Enemy> getListEnemy() { return listEnemy; }
}
