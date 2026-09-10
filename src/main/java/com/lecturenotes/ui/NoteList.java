package com.lecturenotes.ui;

import com.lecturenotes.model.Note;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class NoteList extends ScrollPane {

    private final VBox container;

    private Consumer<Note> noteSelected;

    public NoteList() {

        getStyleClass().add(
                "note-list");

        setPrefWidth(330);

        setFitToWidth(true);

        setHbarPolicy(
                ScrollBarPolicy.NEVER);

        setVbarPolicy(
                ScrollBarPolicy.AS_NEEDED);

        container = new VBox(3);

        container.setPadding(
                new Insets(
                        30,
                        18,
                        30,
                        18));

        setContent(container);

        setNotes(
                List.of(),
                "Notes");
    }

    public void setNotes(
            List<Note> notes) {

        setNotes(
                notes,
                "Notes");
    }

    public void setNotes(
            List<Note> notes,
            String headingText) {

        container
                .getChildren()
                .clear();

        Label heading = new Label(headingText);

        heading.getStyleClass().add(
                "note-list-heading");

        container
                .getChildren()
                .add(heading);

        for (Note note : notes) {

            addNote(note);
        }
    }

    private void addNote(
            Note note) {

        VBox noteBox = new VBox(4);

        noteBox.getStyleClass().add(
                "note-item");

        noteBox.setMaxWidth(
                Double.MAX_VALUE);

        Label title = new Label(
                note.getTitle());

        title.getStyleClass().add(
                "note-item-title");

        title.setWrapText(true);

        title.setMaxWidth(
                Double.MAX_VALUE);

        Label preview = new Label(
                createPreview(
                        note.getContent()));

        preview.getStyleClass().add(
                "note-item-preview");

        preview.setWrapText(true);

        preview.setMaxWidth(
                Double.MAX_VALUE);

        noteBox
                .getChildren()
                .addAll(
                        title,
                        preview);

        // ========================================
        // CLICK
        // ========================================

        noteBox.setOnMouseClicked(
                event -> {

                    if (noteSelected != null) {

                        noteSelected.accept(
                                note);
                    }
                });

        // ========================================
        // DRAG
        // ========================================

        noteBox.setOnDragDetected(
                event -> {

                    Dragboard board = noteBox.startDragAndDrop(
                            TransferMode.MOVE);

                    ClipboardContent content = new ClipboardContent();

                    content.putString(
                            "NOTE:"
                                    + note.getId());

                    board.setContent(content);

                    event.consume();
                });

        // ========================================
        // Dragging over another note
        // ========================================

        noteBox.setOnDragOver(event -> {

            Dragboard board = event.getDragboard();

            if (board.hasString()
                    &&
                    board.getString()
                            .startsWith("NOTE:")) {

                event.acceptTransferModes(
                        TransferMode.MOVE);
            }

            event.consume();
        });

        container
                .getChildren()
                .add(noteBox);
    }

    private String createPreview(
            String content) {

        if (content == null ||
                content.isBlank()) {

            return "No content";
        }

        String cleaned = content
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();

        if (cleaned.length() > 90) {

            return cleaned.substring(
                    0,
                    90) + "…";
        }

        return cleaned;
    }

    public void setOnNoteSelected(
            Consumer<Note> callback) {

        noteSelected = callback;
    }
}