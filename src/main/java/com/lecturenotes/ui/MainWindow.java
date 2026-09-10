package com.lecturenotes.ui;

import com.lecturenotes.model.Folder;
import com.lecturenotes.model.Note;
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

    private NoteList noteList;
    private Sidebar sidebar;
    private NoteEditor noteEditor;

    private Folder selectedFolder;

    public MainWindow(Stage stage) {

        this.stage = stage;

        this.noteManager =
            new NoteManager();

        this.folderManager =
            new FolderManager();
    }

    public void show() {

        BorderPane root =
            new BorderPane();

        sidebar =
            new Sidebar();

        noteList =
            new NoteList();

        noteEditor =
            new NoteEditor();

        // --------------------------------
        // Initial notes
        // --------------------------------

        refreshNoteList();

        // --------------------------------
        // Select note
        // --------------------------------

        noteList.setOnNoteSelected(
            noteEditor::showNote
        );

        // --------------------------------
        // Autosave
        // --------------------------------

        noteEditor.setOnNoteChanged(
            note -> noteManager.save()
        );

        // --------------------------------
        // Load folders
        // --------------------------------

        sidebar.setFolders(
            folderManager.getFolders()
        );

        // --------------------------------
        // Select folder
        // --------------------------------

        sidebar.setOnFolderSelected(
            folder -> {

                selectedFolder = folder;

                refreshNoteList();
            }
        );

        // --------------------------------
        // Select Notes
        // --------------------------------

        sidebar.setOnNotesSelected(
            () -> {

                selectedFolder = null;

                refreshNoteList();
            }
        );

        // ================================================
        // NOTE DROP
        // ================================================

        sidebar.setOnNoteDropped(
            droppedNote -> {

                Note realNote =
                    findNoteById(
                        droppedNote.getId()
                    );

                if (realNote == null) {
                    return;
                }

                noteManager.moveNote(
                    realNote,
                    droppedNote.getFolderId()
                );

                refreshNoteList();
            }
        );

        // ================================================
        // FOLDER DROP
        // ================================================

        sidebar.setOnFolderDropped(
            (droppedFolder, newParentId) -> {

                Folder realFolder =
                    findFolderById(
                        droppedFolder.getId()
                    );

                if (realFolder == null) {
                    return;
                }

                boolean moved =
                    folderManager.moveFolder(
                        realFolder,
                        newParentId
                    );

                if (!moved) {
                    return;
                }

                // Rebuild sidebar
                sidebar.setFolders(
                    folderManager.getFolders()
                );

                // Keep current folder view
                refreshNoteList();
            }
        );

        // --------------------------------
        // New Note
        // --------------------------------

        sidebar
            .getNewNoteButton()
            .setOnAction(event -> {

                String folderId =
                    selectedFolder == null
                        ? null
                        : selectedFolder.getId();

                Note newNote =
                    noteManager.createNote(
                        folderId
                    );

                refreshNoteList();

                noteEditor.showNote(
                    newNote
                );
            });

        // --------------------------------
        // New Folder
        // --------------------------------

        sidebar
            .getNewFolderButton()
            .setOnAction(event -> {

                Optional<String> result =
                    sidebar.requestFolderName();

                result.ifPresent(name -> {

                    if (name.isBlank()) {
                        return;
                    }

                    Folder parent =
                        sidebar.getSelectedFolder();

                    String parentId =
                        parent == null
                            ? null
                            : parent.getId();

                    folderManager.createFolder(
                        name,
                        parentId
                    );

                    sidebar.setFolders(
                        folderManager.getFolders()
                    );
                });
            });

        // --------------------------------
        // Layout
        // --------------------------------

        HBox content =
            new HBox(
                noteList,
                noteEditor
            );

        root.setLeft(sidebar);

        root.setCenter(content);

        // --------------------------------
        // Scene
        // --------------------------------

        Scene scene =
            new Scene(
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

    // ================================================
    // FIND NOTE
    // ================================================

    private Note findNoteById(
        String id
    ) {

        for (
            Note note :
            noteManager.getNotes()
        ) {

            if (
                note.getId().equals(id)
            ) {

                return note;
            }
        }

        return null;
    }

    // ================================================
    // FIND FOLDER
    // ================================================

    private Folder findFolderById(
        String id
    ) {

        for (
            Folder folder :
            folderManager.getFolders()
        ) {

            if (
                folder.getId().equals(id)
            ) {

                return folder;
            }
        }

        return null;
    }

    // ================================================
    // REFRESH NOTES
    // ================================================

    private void refreshNoteList() {

        if (noteList == null) {
            return;
        }

        if (selectedFolder == null) {

            noteList.setNotes(
                noteManager.getNotes(),
                "Notes"
            );

        } else {

            noteList.setNotes(
                noteManager.getNotesInFolder(
                    selectedFolder.getId()
                ),
                selectedFolder.getName()
            );
        }
    }
}
