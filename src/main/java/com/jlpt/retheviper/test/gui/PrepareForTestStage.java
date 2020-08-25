package com.jlpt.retheviper.test.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PrepareForTestStage extends Stage {

    public static Stage stage;

    public PrepareForTestStage() {
        try {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PrepareForTestView.fxml"));
            Parent root = loader.load();
            stage.setTitle("시험 설정");
            stage.setResizable(false);
            final Scene prepareForTestScene = new Scene(root);
            prepareForTestScene.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                }
            });
            stage.setScene(prepareForTestScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    @SuppressWarnings("static-access")
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
