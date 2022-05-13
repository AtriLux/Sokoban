package game.sokoban.elements.blocks;

import javafx.scene.image.ImageView;

public class Box extends Block {

    public Box(int posX, int posY, ImageView img) {
        super(posX, posY, img);
        node.setTranslateX(posX * blockSize - 4);
        node.setTranslateY(posY * blockSize);
    }

}
