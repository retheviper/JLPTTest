package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class LoginStage {

    @Getter
    private static Stage stage;

    public LoginStage() {
        try {
            stage = StageManager.create("LoginView.fxml", "학습자 로그인");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
