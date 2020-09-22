package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class RegistStudentStage {

    @Getter
    private static Stage stage;

    public RegistStudentStage() {
        try {
            stage = StageManager.create("RegistStudentView.fxml", "학습자 등록");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
