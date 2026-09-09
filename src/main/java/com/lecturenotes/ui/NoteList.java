package com.lecturenotes.ui;

import com.lecturenotes.model.Note;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class NoteList extends ScrollPane {

    private final VBox container;
    private Consumer<Note> noteSelected;

    public NoteList() {

        getStyleClass().add("note-list");

        setPrefWidth(300);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);

        container = new VBox(10);
        container.setPadding(
            new Insets(24, 16, 24, 16)
        );

        Label heading = new Label("Recent Notes");
        heading.getStyleClass().add("note-list-heading");

        container.getChildren().add(heading);

        setContent(container);
    }

    public void setNotes(List<Note> notes) {

        container.getChildren().clear();

        Label heading = new Label("Recent Notes");
        heading.getStyleClass().add("note-list-heading");

        container.getChildren().add(heading);

        for (Note note : notes) {
            addNote(note);
        }
    }

    private void addNote(Note note) {

        Label item = new Label(note.getTitle());

        item.getStyleClass().add("note-item");

        item.setMaxWidth(Double.MAX_VALUE);
        item.setWrapText(true);

        item.setOnMouseClicked(event -> {

            if (noteSelected != null) {
                noteSelected.accept(note);
            }
        });

        container.getChildren().add(item);
    }

    public void setOnNoteSelected(Consumer<Note> callback) {
        this.noteSelected = callback;
    }
}
