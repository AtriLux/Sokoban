package game.sokoban.elements;

import game.sokoban.LvlChanger;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Hero {

    private int blockSize = Block.blockSize;
    private int x, y;
    private int winX, winY;
    private int keyX, keyY;
    private int chestX, chestY;
    private double errPos = 0.15*blockSize;
    private ImageView node;
    private int turns;
    private boolean isKey = false;

    public Hero(int posX, int posY, int countTurns, ImageView img) {
        x = posX;
        y = posY;
        turns = countTurns;
        node = img;
        node.setTranslateX(x * blockSize + errPos);
        node.setTranslateY(y * blockSize + errPos);
    }

    public void move(int posX, int posY, LvlChanger lvlChanger) {
        ArrayList<Block> listBoxAndEnemy = lvlChanger.getListBoxAndEnemy();
        ArrayList<Block> listSpike = lvlChanger.getListSpike();
        int[][] matrix = lvlChanger.getMatrix();
        if (turns > 0) {
            int isFree = 0;
            for (Block block : listBoxAndEnemy) {
                isFree += checkBlock(posX, posY, block, lvlChanger);
            }
            for (Block spike : listSpike) {
                if (posX == spike.getX() && posY == spike.getY()) turns--;
            }
            if (posX == chestX && posY == chestY && isKey) {
                Pane pane = (Pane) lvlChanger.getGameController().getGameField().getChildren().get(1);
                pane.getChildren().remove(lvlChanger.getChest());
            }
            if (posX == chestX && posY == chestY && !isKey) {
                isFree++;
            }
            if (posX == keyX && posY == keyY) {
                isKey = true;
                Pane pane = (Pane) lvlChanger.getGameController().getGameField().getChildren().get(1);
                pane.getChildren().remove(lvlChanger.getKey());
            }
            if ((isFree == 0) && (matrix[posX][posY] == 0)) {
                x = posX;
                y = posY;
                node.setTranslateX(x * blockSize + errPos);
                node.setTranslateY(y * blockSize + errPos);
                if ((posX == winX) && (posY == winY)) {
                    //TODO add disappear animation of hero
                    lvlChanger.winLevel();
                }
            }
            turns--;
            lvlChanger.getGameController().setTurnsLeft(turns);
        }
        else die(lvlChanger);
    }

    public int checkBlock(int posX, int posY, Block block, LvlChanger lvlChanger) {
        if ((posX == block.getX()) && (posY == block.getY()) && (block.getIsAlive())) {
            if (posX < x) {
                block.move(posX - 1, posY, lvlChanger);
            }
            if (posX > x) {
                block.move(posX + 1, posY, lvlChanger);
            }
            if (posY < y) {
                block.move(posX, posY - 1, lvlChanger);
            }
            if (posY > y) {
                block.move(posX, posY + 1, lvlChanger);
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
    public boolean getIsKey() { return isKey; }
    public void setWinX(int x) { winX = x; }
    public void setWinY(int y) { winY = y; }
    public void setKeyX(int x) { keyX = x; }
    public void setKeyY(int y) { keyY = y; }
    public void setChestX(int x) { chestX = x; }
    public void setChestY(int y) { chestY = y; }
}
