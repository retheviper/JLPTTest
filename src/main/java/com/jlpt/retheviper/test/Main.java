package com.jlpt.retheviper.test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class Main extends Application {

    @Getter
    @Setter
    private static Stage primaryStage;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        try {
            Main.setPrimaryStage(primaryStage);
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("gui/MainView.fxml"));
            final Parent root = loader.load();
            primaryStage.setTitle("JLPT Test Ver 0.1");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> Platform.exit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}