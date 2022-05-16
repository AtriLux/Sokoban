package game.sokoban.elements.blocks;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Box extends Block {

    public Box(int posX, int posY, ImageView img) {
        super(posX, posY, img);
        errorX = -0.05*blockSize;
        errorY = 0;
        node.setTranslateX(posX * blockSize + errorX);
        node.setTranslateY(posY * blockSize + errorY);
        imgAfterKick = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/table2.png")), blockSize, blockSize, false, true);
    }
}
