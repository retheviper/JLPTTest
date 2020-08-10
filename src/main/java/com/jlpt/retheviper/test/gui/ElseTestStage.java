package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.Main;
import com.jlpt.retheviper.test.controller.ElseTestViewControl;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ElseTestStage extends Stage {

	public static Stage stage;

	public ElseTestStage() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ElseTestView.fxml"));
			Parent root = loader.load();
			stage.setTitle(ElseTestViewControl.getSubject().getValue() + " 과목 모의고사");
			stage.setResizable(false);
			final Scene elseTestScene = new Scene(root);
			elseTestScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(final KeyEvent event) {
					if (event.getCode().equals(KeyCode.ESCAPE)) {
						stage.close();
					}
				}
			});
			stage.setScene(elseTestScene);
			stage.show();
			stage.setOnCloseRequest(event -> Main.getPrimaryStage().setIconified(false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
