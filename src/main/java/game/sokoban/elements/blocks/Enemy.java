package game.sokoban.elements.blocks;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Enemy extends Block {

    public Enemy(int posX, int posY, ImageView img) {
        super(posX, posY, img);
        errorX = 7;
        errorY = -1;
        node.setTranslateX(posX * blockSize + errorX);
        node.setTranslateY(posY * blockSize + errorY);
        imgAfterKick = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/skull.png")), 0.5*blockSize, 0.5*blockSize, false, true);
    }
}
