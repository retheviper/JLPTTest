package com.jlpt.retheviper.test.gui;

import com.jlpt.retheviper.test.Main;
import com.jlpt.retheviper.test.controller.ListenTestViewControl;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ListenTestStage extends Stage {

	public static Stage stage;

	public ListenTestStage() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ListenTestView.fxml"));
			Parent root = loader.load();
			stage.setTitle("청해 과목 모의고사");
			stage.setResizable(false);
			final Scene listenTestScene = new Scene(root);
			listenTestScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(final KeyEvent event) {
					if (event.getCode().equals(KeyCode.ESCAPE)) {
						stage.close();
					}
				}
			});
			stage.setScene(listenTestScene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if (ListenTestViewControl.mediaPlayer != null
							&& ListenTestViewControl.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
						ListenTestViewControl.mediaPlayer.stop();
					}
					Main.getPrimaryStage().setIconified(false);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
