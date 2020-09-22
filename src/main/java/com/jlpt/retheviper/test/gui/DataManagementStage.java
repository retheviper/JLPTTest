package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataManagementStage {

    @Getter
    private static Stage stage;

    public static void createStage() {
        try {
            stage = StageManager.create("DataManagementView.fxml", "문제 등록");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
