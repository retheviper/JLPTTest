package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.bean.Student;
import com.jlpt.retheviper.test.gui.RegisterStudentStage;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterStudentViewControl implements Initializable {

    private final StudentManagementService service = StudentManagementService.getInstance();

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.registerButton.setOnAction(event -> doRegisterCheck());
        this.passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                doRegisterCheck();
            }
        });
        this.cancelButton.setOnAction(event -> RegisterStudentStage.getStage().hide());
    }

    @FXML
    private void doRegisterCheck() {
        if (this.service
                .registerStudent(Student.builder().id(idField.getText()).password(passwordField.getText()).build())) {
            CreateAlert.withoutHeader(AlertType.INFORMATION, "알림", "정상적으로 등록되었습니다");
            RegisterStudentStage.getStage().hide();
        } else {
            CreateAlert.withoutHeader(AlertType.ERROR, "알림", "입력한 ID로는 등록할 수 없습니다");
        }
    }
}
