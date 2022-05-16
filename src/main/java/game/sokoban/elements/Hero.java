package game.sokoban.elements;

import game.sokoban.LvlChanger;
import game.sokoban.elements.blocks.Block;
import game.sokoban.elements.blocks.Box;
import game.sokoban.elements.blocks.Enemy;
import game.sokoban.elements.blocks.Spike;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Hero {

    private int blockSize = Block.blockSize;
    private int x, y;
    private int winX, winY;
    private ImageView node;
    private int turns;

    public Hero(int posX, int posY, int countTurns, ImageView img) {
        x = posX;
        y = posY;
        turns = countTurns;
        node = img;
        node.setTranslateX(x * blockSize + 0.15*blockSize);
        node.setTranslateY(y * blockSize - 0.02*blockSize);
    }

    public void move(int posX, int posY, int[][] matrix, LvlChanger lvlChanger, ArrayList<Box> listBox, ArrayList<Enemy> listEnemy, ArrayList<Spike> listSpike) {
        if (turns > 0) {
            int isFree = 0;
            for (Box box : listBox) {
                isFree += checkBlock(posX, posY, box, matrix, listBox, listEnemy, listSpike);
            }
            for (Enemy enemy : listEnemy) {
                isFree += checkBlock(posX, posY, enemy, matrix, listBox, listEnemy, listSpike);
            }
            for (Spike spike : listSpike) {
                if (posX == spike.getX() && posY == spike.getY()) turns--;
            }
            if ((isFree == 0) && (matrix[posX][posY] != 1)) {
                x = posX;
                y = posY;
                node.setTranslateX(x * blockSize + 7);
                node.setTranslateY(y * blockSize - 1);
                if ((posX == winX) && (posY == winY)) {
                    //TODO add img "happy (win) hero"
                    lvlChanger.winGame();
                }
            }
            turns--;
            lvlChanger.getGameController().setTurnsLeft(turns);
        }
        else die(lvlChanger);
    }

    public int checkBlock(int posX, int posY, Block block, int[][] matrix, ArrayList<Box> listBox, ArrayList<Enemy> listEnemy, ArrayList<Spike> listSpike) {
        if ((posX == block.getX()) && (posY == block.getY()) && (block.getIsAlive())) {
            if (posX < x) {
                block.move(posX - 1, posY, matrix, listBox, listEnemy, listSpike);
            }
            if (posX > x) {
                block.move(posX + 1, posY, matrix, listBox, listEnemy, listSpike);
            }
            if (posY < y) {
                block.move(posX, posY - 1, matrix, listBox, listEnemy, listSpike);
            }
            if (posY > y) {
                block.move(posX, posY + 1, matrix, listBox, listEnemy, listSpike);
            }
            return 1;
        }
        return 0;
    }

    public void die(LvlChanger lvlChanger) {
        //TODO add img "dead hero"
        lvlChanger.loadLvl(lvlChanger.getGameController().getGameField(), lvlChanger.getNumLvl());
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public ImageView getImg() { return node; }
    public void setWinX(int x) { winX = x; }
    public void setWinY(int y) { winY = y; }
}
