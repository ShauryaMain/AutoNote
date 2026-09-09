package com.lecturenotes.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lecturenotes.model.Note;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NoteStorage {

    private final ObjectMapper mapper;
    private final Path storagePath;

    public NoteStorage() {

        mapper = new ObjectMapper();

        mapper.registerModule(
            new JavaTimeModule()
        );

        storagePath = Path.of(
            System.getProperty("user.home"),
            "Library",
            "Application Support",
            "AutoNote",
            "notes.json"
        );
    }

    public List<Note> load() {

        try {

            if (!Files.exists(storagePath)) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                storagePath.toFile(),
                new TypeReference<List<Note>>() {}
            );

        } catch (IOException e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    public void save(List<Note> notes) {

        try {

            Files.createDirectories(
                storagePath.getParent()
            );

            mapper.writerWithDefaultPrettyPrinter()
                .writeValue(
                    storagePath.toFile(),
                    notes
                );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
