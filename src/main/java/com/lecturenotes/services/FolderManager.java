package com.lecturenotes.services;

import com.lecturenotes.model.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderManager {

    private final List<Folder> folders;

    public FolderManager() {
        folders = new ArrayList<>();
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public Folder createFolder(String name, String parentId) {

        Folder folder = new Folder(
            name,
            parentId
        );

        folders.add(folder);

        return folder;
    }

    public void deleteFolder(Folder folder) {
        folders.remove(folder);
    }
}


