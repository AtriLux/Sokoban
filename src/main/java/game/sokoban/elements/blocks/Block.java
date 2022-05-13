package game.sokoban.elements.blocks;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class Block {
    int blockSize = 60;
    int x;
    int y;
    ImageView node;

    Block(int posX, int posY, ImageView img) {
        x = posX;
        y = posY;
        node = img;
    }

    public void move(int posX, int posY, int[][] matrix, ArrayList<Box> listBox, ArrayList<Enemy> listEnemy) {
        boolean isFree = true;
        for (Box box: listBox) {
            if ((posX == box.getX()) && (posY == box.getY())) {
                isFree = false;
            }
        }
        for (Enemy enemy: listEnemy) {
            if ((posX == enemy.getX()) && (posY == enemy.getY())) {
                isFree = false;
            }
        }
        if ((matrix[posX][posY] == 0) && (isFree)) {
            x = posX;
            y = posY;
            node.setTranslateX(x * blockSize);
            node.setTranslateY(y * blockSize);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
