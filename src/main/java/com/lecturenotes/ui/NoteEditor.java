package com.lecturenotes.ui;

import com.lecturenotes.model.Note;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class NoteEditor extends BorderPane {

    private final Label title;
    private final TextArea editor;

    private Note currentNote;

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
    }

    public void showNote(Note note) {

        currentNote = note;

        title.setText(note.getTitle());
        editor.setText(note.getContent());
    }

    public void saveCurrentNote() {

        if (currentNote == null) {
            return;
        }

        currentNote.setTitle(title.getText());
        currentNote.setContent(editor.getText());
    }
}
