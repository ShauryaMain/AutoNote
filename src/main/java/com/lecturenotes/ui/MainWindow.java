package com.lecturenotes.ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow {
  private final Stage stage;

  public MainWindow(Stage stage) {
    this.stage = stage;
  }

  //Show the MainWindow
  public void show() {
    BorderPane root = new BorderPane();

    Sidebar sidebar = new Sidebar();

    root.setLeft(sidebar);
    Scene scene = new Scene(root, 1200, 800);

    stage.setTitle("AutoNote");
    stage.setScene(scene);
    stage.show();
  }
}


