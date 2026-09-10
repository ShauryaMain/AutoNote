package com.lecturenotes.ui;

import com.lecturenotes.model.Folder;
import com.lecturenotes.model.Note;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Sidebar extends VBox {

    private final Button newNoteButton;
    private final Button newFolderButton;
    private final VBox folderContainer;
    private final Button notesButton;

    private Consumer<Folder> folderSelected;
    private Consumer<Note> noteDropped;
    private Runnable notesSelected;

    private Folder selectedFolder;

    public Sidebar() {

        getStyleClass().add("sidebar");

        setPrefWidth(250);

        setPadding(
            new Insets(
                28,
                20,
                28,
                20
            )
        );

        setSpacing(8);

        // --------------------------------
        // Title
        // --------------------------------

        Label title =
            new Label("AutoNote");

        title.getStyleClass().add(
            "sidebar-title"
        );

        // --------------------------------
        // New Note
        // --------------------------------

        newNoteButton =
            new Button("+  New Note");

        newNoteButton.getStyleClass().add(
            "new-note-button"
        );

        newNoteButton.setMaxWidth(
            Double.MAX_VALUE
        );

        // --------------------------------
        // Notes
        // --------------------------------

        notesButton =
            new Button("Notes");

        notesButton.getStyleClass().add(
            "sidebar-item"
        );

        notesButton.setMaxWidth(
            Double.MAX_VALUE
        );

        notesButton.setAlignment(
            Pos.CENTER_LEFT
        );

        // Notes is a drop target

        notesButton.setOnDragOver(event -> {

            if (
                event.getGestureSource() != notesButton &&
                event.getDragboard().hasString()
            ) {

                event.acceptTransferModes(
                    TransferMode.MOVE
                );
            }

            event.consume();
        });

        notesButton.setOnDragEntered(event -> {

            if (event.getDragboard().hasString()) {

                notesButton.setStyle(
                    "-fx-background-color: #dedee3;"
                );
            }
        });

        notesButton.setOnDragExited(event -> {

            notesButton.setStyle("");
        });

        notesButton.setOnDragDropped(event -> {

            Dragboard dragboard =
                event.getDragboard();

            boolean success = false;

            if (dragboard.hasString()) {

                String noteId =
                    dragboard.getString();

                if (noteDropped != null) {

                    Note dummy =
                        new Note();

                    dummy.setId(noteId);

                    noteDropped.accept(dummy);

                    success = true;
                }
            }

            event.setDropCompleted(success);

            event.consume();
        });

        notesButton.setOnAction(event -> {

            selectedFolder = null;

            if (notesSelected != null) {
                notesSelected.run();
            }
        });

        // --------------------------------
        // Recent
        // --------------------------------

        Label recent =
            createItem("Recent");

        // --------------------------------
        // Folders
        // --------------------------------

        Label foldersTitle =
            new Label("Folders");

        foldersTitle.getStyleClass().add(
            "sidebar-item"
        );

        newFolderButton =
            new Button("+  New Folder");

        newFolderButton.getStyleClass().add(
            "new-folder-button"
        );

        newFolderButton.setMaxWidth(
            Double.MAX_VALUE
        );

        folderContainer =
            new VBox(3);

        // --------------------------------
        // Layout
        // --------------------------------

        getChildren().addAll(
            title,
            newNoteButton,
            notesButton,
            recent,
            foldersTitle,
            newFolderButton,
            folderContainer
        );
    }

    private Label createItem(
        String text
    ) {

        Label item =
            new Label(text);

        item.getStyleClass().add(
            "sidebar-item"
        );

        item.setMaxWidth(
            Double.MAX_VALUE
        );

        return item;
    }

    // --------------------------------
    // Build folder tree
    // --------------------------------

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

            } else if (
                !parentId.equals(
                    folder.getParentId()
                )
            ) {

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
                (hasChildren
                    ? "▸  "
                    : "•  ")
                + folder.getName()
            );

            // --------------------------------
            // Normal folder selection
            // --------------------------------

            folderButton.setOnAction(event -> {

                selectedFolder = folder;

                if (folderSelected != null) {

                    folderSelected.accept(
                        folder
                    );
                }
            });

            // --------------------------------
            // Drag over folder
            // --------------------------------

            folderButton.setOnDragOver(event -> {

                if (
                    event.getGestureSource()
                        != folderButton &&
                    event.getDragboard()
                        .hasString()
                ) {

                    event.acceptTransferModes(
                        TransferMode.MOVE
                    );
                }

                event.consume();
            });

            // --------------------------------
            // Drag entered
            // --------------------------------

            folderButton.setOnDragEntered(event -> {

                if (
                    event.getDragboard()
                        .hasString()
                ) {

                    folderButton.setStyle(
                        "-fx-background-color: #dedee3;" +
                        "-fx-text-fill: #18181b;"
                    );
                }
            });

            // --------------------------------
            // Drag exited
            // --------------------------------

            folderButton.setOnDragExited(event -> {

                folderButton.setStyle("");
            });

            // --------------------------------
            // Drop
            // --------------------------------

            folderButton.setOnDragDropped(event -> {

                Dragboard dragboard =
                    event.getDragboard();

                boolean success = false;

                if (dragboard.hasString()) {

                    String noteId =
                        dragboard.getString();

                    if (noteDropped != null) {

                        Note dummy =
                            new Note();

                        dummy.setId(noteId);

                        dummy.setFolderId(
                            folder.getId()
                        );

                        noteDropped.accept(
                            dummy
                        );

                        success = true;
                    }
                }

                event.setDropCompleted(
                    success
                );

                event.consume();
            });

            folderContainer
                .getChildren()
                .add(folderButton);

            // --------------------------------
            // Children
            // --------------------------------

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

            if (
                parentId.equals(
                    folder.getParentId()
                )
            ) {

                return true;
            }
        }

        return false;
    }

    // --------------------------------
    // Getters
    // --------------------------------

    public Button getNewNoteButton() {
        return newNoteButton;
    }

    public Button getNewFolderButton() {
        return newFolderButton;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    // --------------------------------
    // Callbacks
    // --------------------------------

    public void setOnFolderSelected(
        Consumer<Folder> callback
    ) {

        this.folderSelected = callback;
    }

    public void setOnNoteDropped(
        Consumer<Note> callback
    ) {

        this.noteDropped = callback;
    }

    public void setOnNotesSelected(
        Runnable callback
    ) {

        this.notesSelected = callback;
    }

    // --------------------------------
    // Folder dialog
    // --------------------------------

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
