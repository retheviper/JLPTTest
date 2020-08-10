package com.jlpt.retheviper.test.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegistStudentStage extends Stage {

	public static Stage stage;

	public RegistStudentStage() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RegistStudentView.fxml"));
			Parent root = loader.load();
			stage.setTitle("학습자 등록");
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
