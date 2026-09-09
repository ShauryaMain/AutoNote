package com.lecturenotes.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    private final Button newNoteButton;

    public Sidebar() {

        getStyleClass().add("sidebar");

        setPrefWidth(240);
        setPadding(new Insets(28, 20, 28, 20));
        setSpacing(8);

        Label title = new Label("AutoNote");
        title.getStyleClass().add("sidebar-title");

        newNoteButton = new Button("+  New Note");
        newNoteButton.getStyleClass().add("new-note-button");

        newNoteButton.setMaxWidth(Double.MAX_VALUE);

        Label notes = createItem("Notes");
        Label recent = createItem("Recent");
        Label folders = createItem("Folders");

        getChildren().addAll(
            title,
            newNoteButton,
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

    public Button getNewNoteButton() {
        return newNoteButton;
    }
}
