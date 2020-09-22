package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RemoveStudentStage {

    @Getter
    private static Stage stage;

    public static void createStage() {
        try {
            stage = StageManager.create("RemoveStudentView.fxml", "학습자 삭제");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}