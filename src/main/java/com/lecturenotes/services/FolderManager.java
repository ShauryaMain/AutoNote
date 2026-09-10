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

        return folder;
    }

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

    public void deleteFolder(
        Folder folder
    ) {

        List<Folder> children =
            getChildren(
                folder.getId()
            );

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
