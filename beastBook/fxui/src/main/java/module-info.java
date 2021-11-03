module beastbook.fxui {
    requires beastbook.core;
    requires javafx.controls;
    requires javafx.fxml;

    opens beastbook.fxui to javafx.fxml, javafx.graphics;
}
