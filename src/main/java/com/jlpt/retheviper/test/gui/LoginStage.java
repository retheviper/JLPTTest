package com.jlpt.retheviper.test.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginStage extends Stage {

	public static Stage stage;

	public LoginStage() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("LoginView.fxml"));
			Parent root = loader.load();
			stage.setTitle("학습자 로그인");
			stage.setResizable(false);
			final Scene longinScene = new Scene(root);
			longinScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(final KeyEvent event) {
					if (event.getCode().equals(KeyCode.ESCAPE)) {
						stage.close();
					}
				}
			});
			stage.setScene(longinScene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Stage getStage() {
		return stage;
	}

	@SuppressWarnings("static-access")
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
