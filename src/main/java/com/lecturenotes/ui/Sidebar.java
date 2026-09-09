package com.lecturenotes.ui;

import com.lecturenotes.model.Folder;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class Sidebar extends VBox {

    private final Button newNoteButton;
    private final VBox folderContainer;

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

        Label foldersTitle = new Label("Folders");
        foldersTitle.getStyleClass().add("sidebar-item");

        folderContainer = new VBox(4);

        getChildren().addAll(
            title,
            newNoteButton,
            notes,
            recent,
            foldersTitle,
            folderContainer
        );
    }

    private Label createItem(String text) {

        Label item = new Label(text);
        item.getStyleClass().add("sidebar-item");

        return item;
    }

    public void setFolders(List<Folder> folders) {

        folderContainer.getChildren().clear();

        for (Folder folder : folders) {

            Label folderItem = new Label(
                "▸  " + folder.getName()
            );

            folderItem.getStyleClass().add(
                "sidebar-item"
            );

            folderItem.setMaxWidth(
                Double.MAX_VALUE
            );

            folderContainer.getChildren().add(
                folderItem
            );
        }
    }

    public Button getNewNoteButton() {
        return newNoteButton;
    }
}


