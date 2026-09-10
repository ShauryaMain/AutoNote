package com.lecturenotes.ui;

import com.lecturenotes.model.Folder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Sidebar extends VBox {

    private final Button newNoteButton;
    private final Button newFolderButton;
    private final VBox folderContainer;

    private Consumer<Folder> folderSelected;
    private Runnable notesSelected;

    private Folder selectedFolder;

    public Sidebar() {

        getStyleClass().add("sidebar");

        setPrefWidth(250);
        setPadding(new Insets(28, 20, 28, 20));
        setSpacing(8);

        Label title = new Label("AutoNote");
        title.getStyleClass().add("sidebar-title");

        newNoteButton = new Button("+  New Note");

        newNoteButton.getStyleClass().add(
            "new-note-button"
        );

        newNoteButton.setMaxWidth(
            Double.MAX_VALUE
        );

        // Notes button

        Button notes = new Button("Notes");

        notes.getStyleClass().add(
            "sidebar-item"
        );

        notes.setMaxWidth(
            Double.MAX_VALUE
        );

        notes.setAlignment(
            Pos.CENTER_LEFT
        );

        notes.setOnAction(event -> {

            selectedFolder = null;

            if (notesSelected != null) {
                notesSelected.run();
            }
        });

        // Recent

        Label recent = createItem("Recent");

        // Folders

        Label foldersTitle =
            new Label("Folders");

        foldersTitle.getStyleClass().add(
            "sidebar-item"
        );

        newFolderButton = new Button(
            "+  New Folder"
        );

        newFolderButton.getStyleClass().add(
            "new-folder-button"
        );

        newFolderButton.setMaxWidth(
            Double.MAX_VALUE
        );

        folderContainer = new VBox(3);

        getChildren().addAll(
            title,
            newNoteButton,
            notes,
            recent,
            foldersTitle,
            newFolderButton,
            folderContainer
        );
    }

    private Label createItem(String text) {

        Label item = new Label(text);

        item.getStyleClass().add(
            "sidebar-item"
        );

        item.setMaxWidth(
            Double.MAX_VALUE
        );

        return item;
    }

    public void setFolders(
        List<Folder> folders
    ) {

        folderContainer
            .getChildren()
            .clear();

        buildTree(
            folders,
            null,
            0
        );
    }

    private void buildTree(
        List<Folder> folders,
        String parentId,
        int depth
    ) {

        for (Folder folder : folders) {

            if (parentId == null) {

                if (folder.getParentId() != null) {
                    continue;
                }

            } else if (!parentId.equals(
                folder.getParentId()
            )) {

                continue;
            }

            Button folderButton =
                new Button();

            folderButton.getStyleClass().add(
                "folder-item"
            );

            folderButton.setMaxWidth(
                Double.MAX_VALUE
            );

            folderButton.setAlignment(
                Pos.CENTER_LEFT
            );

            folderButton.setPadding(
                new Insets(
                    7,
                    8,
                    7,
                    8 + (depth * 18)
                )
            );

            boolean hasChildren =
                hasChildren(
                    folders,
                    folder.getId()
                );

            folderButton.setText(
                (hasChildren ? "▸  " : "•  ")
                    + folder.getName()
            );

            folderButton.setOnAction(event -> {

                selectedFolder = folder;

                if (folderSelected != null) {
                    folderSelected.accept(folder);
                }
            });

            folderContainer.getChildren().add(
                folderButton
            );

            if (hasChildren) {

                buildTree(
                    folders,
                    folder.getId(),
                    depth + 1
                );
            }
        }
    }

    private boolean hasChildren(
        List<Folder> folders,
        String parentId
    ) {

        for (Folder folder : folders) {

            if (parentId.equals(
                folder.getParentId()
            )) {

                return true;
            }
        }

        return false;
    }

    public Button getNewNoteButton() {
        return newNoteButton;
    }

    public Button getNewFolderButton() {
        return newFolderButton;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public void setOnFolderSelected(
        Consumer<Folder> callback
    ) {

        this.folderSelected = callback;
    }

    public void setOnNotesSelected(
        Runnable callback
    ) {

        this.notesSelected = callback;
    }

    public Optional<String> requestFolderName() {

        TextInputDialog dialog =
            new TextInputDialog();

        dialog.setTitle("New Folder");

        dialog.setHeaderText(
            "Create a new folder"
        );

        dialog.setContentText(
            "Folder name:"
        );

        return dialog.showAndWait();
    }
}
