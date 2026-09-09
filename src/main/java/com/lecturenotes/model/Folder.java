package com.lecturenotes.model;

import java.util.UUID;

public class Folder {

  private String id;
  private String name;
  private String parentId;

  public Folder() {
    //Needed for JSON deseralization
  }

  public Folder(String name, String parentId) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.parentId = parentId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

}


