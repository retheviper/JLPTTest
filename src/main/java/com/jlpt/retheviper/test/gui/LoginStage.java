package com.jlpt.retheviper.test.gui;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginStage {

    @Getter
    private static Stage stage;

    public static void createStage() {
        try {
            stage = StageManager.create("LoginView.fxml", "학습자 로그인");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
