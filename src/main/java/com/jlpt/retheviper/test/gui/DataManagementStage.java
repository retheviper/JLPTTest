package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.Getter;

public class DataManagementStage {

    @Getter
    private static Stage stage;

    public DataManagementStage() {
        try {
            stage = StageManager.create("DataManagementView.fxml", "문제 등록");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
