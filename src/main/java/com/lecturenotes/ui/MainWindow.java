package com.lecturenotes.ui;

import com.lecturenotes.services.NoteManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainWindow {

    private final Stage stage;

    private final NoteManager noteManager;

    public MainWindow(Stage stage) {

        this.stage = stage;
        this.noteManager = new NoteManager();
    }

    public void show() {

        BorderPane root = new BorderPane();

        Sidebar sidebar = new Sidebar();
        NoteList noteList = new NoteList();
        NoteEditor noteEditor = new NoteEditor();

        noteList.setNotes(
            noteManager.getNotes()
        );

        noteList.setOnNoteSelected(
            noteEditor::showNote
        );

        HBox content = new HBox(
            noteList,
            noteEditor
        );

        root.setLeft(sidebar);
        root.setCenter(content);

        Scene scene = new Scene(
            root,
            1400,
            850
        );

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
