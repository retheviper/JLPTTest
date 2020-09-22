package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.service.StudentManagementService;
import javafx.stage.Stage;

import java.io.IOException;

public class ScoreStage extends Stage {

    public ScoreStage() {
        try {
            final Stage stage = StageManager.create("ScoreView.fxml", StudentManagementService.getInstance().getLoginedUser() + "님의 점수");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}