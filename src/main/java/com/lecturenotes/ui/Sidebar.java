package com.lecturenotes.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public Sidebar() {

        getStyleClass().add("sidebar");

        setPrefWidth(240);
        setPadding(new Insets(28, 20, 28, 20));
        setSpacing(8);

        Label title = new Label("AutoNote");
        title.getStyleClass().add("sidebar-title");

        Label notes = createItem("Notes");
        Label recent = createItem("Recent");
        Label folders = createItem("Folders");

        getChildren().addAll(
            title,
            notes,
            recent,
            folders
        );
    }

    private Label createItem(String text) {

        Label item = new Label(text);
        item.getStyleClass().add("sidebar-item");

        return item;
    }
}
