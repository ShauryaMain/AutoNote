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

        getStyleClass().add("note-list");

        setPrefWidth(320);

        setFitToWidth(true);

        setHbarPolicy(
            ScrollBarPolicy.NEVER
        );

        setVbarPolicy(
            ScrollBarPolicy.AS_NEEDED
        );

        container = new VBox(2);

        container.setPadding(
            new Insets(
                28,
                18,
                28,
                18
            )
        );

        setContent(container);

        setNotes(
            List.of(),
            "Notes"
        );
    }

    public void setNotes(
        List<Note> notes
    ) {

        setNotes(
            notes,
            "Notes"
        );
    }

    public void setNotes(
        List<Note> notes,
        String headingText
    ) {

        container.getChildren().clear();

        Label heading =
            new Label(headingText);

        heading.getStyleClass().add(
            "note-list-heading"
        );

        container.getChildren().add(
            heading
        );

        for (Note note : notes) {

            addNote(note);
        }
    }

    private void addNote(Note note) {

        VBox noteBox =
            new VBox(3);

        noteBox.getStyleClass().add(
            "note-item"
        );

        noteBox.setMaxWidth(
            Double.MAX_VALUE
        );

        Label title =
            new Label(note.getTitle());

        title.setWrapText(true);

        title.setMaxWidth(
            Double.MAX_VALUE
        );

        Label preview =
            new Label(
                createPreview(
                    note.getContent()
                )
            );

        preview.setWrapText(true);

        preview.setMaxWidth(
            Double.MAX_VALUE
        );

        preview.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #85858c;"
        );

        noteBox.getChildren().addAll(
            title,
            preview
        );

        // --------------------------------
        // Normal click
        // --------------------------------

        noteBox.setOnMouseClicked(event -> {

            if (noteSelected != null) {

                noteSelected.accept(note);
            }
        });

        // --------------------------------
        // Start drag
        // --------------------------------

        noteBox.setOnDragDetected(event -> {

            Dragboard dragboard =
                noteBox.startDragAndDrop(
                    TransferMode.MOVE
                );

            ClipboardContent content =
                new ClipboardContent();

            content.putString(
                note.getId()
            );

            dragboard.setContent(content);

            event.consume();
        });

        // --------------------------------
        // Drag over
        // --------------------------------

        noteBox.setOnDragOver(event -> {

            if (
                event.getGestureSource() != noteBox &&
                event.getDragboard().hasString()
            ) {

                event.acceptTransferModes(
                    TransferMode.MOVE
                );
            }

            event.consume();
        });

        container.getChildren().add(
            noteBox
        );
    }

    private String createPreview(
        String content
    ) {

        if (
            content == null ||
            content.isBlank()
        ) {

            return "No content";
        }

        String cleaned =
            content
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();

        if (cleaned.length() > 80) {

            return cleaned.substring(0, 80)
                + "…";
        }

        return cleaned;
    }

    public void setOnNoteSelected(
        Consumer<Note> callback
    ) {

        this.noteSelected = callback;
    }
}


