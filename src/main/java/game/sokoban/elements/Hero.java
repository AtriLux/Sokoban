package game.sokoban.elements;

import game.sokoban.elements.blocks.Block;
import game.sokoban.elements.blocks.Box;
import game.sokoban.elements.blocks.Enemy;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Hero {

    private int blockSize = 60;
    private int x, y;
    private int winX, winY;
    private ImageView node;

    public Hero(int posX, int posY, ImageView img) {
        x = posX;
        y = posY;
        node = img;
        node.setTranslateX(x * blockSize + 7);
        node.setTranslateY(y * blockSize - 1);
    }

    public void move(int posX, int posY, int[][] matrix, ArrayList<Box> listBox, ArrayList<Enemy> listEnemy) {
        int isFree = 0;
        for (Box box: listBox) {
            isFree += checkBlock(posX, posY, box, matrix, listBox, listEnemy);
        }
        for (Enemy enemy: listEnemy) {
            isFree += checkBlock(posX, posY, enemy, matrix, listBox, listEnemy);
        }
        if ((isFree == 0) && (matrix[posX][posY] != 1)) {
            x = posX;
            y = posY;
            node.setTranslateX(x * blockSize + 7);
            node.setTranslateY(y * blockSize - 1);
            if ((posX == winX) && (posY == winY)) {
                System.out.println("You win!");
            }
        }
    }

    public int checkBlock(int posX, int posY, Block block, int[][] matrix, ArrayList<Box> listBox, ArrayList<Enemy> listEnemy) {
        if ((posX == block.getX()) && (posY == block.getY()) && (block.getIsAlive())) {
            if (posX < x) {
                block.move(posX - 1, posY, matrix, listBox, listEnemy);
            }
            if (posX > x) {
                block.move(posX + 1, posY, matrix, listBox, listEnemy);
            }
            if (posY < y) {
                block.move(posX, posY - 1, matrix, listBox, listEnemy);
            }
            if (posY > y) {
                block.move(posX, posY + 1, matrix, listBox, listEnemy);
            }
            return 1;
        }
        return 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setWinX(int x) { winX = x; }
    public void setWinY(int y) { winY = y; }
}
