package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class PrepareForTestStage {

    @Getter
    private static Stage stage;

    public PrepareForTestStage() {
        try {
            stage = StageManager.create("PrepareForTestView.fxml", "시험 설정");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
