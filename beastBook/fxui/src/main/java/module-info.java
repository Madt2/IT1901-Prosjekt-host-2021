module beastbook.fxui {
    requires beastbook.core;
    requires beastbook.rest;
    requires javafx.controls;
    requires javafx.fxml;

    opens beastbook.fxui to javafx.fxml, javafx.graphics;
}
