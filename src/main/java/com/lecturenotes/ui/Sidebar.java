package com.lecturenotes.ui;

import com.lecturenotes.model.Folder;
import com.lecturenotes.model.Note;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Sidebar extends VBox {

    private final Button newNoteButton;
    private final Button newFolderButton;
    private final Button notesButton;
    private final VBox folderContainer;

    private Consumer<Folder> folderSelected;
    private Consumer<Note> noteDropped;
    private BiConsumer<Folder, String> folderDropped;
    private Runnable notesSelected;

    private Folder selectedFolder;

    public Sidebar() {

        getStyleClass().add("sidebar");

        setPrefWidth(255);

        setPadding(
            new Insets(
                30,
                16,
                30,
                16
            )
        );

        setSpacing(6);

        // --------------------------------
        // Header
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

        notesButton.setOnAction(event -> {

            selectedFolder = null;

            if (notesSelected != null) {
                notesSelected.run();
            }
        });

        // Notes is a drop target

        notesButton.setOnDragOver(event -> {

            Dragboard board =
                event.getDragboard();

            if (
                board.hasString()
                &&
                (
                    board.getString()
                        .startsWith("NOTE:")
                    ||
                    board.getString()
                        .startsWith("FOLDER:")
                )
            ) {

                event.acceptTransferModes(
                    TransferMode.MOVE
                );
            }

            event.consume();
        });

        notesButton.setOnDragEntered(event -> {

            if (
                event.getDragboard()
                    .hasString()
            ) {

                notesButton
                    .getStyleClass()
                    .add("drop-target");
            }
        });

        notesButton.setOnDragExited(event -> {

            notesButton
                .getStyleClass()
                .remove("drop-target");
        });

        notesButton.setOnDragDropped(event -> {

            Dragboard board =
                event.getDragboard();

            if (!board.hasString()) {
                return;
            }

            String data =
                board.getString();

            boolean success = false;

            if (data.startsWith("NOTE:")) {

                String id =
                    data.substring(5);

                Note note =
                    new Note();

                note.setId(id);

                if (noteDropped != null) {

                    noteDropped.accept(note);

                    success = true;
                }
            }

            else if (data.startsWith("FOLDER:")) {

                String id =
                    data.substring(7);

                Folder folder =
                    new Folder();

                folder.setId(id);

                if (folderDropped != null) {

                    folderDropped.accept(
                        folder,
                        null
                    );

                    success = true;
                }
            }

            event.setDropCompleted(success);

            notesButton
                .getStyleClass()
                .remove("drop-target");

            event.consume();
        });

        // --------------------------------
        // Section labels
        // --------------------------------

        Label recent =
            createSectionLabel("Recent");

        Label foldersTitle =
            createSectionLabel("Folders");

        // --------------------------------
        // New Folder
        // --------------------------------

        newFolderButton =
            new Button("+  New Folder");

        newFolderButton.getStyleClass().add(
            "new-folder-button"
        );

        newFolderButton.setMaxWidth(
            Double.MAX_VALUE
        );

        // --------------------------------
        // Folder container
        // --------------------------------

        folderContainer =
            new VBox(2);

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

    // ================================================
    // SECTION LABEL
    // ================================================

    private Label createSectionLabel(
        String text
    ) {

        Label label =
            new Label(text);

        label.getStyleClass().add(
            "sidebar-section"
        );

        label.setMaxWidth(
            Double.MAX_VALUE
        );

        return label;
    }

    // ================================================
    // FOLDER TREE
    // ================================================

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

        List<Folder> children =
            getChildren(
                folders,
                parentId
            );

        for (Folder folder : children) {

            boolean hasChildren =
                hasChildren(
                    folders,
                    folder.getId()
                );

            Button folderButton =
                createFolderButton(
                    folder,
                    depth,
                    hasChildren
                );

            folderContainer
                .getChildren()
                .add(folderButton);

            if (hasChildren) {

                buildTree(
                    folders,
                    folder.getId(),
                    depth + 1
                );
            }
        }
    }

    private List<Folder> getChildren(
        List<Folder> folders,
        String parentId
    ) {

        java.util.ArrayList<Folder>
            result =
                new java.util.ArrayList<>();

        for (Folder folder : folders) {

            if (parentId == null) {

                if (
                    folder.getParentId()
                        == null
                ) {

                    result.add(folder);
                }

            } else if (
                parentId.equals(
                    folder.getParentId()
                )
            ) {

                result.add(folder);
            }
        }

        return result;
    }

    // ================================================
    // FOLDER BUTTON
    // ================================================

    private Button createFolderButton(
        Folder folder,
        int depth,
        boolean hasChildren
    ) {

        Button button =
            new Button();

        button.getStyleClass().add(
            "folder-item"
        );

        button.setMaxWidth(
            Double.MAX_VALUE
        );

        button.setAlignment(
            Pos.CENTER_LEFT
        );

        /*
         * Strong indentation makes nesting obvious.
         *
         * Root:
         *   📁 Mathematics
         *
         * Child:
         *       📁 Algebra
         *
         * Grandchild:
         *           📁 Quadratics
         */

        button.setPadding(
            new Insets(
                7,
                8,
                7,
                8 + (depth * 22)
            )
        );

        String arrow =
            hasChildren
                ? "⌄  "
                : "   ";

        button.setText(
            arrow
            + "▱  "
            + folder.getName()
        );

        // --------------------------------
        // Click
        // --------------------------------

        button.setOnAction(event -> {

            selectedFolder = folder;

            if (folderSelected != null) {

                folderSelected.accept(
                    folder
                );
            }
        });

        // --------------------------------
        // Start drag
        // --------------------------------

        button.setOnDragDetected(event -> {

            Dragboard board =
                button.startDragAndDrop(
                    TransferMode.MOVE
                );

            ClipboardContent content =
                new ClipboardContent();

            content.putString(
                "FOLDER:"
                + folder.getId()
            );

            board.setContent(content);

            event.consume();
        });

        // --------------------------------
        // Drag over
        // --------------------------------

        button.setOnDragOver(event -> {

            Dragboard board =
                event.getDragboard();

            if (!board.hasString()) {
                return;
            }

            String data =
                board.getString();

            boolean valid =
                data.startsWith("NOTE:")
                ||
                data.startsWith("FOLDER:");

            if (valid) {

                event.acceptTransferModes(
                    TransferMode.MOVE
                );
            }

            event.consume();
        });

        // --------------------------------
        // Drag entered
        // --------------------------------

        button.setOnDragEntered(event -> {

            if (
                event.getDragboard()
                    .hasString()
            ) {

                button
                    .getStyleClass()
                    .add("drop-target");
            }
        });

        // --------------------------------
        // Drag exited
        // --------------------------------

        button.setOnDragExited(event -> {

            button
                .getStyleClass()
                .remove("drop-target");
        });

        // --------------------------------
        // Drop
        // --------------------------------

        button.setOnDragDropped(event -> {

            Dragboard board =
                event.getDragboard();

            if (!board.hasString()) {
                return;
            }

            String data =
                board.getString();

            boolean success = false;

            // ----------------------------
            // NOTE → FOLDER
            // ----------------------------

            if (data.startsWith("NOTE:")) {

                String noteId =
                    data.substring(5);

                Note note =
                    new Note();

                note.setId(noteId);

                note.setFolderId(
                    folder.getId()
                );

                if (noteDropped != null) {

                    noteDropped.accept(note);

                    success = true;
                }
            }

            // ----------------------------
            // FOLDER → FOLDER
            // ----------------------------

            else if (
                data.startsWith("FOLDER:")
            ) {

                String folderId =
                    data.substring(7);

                Folder dragged =
                    new Folder();

                dragged.setId(folderId);

                if (folderDropped != null) {

                    folderDropped.accept(
                        dragged,
                        folder.getId()
                    );

                    success = true;
                }
            }

            event.setDropCompleted(
                success
            );

            button
                .getStyleClass()
                .remove("drop-target");

            event.consume();
        });

        return button;
    }

    // ================================================
    // CHILD CHECK
    // ================================================

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

    // ================================================
    // GETTERS
    // ================================================

    public Button getNewNoteButton() {
        return newNoteButton;
    }

    public Button getNewFolderButton() {
        return newFolderButton;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    // ================================================
    // CALLBACKS
    // ================================================

    public void setOnFolderSelected(
        Consumer<Folder> callback
    ) {

        folderSelected = callback;
    }

    public void setOnNoteDropped(
        Consumer<Note> callback
    ) {

        noteDropped = callback;
    }

    public void setOnFolderDropped(
        BiConsumer<Folder, String> callback
    ) {

        folderDropped = callback;
    }

    public void setOnNotesSelected(
        Runnable callback
    ) {

        notesSelected = callback;
    }

    // ================================================
    // FOLDER DIALOG
    // ================================================

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