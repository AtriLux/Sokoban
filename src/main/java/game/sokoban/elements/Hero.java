package game.sokoban.elements;

import game.sokoban.elements.blocks.Box;
import game.sokoban.elements.blocks.Enemy;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Hero {

    private int blockSize = 60;
    private int x;
    private int y;
    private ImageView node;

    public Hero(int posX, int posY, ImageView img) {
        x = posX;
        y = posY;
        node = img;
        node.setTranslateX(x * blockSize + 7);
        node.setTranslateY(y * blockSize - 1);
    }

    public void move(int posX, int posY, int[][] matrix, ArrayList<Box> listBox, ArrayList<Enemy> listEnemy) {
        boolean isFree = true;
        for (Box box: listBox) {
            if ((posX == box.getX()) && (posY == box.getY())) {
                if (posX < x) {
                    box.move(posX - 1, posY, matrix, listBox, listEnemy);
                }
                if (posX > x) {
                    box.move(posX + 1, posY, matrix, listBox, listEnemy);
                }
                if (posY < y) {
                    box.move(posX, posY - 1, matrix, listBox, listEnemy);
                }
                if (posY > y) {
                    box.move(posX, posY + 1, matrix, listBox, listEnemy);
                }
                isFree = false;
            }
        }
        for (Enemy enemy: listEnemy) {
            if ((posX == enemy.getX()) && (posY == enemy.getY())) {
                if (posX < x) {
                    enemy.move(posX - 1, posY, matrix, listBox, listEnemy);
                }
                if (posX > x) {
                    enemy.move(posX + 1, posY, matrix, listBox, listEnemy);
                }
                if (posY < y) {
                    enemy.move(posX, posY - 1, matrix, listBox, listEnemy);
                }
                if (posY > y) {
                    enemy.move(posX, posY + 1, matrix, listBox, listEnemy);
                }
                isFree = false;
            }
        }
        if ((matrix[posX][posY] == 0) && (isFree)) {
            x = posX;
            y = posY;
            node.setTranslateX(x * blockSize + 7);
            node.setTranslateY(y * blockSize - 1);
        }
    }

    public void setX(int posX) { x = posX; }
    public void setY(int posY) { y = posY; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return blockSize; }
}
