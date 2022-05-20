package game.sokoban.elements;

import game.sokoban.LvlChanger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Objects;

public class Block {
    public static int blockSize = 80;
    String type;
    int x, y; // coords on field
    double errPos = 0.15*blockSize;
    ImageView node;
    Image imgDead = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0005.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    boolean isAlive = true;

    public Block(int posX, int posY, ImageView img, String typeName) {
        x = posX;
        y = posY;
        type = typeName;
        node = img;
        node.setTranslateX(posX * blockSize + errPos);
        node.setTranslateY(posY * blockSize + errPos);
    }

    public void move(int posX, int posY, LvlChanger lvlChanger) {
        ArrayList<Block> listBoxAndEnemy = lvlChanger.getListBoxAndEnemy();
        ArrayList<Block> listSpike = lvlChanger.getListSpike();
        int[][] matrix = lvlChanger.getMatrix();
        boolean isFree = true;
        for (Block block: listBoxAndEnemy) {
            if (posX == block.getX() && posY == block.getY() && isAlive) {
                isFree = false;
            }
        }
        for (Block spike: listSpike) {
            if (posX == spike.getX() && posY == spike.getY()) {
                //if (type == "Box") isFree = false;
                if (type == "Enemy") kill();
            }
        }
        if ((matrix[posX][posY] == 0) && (isFree)) {
            x = posX;
            y = posY;
            node.setTranslateX(x * blockSize + errPos);
            node.setTranslateY(y * blockSize + errPos);
        }
        else if (matrix[posX][posY] != 0 && type == "Enemy") kill();
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean getIsAlive() { return isAlive; }
    public void kill() {
        node.setImage(imgDead);
        isAlive = false;
    }
}
