package com.jlpt.retheviper.test.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RemoveStudentStage {

	public static Stage stage;

	public RemoveStudentStage() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("RemoveStudentView.fxml"));
			Parent root = loader.load();
			stage.setTitle("학습자 삭제");
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
