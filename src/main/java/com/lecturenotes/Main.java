package com.lecturenotes;

import com.lecturenotes.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


