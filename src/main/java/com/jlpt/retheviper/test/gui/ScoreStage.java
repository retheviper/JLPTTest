package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.service.StudentManagementService;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreStage {

    public static void createStage() {
        try {
            final Stage stage = StageManager.create("ScoreView.fxml", StudentManagementService.getInstance().getLoginUser() + "님의 점수");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}