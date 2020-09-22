package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.Getter;

public class RemoveStudentStage {

    @Getter
    private static Stage stage;

    public RemoveStudentStage() {
        try {
            stage = StageManager.create("RemoveStudentView.fxml", "학습자 삭제");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}