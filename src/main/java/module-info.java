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
    requires org.postgresql.jdbc;

    opens game.sokoban to javafx.fxml;
    exports game.sokoban;
    exports game.sokoban.gameplay.elements;
    opens game.sokoban.gameplay.elements to javafx.fxml;
    exports game.sokoban.gameplay.controllers;
    opens game.sokoban.gameplay.controllers to javafx.fxml;
    exports game.sokoban.clientServer;
    opens game.sokoban.clientServer to javafx.fxml;
    exports game.sokoban.gameplay;
    opens game.sokoban.gameplay to javafx.fxml;
}