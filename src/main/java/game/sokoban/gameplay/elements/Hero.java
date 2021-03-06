package game.sokoban.gameplay.elements;

import game.sokoban.gameplay.LvlChanger;
import game.sokoban.gameplay.services.Sounds;
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

    private final int blockSize = Block.blockSize;
    private int x, y;
    private int keyX, keyY;
    private int chestX, chestY;
    private final int animTime = 100;
    private final Image redImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/tile_0164.png")), 0.7*blockSize, 0.7*blockSize, false, true);
    private final ImageView node;
    private int turns = 1000;
    private boolean isKey = false;
    private Sounds player = new Sounds();


    public Hero(int posX, int posY, int countTurns, ImageView img, LvlChanger lvlChanger) {
        x = posX;
        y = posY;
        if (lvlChanger.isSingleGame()) turns = countTurns;
        node = img;
        node.setTranslateX(x * blockSize + 0.15 * blockSize);
        node.setTranslateY(y * blockSize + 0.15 * blockSize);
        lvlChanger.getGameController().setTurnsLeft(turns);
    }

    public void move(int posX, int posY, LvlChanger lvlChanger) {
        ArrayList<Block> listBoxAndEnemy = lvlChanger.getListBoxAndEnemy();
        ArrayList<Block> listSpike = lvlChanger.getListSpike();
        char[][] matrix = lvlChanger.getMatrix();
        if (turns > 0) {
            boolean isFree = true;
            //check boxes and enemies
            for (Block block : listBoxAndEnemy) {
                isFree = checkBlock(posX, posY, block, lvlChanger);
                if (!isFree) break;
            }
            //check spikes
            for (Block spike : listSpike) {
                if (posX == spike.getX() && posY == spike.getY() && isFree) {
                    spikeAnimation(posX, posY);
                    turns--;
                    player.playSpikeSound();
                }
            }
            //check chest if hero has key
            if (posX == chestX && posY == chestY && isKey) {
                Pane pane = (Pane) lvlChanger.getGameController().getGameField().getChildren().get(1);
                pane.getChildren().remove(lvlChanger.getChest());
                player.playChestSound();
            }
            //check chest if hero hasn't key
            if (posX == chestX && posY == chestY && !isKey) {
                isFree = false;
            }
            //check key
            if (posX == keyX && posY == keyY && isFree) {
                isKey = true;
                Pane pane = (Pane) lvlChanger.getGameController().getGameField().getChildren().get(1);
                pane.getChildren().remove(lvlChanger.getKey());
                player.playChestSound();
            }
            //if free then hero moves
            if (isFree && matrix[posX][posY] == '0') {
                moveAnimation(posX, posY, blockSize, animTime, false, node);
                x = posX;
                y = posY;
                player.playMoveSound();
            }
            else if (matrix[posX][posY] == 'e' || matrix[posX][posY] == 'E') winAnimation(posX, posY, lvlChanger);
            else {
                player.playKickSound();
                kickAnimation(posX,posY);
            }
            turns--;
            lvlChanger.getGameController().setTurnsLeft(turns);
        }
        else {
            lvlChanger.resetLvl(false);
            player.playGameOverSound();
        }
    }

    public boolean checkBlock(int posX, int posY, Block block, LvlChanger lvlChanger) {
        if ((posX == block.getX()) && (posY == block.getY()) && (block.getIsAlive())) {
            if (posX < x)
                block.move(posX - 1, posY, lvlChanger);
            if (posX > x)
                block.move(posX + 1, posY, lvlChanger);
            if (posY < y)
                block.move(posX, posY - 1, lvlChanger);
            if (posY > y)
                block.move(posX, posY + 1, lvlChanger);
            return false;
        }
        return true;
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
        damage.setToValue(0.6);
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

    public int getX() { return x; }
    public int getY() { return y; }
    public ImageView getImg() { return node; }
    public int getAnimTime() { return animTime; }
    public void setKeyX(int x) { keyX = x; }
    public void setKeyY(int y) { keyY = y; }
    public void setChestX(int x) { chestX = x; }
    public void setChestY(int y) { chestY = y; }
}
