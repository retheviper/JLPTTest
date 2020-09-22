package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.controller.ElseTestViewControl;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElseTestStage {

    @Getter
    private static Stage stage;

    public static void createStage() {
        try {
            stage = StageManager.create("ElseTestView.fxml", ElseTestViewControl.getSubject().getValue() + " 과목 모의고사");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
