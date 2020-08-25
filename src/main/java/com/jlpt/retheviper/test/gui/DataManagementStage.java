package com.jlpt.retheviper.test.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class DataManagementStage extends Stage {

    public static Stage stage;

    public DataManagementStage() {
        try {
            stage = new Stage();
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DataManagementView.fxml"));
            final Parent root = loader.load();
            stage.setTitle("문제 등록");
            stage.setResizable(false);
            final Scene dataManegementScene = new Scene(root);
            dataManegementScene.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                }
            });
            stage.setScene(dataManegementScene);
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
