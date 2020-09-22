package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrepareForTestStage {

    @Getter
    private static Stage stage;

    public static void createStage() {
        try {
            stage = StageManager.create("PrepareForTestView.fxml", "시험 설정");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
