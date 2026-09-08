package com.lecturenotes.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public Sidebar() {
        setPrefWidth(240);
        setPadding(new Insets(28, 20, 28, 20));
        setSpacing(18);

        Label title = new Label("AutoNote");
        title.setStyle("""
            -fx-font-size: 22px;
            -fx-font-weight: bold;
        """);

        Label notes = new Label("Notes");
        Label recent = new Label("Recent");
        Label folders = new Label("Folders");

        getChildren().addAll(
            title,
            notes,
            recent,
            folders
        );
    }
}
