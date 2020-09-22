package com.jlpt.retheviper.test.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StageManager {

    public static Stage create(final String view, final String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StageManager.class.getResource(view));
        final Parent root = loader.load();
        final Stage stage = new Stage();
        stage.setTitle("문제 등록");
        stage.setResizable(false);
        final Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.close();
            }
        });
        stage.setScene(scene);
        return stage;
    }
}
