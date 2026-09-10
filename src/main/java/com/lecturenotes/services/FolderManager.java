package com.lecturenotes.services;

import java.util.ArrayList;
import java.util.List;

import com.lecturenotes.model.Folder;
import com.lecturenotes.storage.FolderStorage;

public class FolderManager {

    private final List<Folder> folders;
    private final FolderStorage storage;

    public FolderManager() {

        storage = new FolderStorage();

        List<Folder> loadedFolders =
            storage.load();

        if (loadedFolders == null) {

            folders = new ArrayList<>();

            createDefaultFolders();

            save();

        } else {

            folders = loadedFolders;
        }
    }

    // --------------------------------
    // Default folders
    // --------------------------------

    private void createDefaultFolders() {

        createFolder(
            "Physics",
            null
        );

        createFolder(
            "Mathematics",
            null
        );

        createFolder(
            "Computer Science",
            null
        );
    }

    // --------------------------------
    // Get all folders
    // --------------------------------

    public List<Folder> getFolders() {

        return folders;
    }

    // --------------------------------
    // Create folder
    // --------------------------------

    public Folder createFolder(
        String name,
        String parentId
    ) {

        Folder folder =
            new Folder(
                name,
                parentId
            );

        folders.add(folder);

        save();

        return folder;
    }

    // --------------------------------
    // Get children
    // --------------------------------

    public List<Folder> getChildren(
        String parentId
    ) {

        List<Folder> children =
            new ArrayList<>();

        for (Folder folder : folders) {

            if (parentId == null) {

                if (folder.getParentId() == null) {

                    children.add(folder);
                }

            } else if (
                parentId.equals(
                    folder.getParentId()
                )
            ) {

                children.add(folder);
            }
        }

        return children;
    }

    // --------------------------------
    // Move folder
    // --------------------------------

    public boolean moveFolder(
        Folder folder,
        String newParentId
    ) {

        if (folder == null) {
            return false;
        }

        // Can't move a folder into itself
        if (
            folder.getId().equals(
                newParentId
            )
        ) {

            return false;
        }

        // Can't move a folder into one
        // of its own descendants
        if (
            isDescendant(
                newParentId,
                folder.getId()
            )
        ) {

            return false;
        }

        folder.setParentId(
            newParentId
        );

        save();

        return true;
    }

    // --------------------------------
    // Check whether target is inside
    // source folder
    // --------------------------------

    private boolean isDescendant(
        String possibleChildId,
        String sourceId
    ) {

        if (possibleChildId == null) {
            return false;
        }

        Folder possibleChild =
            findFolder(
                possibleChildId
            );

        if (possibleChild == null) {
            return false;
        }

        String parentId =
            possibleChild.getParentId();

        while (parentId != null) {

            if (
                parentId.equals(sourceId)
            ) {

                return true;
            }

            Folder parent =
                findFolder(parentId);

            if (parent == null) {
                break;
            }

            parentId =
                parent.getParentId();
        }

        return false;
    }

    // --------------------------------
    // Find folder
    // --------------------------------

    private Folder findFolder(
        String id
    ) {

        for (Folder folder : folders) {

            if (
                folder.getId().equals(id)
            ) {

                return folder;
            }
        }

        return null;
    }

    // --------------------------------
    // Delete folder
    // --------------------------------

    public void deleteFolder(
        Folder folder
    ) {

        if (folder == null) {
            return;
        }

        List<Folder> children =
            new ArrayList<>(
                getChildren(
                    folder.getId()
                )
            );

        for (Folder child : children) {

            deleteFolder(child);
        }

        folders.remove(folder);

        save();
    }

    // --------------------------------
    // Rename folder
    // --------------------------------

    public void renameFolder(
        Folder folder,
        String newName
    ) {

        if (folder == null) {
            return;
        }

        if (
            newName == null ||
            newName.isBlank()
        ) {

            return;
        }

        folder.setName(
            newName
        );

        save();
    }

    // --------------------------------
    // Save folders
    // --------------------------------

    public void save() {

        storage.save(
            folders
        );
    }
}