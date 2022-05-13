package game.sokoban.elements.blocks;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class Block {
    int blockSize = 60;
    int x, y; // coords on field
    int errorX, errorY; // image has different size
    ImageView node;
    Image imgAfterKick;
    boolean isAlive = true;

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
        if ((matrix[posX][posY] != 1) && (isFree)) {
            x = posX;
            y = posY;
            node.setTranslateX(x * blockSize + errorX);
            node.setTranslateY(y * blockSize + errorY);
        }
        else if (matrix[posX][posY] == 1) {
            node.setImage(getImg());
            if (this instanceof Enemy) kill();
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Image getImg() { return imgAfterKick; }

    public boolean getIsAlive() { return isAlive; }
    public void kill() { isAlive = false; }
}
