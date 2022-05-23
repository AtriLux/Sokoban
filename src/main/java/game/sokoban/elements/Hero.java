package game.sokoban.elements;

import game.sokoban.LvlChanger;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class Hero {

    private int blockSize = Block.blockSize;
    private int x, y;
    private int keyX, keyY;
    private int chestX, chestY;
    private double errPos = 0.15*blockSize;
    private int animTime = 100;
    private Image redImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0164.png")), 0.7*blockSize, 0.7*blockSize, false, true);
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
        char[][] matrix = lvlChanger.getMatrix();
        if (turns > 0) {
            int isFree = 0;
            //check boxes and enemies
            for (Block block : listBoxAndEnemy) {
                isFree += checkBlock(posX, posY, block, lvlChanger);
            }
            //check spikes
            for (Block spike : listSpike) {
                if (posX == spike.getX() && posY == spike.getY()) {
                    spikeAnimation(posX, posY);
                    turns--;
                }
            }
            //check chest if hero has key
            if (posX == chestX && posY == chestY && isKey) {
                Pane pane = (Pane) lvlChanger.getGameController().getGameField().getChildren().get(1);
                pane.getChildren().remove(lvlChanger.getChest());
            }
            //check chest if hero hasn't key
            if (posX == chestX && posY == chestY && !isKey) {
                isFree++;
            }
            //check key
            if (posX == keyX && posY == keyY) {
                isKey = true;
                Pane pane = (Pane) lvlChanger.getGameController().getGameField().getChildren().get(1);
                pane.getChildren().remove(lvlChanger.getKey());
            }
            //if free hero move
            if (isFree == 0 && matrix[posX][posY] == '0') {
                moveAnimation(posX, posY, blockSize, animTime, false, node);
                x = posX;
                y = posY;
            }
            else if (matrix[posX][posY] == 'e') winAnimation(posX, posY, lvlChanger);
            else kickAnimation(posX,posY);
            turns--;
            lvlChanger.getGameController().setTurnsLeft(turns);
        }
        else lvlChanger.loseLvl();
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

    private void moveAnimation(int posX, int posY, double distance, int time, boolean isKick, ImageView nd) {
        TranslateTransition anim = new TranslateTransition(Duration.millis(time), nd);

        if (isKick) anim.setDelay(Duration.millis(time));

        if (posX < x) {
            anim.setByX(-distance);
            if (isKick) anim.setFromX(node.getTranslateX() + distance);
        }
        if (posX > x) {
            anim.setByX(distance);
            if (isKick) anim.setFromX(node.getTranslateX() - distance);
        }
        if (posY < y) {
            anim.setByY(-distance);
            if (isKick) anim.setFromY(node.getTranslateY() + distance);
        }
        if (posY > y) {
            anim.setByY(distance);
            if (isKick) anim.setFromY(node.getTranslateY() - distance);
        }
        anim.play();
    }

    private void spikeAnimation(int posX, int posY) {
        //add red hero
        ImageView redNode = new ImageView(redImg);
        redNode.setTranslateX(node.getTranslateX());
        redNode.setTranslateY(node.getTranslateY());
        Pane pane = (Pane) node.getParent();
        pane.getChildren().add(redNode);
        node.toFront();
        //move red hero as default hero
        moveAnimation(posX, posY, blockSize, animTime, false, redNode);
        //show red hero
        FadeTransition damage = new FadeTransition(Duration.millis(animTime),node);
        damage.setDelay(Duration.millis(0.5*animTime));
        damage.setToValue(0.8);
        damage.setAutoReverse(true);
        damage.setCycleCount(2);
        damage.play();
        //delete red hero after animation
        damage.setOnFinished(e -> pane.getChildren().remove(redNode));
    }

    private void kickAnimation(int posX, int posY) {
        moveAnimation(posX, posY, 0.25*blockSize, animTime, false, node);
        moveAnimation(posX, posY, -0.25*blockSize, animTime, true, node);
    }

    private void winAnimation(int posX, int posY, LvlChanger lvlChanger) {
        moveAnimation(posX, posY, 1.5*blockSize, 3*animTime, false, node);
        ScaleTransition anim = new ScaleTransition(Duration.millis(3*animTime), node);
        anim.setByY(-0.2);
        anim.setByX(-0.2);
        anim.play();
        FadeTransition anim1 = new FadeTransition(Duration.millis(3*animTime), node);
        anim1.setToValue(0.0);
        anim1.play();
        anim1.setOnFinished(e -> lvlChanger.winLvl());
    }

    private void die(LvlChanger lvlChanger) {

        lvlChanger.loadLvl(lvlChanger.getGameController().getGameField(), lvlChanger.getNumLvl());
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public ImageView getImg() { return node; }
    public int getAnimTime() { return animTime; }
    public void setKeyX(int x) { keyX = x; }
    public void setKeyY(int y) { keyY = y; }
    public void setChestX(int x) { chestX = x; }
    public void setChestY(int y) { chestY = y; }
}
