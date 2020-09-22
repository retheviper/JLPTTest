package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.Main;
import com.jlpt.retheviper.test.gui.LoginStage;
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

public class LoginViewContol implements Initializable {

    private final StudentManagementService service = StudentManagementService.getInstance();

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    private void doLoginCheck() {
        if (this.service.login(this.idField.getText(), this.passwordField.getText())) {
            CreateAlert.withHeader(AlertType.INFORMATION, "알림", "로그인 성공", service.getLoginedUser() + " 님, 환영합니다");
            Main.getPrimaryStage().setTitle("JLPT Test Ver 0.1 (학습자 " + service.getLoginedUser() + " 로그인함)");
            LoginStage.getStage().hide();
        } else {
            CreateAlert.withHeader(AlertType.ERROR, "알림", "로그인 실패", "ID나 비밀번호를 확인하세요");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.confirmButton.setOnAction(event -> doLoginCheck());
        this.passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doLoginCheck();
            }
        });
        this.cancelButton.setOnAction(event -> LoginStage.getStage().hide());
    }
}
