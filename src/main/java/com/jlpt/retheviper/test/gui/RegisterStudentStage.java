package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterStudentStage {

    @Getter
    private static Stage stage;

    public static void createStage() {
        try {
            stage = StageManager.create("RegisterStudentView.fxml", "학습자 등록");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
