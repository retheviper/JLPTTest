package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.Main;
import com.jlpt.retheviper.test.controller.ListenTestViewControl;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class ListenTestStage {

    @Getter
    private static Stage stage;

    public ListenTestStage() {
        try {
            stage = StageManager.create("ListenTestView.fxml", "청해 과목 모의고사");
            stage.show();
            stage.setOnCloseRequest(event -> {
                if (ListenTestViewControl.mediaPlayer != null
                        && ListenTestViewControl.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    ListenTestViewControl.mediaPlayer.stop();
                }
                Main.getPrimaryStage().setIconified(false);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
