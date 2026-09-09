package com.lecturenotes.services;

import com.lecturenotes.model.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderManager {

    private final List<Folder> folders;

    public FolderManager() {
        folders = new ArrayList<>();

        createFolder("Physics", null);
        createFolder("Mathematics", null);
        createFolder("Computer Science", null);
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

    public List<Folder> getChildren(String parentId) {

        List<Folder> children = new ArrayList<>();

        for (Folder folder : folders) {

            if (parentId == null) {

                if (folder.getParentId() == null) {
                    children.add(folder);
                }

            } else if (parentId.equals(
                folder.getParentId()
            )) {

                children.add(folder);
            }
        }

        return children;
    }

    public void deleteFolder(Folder folder) {

        // Delete all descendants first
        List<Folder> children =
            getChildren(folder.getId());

        for (Folder child : children) {
            deleteFolder(child);
        }

        folders.remove(folder);
    }

    public void renameFolder(
        Folder folder,
        String newName
    ) {

        folder.setName(newName);
    }
}
