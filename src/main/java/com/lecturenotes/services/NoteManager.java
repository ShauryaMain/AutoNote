package com.lecturenotes.services;

import com.lecturenotes.model.Note;
import com.lecturenotes.storage.NoteStorage;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    private final List<Note> notes;
    private final NoteStorage storage;

    public NoteManager() {

        storage = new NoteStorage();

        notes = storage.load();

        if (notes.isEmpty()) {
            createDefaultNotes();
            save();
        }
    }

    private void createDefaultNotes() {

        notes.add(
            new Note(
                "Electromagnetic Induction",
                """
                Electromagnetic induction is the production of an
                electromotive force when the magnetic flux through
                a circuit changes.

                Faraday's law:

                emf = -dΦ/dt

                The negative sign represents Lenz's law.
                """
            )
        );

        notes.add(
            new Note(
                "Probability Distributions",
                """
                A probability distribution describes the possible
                values of a random variable and their probabilities.

                For a binomial distribution:

                X ~ B(n, p)

                where n is the number of trials and p is the
                probability of success.
                """
            )
        );

        notes.add(
            new Note(
                "Newton's Laws",
                """
                Newton's laws describe the relationship between
                forces and motion.

                F = ma

                The net force acting on an object is equal to its
                mass multiplied by its acceleration.
                """
            )
        );
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Note> getNotesInFolder(String folderId) {

        List<Note> filteredNotes = new ArrayList<>();

        for (Note note : notes) {

            if (folderId == null) {

                if (note.getFolderId() == null) {
                    filteredNotes.add(note);
                }

            } else if (folderId.equals(
                note.getFolderId()
            )) {

                filteredNotes.add(note);
            }
        }

        return filteredNotes;
    }

    public Note createNote() {

        return createNote(null);
    }

    public Note createNote(String folderId) {

        Note note = new Note(
            "Untitled Note",
            ""
        );

        note.setFolderId(folderId);

        notes.add(0, note);

        save();

        return note;
    }

    public void moveNote(
        Note note,
        String folderId
    ) {

        if (note == null) {
            return;
        }

        note.setFolderId(folderId);

        save();
    }

    public void deleteNote(Note note) {

        notes.remove(note);

        save();
    }

    public void save() {
        storage.save(notes);
    }
}


