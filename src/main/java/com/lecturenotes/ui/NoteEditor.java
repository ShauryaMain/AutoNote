package com.lecturenotes.ui;

import com.lecturenotes.model.Note;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

public class NoteEditor extends BorderPane {

    private final Label title;
    private final TextArea editor;

    private Note currentNote;

    private Consumer<Note> onNoteChanged;

    public NoteEditor() {

        getStyleClass().add("editor");

        setPadding(
            new Insets(40, 50, 40, 50)
        );

        title = new Label("Welcome to AutoNote");
        title.getStyleClass().add("editor-title");

        editor = new TextArea();

        editor.getStyleClass().add("editor-text");

        editor.setWrapText(true);

        editor.setPromptText(
            "Start writing your note..."
        );

        setTop(title);
        setCenter(editor);

        editor.textProperty().addListener(
            (observable, oldValue, newValue) -> {

                if (currentNote != null) {

                    currentNote.setContent(
                        newValue
                    );

                    if (onNoteChanged != null) {
                        onNoteChanged.accept(currentNote);
                    }
                }
            }
        );
    }

    public void showNote(Note note) {

        currentNote = note;

        title.setText(note.getTitle());

        editor.setText(
            note.getContent()
        );
    }

    public void saveCurrentNote() {

        if (currentNote == null) {
            return;
        }

        currentNote.setTitle(
            title.getText()
        );

        currentNote.setContent(
            editor.getText()
        );

        if (onNoteChanged != null) {
            onNoteChanged.accept(currentNote);
        }
    }

    public void setOnNoteChanged(
        Consumer<Note> callback
    ) {
        this.onNoteChanged = callback;
    }
}
