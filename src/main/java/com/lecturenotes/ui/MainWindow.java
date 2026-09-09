package com.lecturenotes.ui;

import com.lecturenotes.model.Folder;
import com.lecturenotes.services.FolderManager;
import com.lecturenotes.services.NoteManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Optional;

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

        // Load notes
        noteList.setNotes(
            noteManager.getNotes()
        );

        // Select note
        noteList.setOnNoteSelected(
            noteEditor::showNote
        );

        // Autosave
        noteEditor.setOnNoteChanged(
            note -> noteManager.save()
        );

        // Load folders
        sidebar.setFolders(
            folderManager.getFolders()
        );

        // Create new note
        sidebar.getNewNoteButton().setOnAction(
            event -> {

                var newNote =
                    noteManager.createNote();

                noteList.setNotes(
                    noteManager.getNotes()
                );

                noteEditor.showNote(newNote);
            }
        );

        // Create new folder
        sidebar.getNewFolderButton().setOnAction(
            event -> {

                Optional<String> result =
                    sidebar.requestFolderName();

                result.ifPresent(name -> {

                    if (name.isBlank()) {
                        return;
                    }

                    Folder parent = sidebar.getSelectedFolder();
                    String parentId = parent == null ? null : parent.getId();

                    folderManager.createFolder(name, parentId);

                    sidebar.setFolders(
                        folderManager.getFolders()
                    );
                });
            }
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
