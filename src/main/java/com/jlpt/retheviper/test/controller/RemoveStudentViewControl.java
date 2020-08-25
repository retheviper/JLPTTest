package com.jlpt.retheviper.test.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jlpt.retheviper.test.gui.RemoveStudentStage;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class RemoveStudentViewControl implements Initializable {

    private StudentManagementService service = StudentManagementService.getInstance();

    @FXML
    private Button cancelButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.removeButton.setOnAction(event -> doRemoveCheck());
        this.passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                doRemoveCheck();
            }
        });
        this.cancelButton.setOnAction(event -> RemoveStudentStage.stage.hide());
    }

    @FXML
    private void doRemoveCheck() {
        if (this.service.removeStudent(this.idField.getText(), this.passwordField.getText())) {
            CreateAlert.withoutHeader(AlertType.INFORMATION, "알림", "정상적으로 삭제되었습니다");
            RemoveStudentStage.stage.hide();
        } else {
            CreateAlert.withoutHeader(AlertType.ERROR, "알림", "입력한 ID가 존재하지 않습니다");
        }
    }
}
