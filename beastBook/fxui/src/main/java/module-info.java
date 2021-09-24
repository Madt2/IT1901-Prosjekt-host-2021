module beastBook.fxui {
    requires beastBook.core;
    requires javafx.controls;
    requires javafx.fxml;

    opens beastBook.fxui to javafx.graphics, javafx.fxml;
}
