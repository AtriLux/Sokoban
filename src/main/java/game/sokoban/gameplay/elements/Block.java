package game.sokoban.gameplay.elements;

import game.sokoban.gameplay.LvlChanger;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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
        if (type.equals("Spike")) errPos = 0.05*blockSize;
        node.setTranslateX(posX * blockSize + errPos);
        node.setTranslateY(posY * blockSize + errPos);
    }

    public void move(int posX, int posY, LvlChanger lvlChanger) {
        ArrayList<Block> listBoxAndEnemy = lvlChanger.getListBoxAndEnemy();
        ArrayList<Block> listSpike = lvlChanger.getListSpike();
        char[][] matrix = lvlChanger.getMatrix();
        boolean isFree = true;

        for (Block block: listBoxAndEnemy)
            if (posX == block.getX() && posY == block.getY() && isAlive) isFree = false;

        for (Block spike: listSpike)
            if (posX == spike.getX() && posY == spike.getY() && type.equals("Enemy")) kill();

        if ((matrix[posX][posY] == '0') && (isFree)) {
            moveAnimation(posX, posY, lvlChanger);
            x = posX;
            y = posY;
        }
        else if (matrix[posX][posY] != '0' && type.equals("Enemy") && isFree) kill();
    }

    private void moveAnimation(int posX, int posY, LvlChanger lvlChanger) {
        TranslateTransition anim = new TranslateTransition();
        anim.setDuration(Duration.millis(lvlChanger.getHero().getAnimTime()));
        anim.setNode(node);
        if (posX < x) anim.setByX(-blockSize);
        if (posX > x) anim.setByX(blockSize);
        if (posY < y) anim.setByY(-blockSize);
        if (posY > y) anim.setByY(blockSize);
        anim.play();
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean getIsAlive() { return isAlive; }
    public void kill() {
        node.setImage(imgDead);
        isAlive = false;
    }
}
