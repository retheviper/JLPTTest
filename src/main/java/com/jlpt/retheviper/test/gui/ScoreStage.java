package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.service.StudentManagementService;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ScoreStage extends Stage {

	private Stage stage;

	public ScoreStage() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ScoreView.fxml"));
			Parent root = loader.load();
			final Scene scoreScene = new Scene(root);
			scoreScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(final KeyEvent event) {
					if (event.getCode().equals(KeyCode.ESCAPE)) {
						stage.close();
					}
				}
			});
			stage.setScene(scoreScene);
			stage.setResizable(false);
			stage.setTitle(StudentManagementService.getInstance().getLoginedUser() + "님의 점수");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
