package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.controller.ElseTestViewControl;
import javafx.stage.Stage;

import java.io.IOException;

public class ElseTestStage {

    public static Stage stage;

    public ElseTestStage() {
        try {
            stage = StageManager.create("ElseTestView.fxml", ElseTestViewControl.getSubject().getValue() + " 과목 모의고사");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
