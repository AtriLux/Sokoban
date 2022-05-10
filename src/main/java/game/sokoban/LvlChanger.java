package game.sokoban;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class LvlChanger {

    private int sizeBlock = 50;
    private int WIDTH;
    private int HEIGHT;
    private int[][] matrix;
    Image imgFloor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/floor.png")), sizeBlock, sizeBlock, false, true);
    Image imgWall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/wall2.png")), sizeBlock, sizeBlock, false, true);

    LvlChanger(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
    }

    public void loadLvl1(Controller controller) {
        //game field
        matrix = new int[10][];
        matrix[0] = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        matrix[1] = new int[]{1, 1, 1, 1, 0, 0, 0, 1};
        matrix[2] = new int[]{1, 0, 0, 0, 0, 0, 0, 1};
        matrix[3] = new int[]{1, 0, 1, 1, 0, 0, 0, 1};
        matrix[4] = new int[]{1, 0, 1, 1, 0, 1, 0, 1};
        matrix[5] = new int[]{1, 0, 1, 1, 0, 0, 0, 1};
        matrix[6] = new int[]{1, 0, 0, 0, 0, 0, 1, 1};
        matrix[7] = new int[]{1, 1, 0, 0, 0, 0, 0, 1};
        matrix[8] = new int[]{1, 1, 0, 0, 0, 1, 0, 1};
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
        grid.setVgap(-1);
        grid.setHgap(-1);
        //add to field
        controller.getField().getChildren().add(grid);
        //grid to center
        int gWidth = grid.getColumnCount() * sizeBlock;
        int gHeight = grid.getRowCount() * sizeBlock;
        grid.setTranslateX((WIDTH - gWidth) / 2d);
        grid.setTranslateY((HEIGHT - 50 - gHeight) / 2d);
    }
}
