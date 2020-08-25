package com.jlpt.retheviper.test.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jlpt.retheviper.test.bean.Student;
import com.jlpt.retheviper.test.gui.RegistStudentStage;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class RegistStudentViewControl implements Initializable {

    private StudentManagementService service = StudentManagementService.getInstance();

    @FXML
    private Button registButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.registButton.setOnAction(event -> doRegistCheck());
        this.passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                doRegistCheck();
            }
        });
        this.cancelButton.setOnAction(event -> RegistStudentStage.stage.hide());
    }

    @FXML
    private void doRegistCheck() {
        if (this.service
                .registStudent(Student.builder().id(idField.getText()).password(passwordField.getText()).build())) {
            CreateAlert.withoutHeader(AlertType.INFORMATION, "알림", "정상적으로 등록되었습니다");
            RegistStudentStage.stage.hide();
        } else {
            CreateAlert.withoutHeader(AlertType.ERROR, "알림", "입력한 ID로는 등록할 수 없습니다");
        }
    }
}
