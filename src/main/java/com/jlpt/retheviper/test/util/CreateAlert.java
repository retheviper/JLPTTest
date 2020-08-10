package com.jlpt.retheviper.test.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class CreateAlert {

	private CreateAlert() {

	}

	public static Optional<ButtonType> withoutHeader(final AlertType type, final String title, final String content) {
		return withHeader(type, title, "", content);
	}

	public static Optional<ButtonType> withHeader(final AlertType type, final String title, final String header, final String content) {
		final Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert.showAndWait();
	}
}
