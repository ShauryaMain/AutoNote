package com.lecturenotes.ui;

import com.lecturenotes.services.NoteManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.lecturenotes.services.FolderManager;

public class MainWindow {

    private final Stage stage;
    private final NoteManager noteManager;
    private final FolderManager folderManager;

    public MainWindow(Stage stage) {
        this.stage = stage;
        this.noteManager = new NoteManager();
        this.folderManager = new FolderManager();
    }

    public void show() {

        BorderPane root = new BorderPane();

        Sidebar sidebar = new Sidebar();
        NoteList noteList = new NoteList();
        NoteEditor noteEditor = new NoteEditor();
        folderManager.createFolder("Physics", null);
        folderManager.createFolder("Math", null);
        folderManager.createFolder("CS", null);

        sidebar.setFolders(folderManager.getFolders());

        // Load existing notes
        noteList.setNotes(
            noteManager.getNotes()
        );

        // Open a note when it is selected
        noteList.setOnNoteSelected(
            noteEditor::showNote
        );

        noteEditor.setOnNoteChanged(note -> noteManager.save());

        // Create a new note
        sidebar.getNewNoteButton().setOnAction(event -> {

            var newNote = noteManager.createNote();

            noteList.setNotes(
                noteManager.getNotes()
            );

            noteEditor.showNote(newNote);
        });

        // Main content area
        HBox content = new HBox(
            noteList,
            noteEditor
        );

        root.setLeft(sidebar);
        root.setCenter(content);

        // Scene
        Scene scene = new Scene(
            root,
            1400,
            850
        );

        // Load CSS
        scene.getStylesheets().add(
            getClass()
                .getResource(
                    "/com/lecturenotes/style.css"
                )
                .toExternalForm()
        );

        stage.setTitle("AutoNote");
        stage.setScene(scene);
        stage.show();
    }
}


