package com.jlpt.retheviper.test.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jlpt.retheviper.test.Main;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.gui.ElseTestStage;
import com.jlpt.retheviper.test.gui.ListenTestStage;
import com.jlpt.retheviper.test.gui.PrepareForTestStage;
import com.jlpt.retheviper.test.service.TestManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class PrepareForTestViewControl implements Initializable {
	// 시험 설정용 창 컨트롤러

	@FXML
	private ComboBox<String> setSubject;

	@FXML
	private ComboBox<String> setTimer;

	@FXML
	private Button startTestButton;

	@FXML
	private Button cancelButton;

	private ObservableList<String> subject = FXCollections.observableArrayList(Subject.VOCABULARY.getValue(),
			Subject.GRAMMER.getValue(), Subject.READ.getValue(), Subject.LISTEN.getValue());

	private ObservableList<String> elseTimer = FXCollections.observableArrayList("정규(55분)", "숙련(40분)", "신속(25분)");

	private ObservableList<String> listenTimer = FXCollections.observableArrayList("정규(50분)", "숙련(35분)", "신속(20분)");

	private void letTheTestBegin() {
		if (this.setSubject.getValue() == null) {
			CreateAlert.withoutHeader(AlertType.ERROR, "오류", "과목을 선택해주세요");
		} else if (setTimer.getValue() == null) {
			CreateAlert.withoutHeader(AlertType.ERROR, "오류", "시험 시간을 선택해주세요");
		} else {
			final TestManagementService service = TestManagementService.getInstance();
			switch (this.setSubject.getValue()) { // 과목 선택에 따른 분기
			case "언어지식(문자·어휘)":
				if (service.getVList() == null || service.getVList().size() == 0) {
					CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제가 등록되어 있지 않습니다.\r\n관리자에게 문의하세요.");
					break;
				}
				ElseTestViewControl.setSubject(Subject.VOCABULARY);
				ElseTestViewControl.timerSetting = setTimer.getSelectionModel().getSelectedIndex();
				new ElseTestStage();
				PrepareForTestStage.stage.hide();
				Main.getPrimaryStage().setIconified(true);
				break;
			case "언어지식(문법)":
				if (service.getGList() == null || service.getGList().size() == 0) {
					CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제가 등록되어 있지 않습니다.\r\n관리자에게 문의하세요.");
					break;
				}
				ElseTestViewControl.setSubject(Subject.GRAMMER);
				ElseTestViewControl.timerSetting = setTimer.getSelectionModel().getSelectedIndex();
				new ElseTestStage();
				PrepareForTestStage.stage.hide();
				Main.getPrimaryStage().setIconified(true);
				break;
			case "독해":
				if (service.getRList() == null || service.getRList().size() == 0) {
					CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제가 등록되어 있지 않습니다.\r\n관리자에게 문의하세요.");
					break;
				}
				ElseTestViewControl.setSubject(Subject.READ);
				ElseTestViewControl.timerSetting = setTimer.getSelectionModel().getSelectedIndex();
				new ElseTestStage();
				PrepareForTestStage.stage.hide();
				Main.getPrimaryStage().setIconified(true);
				break;
			case "청해":
				if (service.getLList() == null || service.getLList().size() == 0) {
					CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제가 등록되어 있지 않습니다.\r\n관리자에게 문의하세요.");
					break;
				}
				ListenTestViewControl.timerSetting = setTimer.getSelectionModel().getSelectedIndex();
				new ListenTestStage();
				PrepareForTestStage.stage.hide();
				Main.getPrimaryStage().setIconified(true);
				break;
			}
		}
	}

	private void timerChange() {
		if (setSubject.getValue().equals("청해")) {
			setTimer.setItems(listenTimer);
		} else {
			setTimer.setItems(elseTimer);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setSubject.setItems(subject);
		setSubject.valueProperty().addListener(event -> timerChange());
		setTimer.setItems(elseTimer);
		startTestButton.setOnAction(event -> letTheTestBegin());
		cancelButton.setOnAction(event -> PrepareForTestStage.stage.hide());
	}

}
