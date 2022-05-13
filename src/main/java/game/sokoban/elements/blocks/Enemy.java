package game.sokoban.elements.blocks;

import javafx.scene.image.ImageView;

public class Enemy extends Block {

    public Enemy(int posX, int posY, ImageView img) {
        super(posX, posY, img);
        node.setTranslateX(posX * blockSize + 7);
        node.setTranslateY(posY * blockSize - 1);
    }
}
