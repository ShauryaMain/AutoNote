package com.lecturenotes.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lecturenotes.model.Folder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FolderStorage {

    private final ObjectMapper mapper;
    private final Path storagePath;

    public FolderStorage() {

        mapper = new ObjectMapper();

        storagePath = Path.of(
            System.getProperty("user.home"),
            "Library",
            "Application Support",
            "AutoNote",
            "folders.json"
        );
    }

    public List<Folder> load() {

        try {

            if (!Files.exists(storagePath)) {
                return null;
            }

            return mapper.readValue(
                storagePath.toFile(),
                new TypeReference<List<Folder>>() {}
            );

        } catch (IOException e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    public void save(List<Folder> folders) {

        try {

            Files.createDirectories(
                storagePath.getParent()
            );

            mapper.writerWithDefaultPrettyPrinter()
                .writeValue(
                    storagePath.toFile(),
                    folders
                );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}