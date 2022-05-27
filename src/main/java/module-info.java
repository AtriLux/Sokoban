module game.sokoban {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens game.sokoban to javafx.fxml;
    exports game.sokoban;
    exports game.sokoban.elements;
    opens game.sokoban.elements to javafx.fxml;
    exports game.sokoban.controllers;
    opens game.sokoban.controllers to javafx.fxml;
}