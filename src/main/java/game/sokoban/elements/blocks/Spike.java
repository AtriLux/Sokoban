package game.sokoban.elements.blocks;

import javafx.scene.image.ImageView;

public class Spike extends Block {

    public Spike(int posX, int posY, ImageView img) {
        super(posX, posY, img);
        errorX = 0;
        errorY = 0.4*blockSize;
        node.setTranslateX(posX * blockSize + errorX);
        node.setTranslateY(posY * blockSize + errorY);
    }
}
