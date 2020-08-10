package com.jlpt.retheviper.test.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jlpt.retheviper.test.bean.Problem;
import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.gui.ElseTestStage;
import com.jlpt.retheviper.test.gui.ListenTestStage;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.service.TestManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class ElseTestViewControl implements Initializable {
	// 청해 제외 3과목 시험용 컨트롤러

	@FXML
	private final ToggleGroup group = new ToggleGroup();

	@FXML
	private RadioButton firstChoice_radio, secondChoice_radio, thirdChoice_radio, forthChoice_radio;

	@FXML
	private TextArea passageArea;

	@FXML
	private Button startTestButton, previousProblemButton, nextProblemButton, submitAnswerButton, Mp3PlayButton;

	@FXML
	private Label problemNumberLabel, mp3CurrentTime, mp3Length, totalProblemLabel, correctAnswerLabel,
			wrongAnswerLabel, skippedAnswerLabel, timer;

	@FXML
	private ProgressBar timeProgress;

	private int problemNumber = 0;

	private int selectedAnswer = 0;

	private TestManagementService service = TestManagementService.getInstance();

	private String imageFileName = ""; // 이미지 파일 이름

	@Getter
	@Setter
	private static Subject subject; // 과목 이름

	private Score score = Score.builder().build();

	private List<Problem> tList; // 임시로 담을 리스트

	private int correctAnswer = 0; // 정답 기록용

	private int wrongAnswer = 0; // 오답 기록용

	private int skippedAnswer = 0; // 넘긴 문제 기록용

	private int settedTime = 0; // 설정된 시험 시간

	public static int timerSetting = 0; // 시험 시간 세팅

	private Task<Void> task; // 시험 시간 측정용

	private Thread thread; // task 설정

	private void startTest() { // 시험 시작
		switch (this.startTestButton.getText()) {
		case "시작":
			this.startTestButton.setText("중단");
			switch (subject) { // 선택한 과목에 따른 문제 세팅
			case VOCABULARY:
				this.tList = this.service.getVList();
				this.score.setSubject(Subject.VOCABULARY);
				break;
			case GRAMMER:
				this.tList = this.service.getGList();
				this.score.setSubject(Subject.GRAMMER);
				break;
			case READ:
				this.tList = this.service.getRList();
				this.score.setSubject(Subject.READ);
				break;
			default:
				break;
			}

			if (this.tList.size() > this.problemNumber) {
				setProblem(this.problemNumber);
			}

			setTimer();
			startTimerProgress();
			break;
		case "중단":
			this.startTestButton.setText("시작");
			final Optional<ButtonType> result = CreateAlert.withoutHeader(AlertType.CONFIRMATION, "정보",
					"성적을 저장하고 끝내시겠습니까?\r\n(기존의 성적을 덮어씁니다)");
			if (result.get() == ButtonType.OK) {
				final StudentManagementService sm = StudentManagementService.getInstance();
				sm.recordScore(score);
				ListenTestStage.stage.hide();
			}
			break;
		}

	}

	private void setTimer() { // 시험 시간 표시
		switch (timerSetting) {
		case 0:
			this.timer.setText("55분");
			this.settedTime = 3300;
			break;
		case 1:
			this.timer.setText("40분");
			this.settedTime = 2400;
			break;
		case 2:
			this.timer.setText("25분");
			this.settedTime = 1500;
			break;
		}
	}

	private void startTimerProgress() { // 진행에 따른 경과 표시
		this.task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (int i = 0; i <= settedTime; i++) {
					updateProgress(i, settedTime);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {

					}
				}
				return null;
			}

		};
		this.timeProgress.progressProperty().bind(this.task.progressProperty());
		this.thread = new Thread(this.task);
		this.thread.setDaemon(true);
		this.thread.start();
	}

	private void setProblem(int i) { // 문제를 세팅
		this.problemNumberLabel.setText(this.tList.get(i).getPNumber() + "-" + this.tList.get(i).getSubNumber());
		this.passageArea.setText(this.tList.get(i).getPassage());
		this.firstChoice_radio.setText("1번: " + this.tList.get(i).getFirstChoice());
		this.secondChoice_radio.setText("2번: " + this.tList.get(i).getSecondChoice());
		this.thirdChoice_radio.setText("3번: " + this.tList.get(i).getThirdChoice());
		this.forthChoice_radio.setText("4번: " + this.tList.get(i).getForthChoice());
		if (!this.tList.get(i).getImgSource().equals("nah") && this.tList.get(i).getImgSource() != null) {
			this.imageFileName = this.tList.get(i).getImgSource();
			// 이미지 파일이 있을 경우 창에 표시
			imagePopupWindowShow();
		}
	}

	private void submitAnswer() { // 선택지에 따른 답을 입력
		if (this.startTestButton.getText().equals("시작")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("오류");
			alert.setHeaderText("");
			alert.setContentText("먼저 시작 버튼을 눌러주세요");
			alert.showAndWait();
		} else if (this.tList.size() > this.problemNumber) {
			if (this.firstChoice_radio.isSelected()) {
				this.selectedAnswer = 1;
			} else if (this.secondChoice_radio.isSelected()) {
				this.selectedAnswer = 2;
			} else if (this.thirdChoice_radio.isSelected()) {
				this.selectedAnswer = 3;
			} else if (this.forthChoice_radio.isSelected()) {
				this.selectedAnswer = 4;
			} else if (!this.firstChoice_radio.isSelected() && !this.secondChoice_radio.isSelected()
					&& !this.thirdChoice_radio.isSelected() && !this.forthChoice_radio.isSelected()) {
				CreateAlert.withoutHeader(AlertType.ERROR, "오류", "제출할 답을 선택해주세요");
			}
			compareWithProblem();
		}
	}

	public void imagePopupWindowShow() { // 첨부 이미지를 띄우는 팝업
		final ImageView imageView = new ImageView(new Image(new File("img/" + this.imageFileName).toURI().toString()));
		final ScrollPane pane = new ScrollPane();
		final Stage stage = new Stage();
		imageView.setFitWidth(735);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		pane.setContent(imageView);
		stage.setScene(new Scene(pane, 750, 600));
		stage.setTitle("참고 이미지");
		stage.setOnCloseRequest(e -> {
			stage.close();
		});
		stage.showAndWait();
	}

	private void compareWithProblem() { // 입력한 답을 정답과 비교후 기록
		if (this.tList.get(this.problemNumber).getAnswer() == this.selectedAnswer) {
			this.correctAnswer++;
			score.setCorrectAnswer(correctAnswer);
			this.totalProblemLabel.setText(this.correctAnswer + this.wrongAnswer + "");
			this.correctAnswerLabel.setText(this.correctAnswer + "");
			CreateAlert.withoutHeader(AlertType.INFORMATION, "정답", "정답입니다");
		} else {
			this.wrongAnswer++;
			score.setWrongAnswer(wrongAnswer);
			this.totalProblemLabel.setText(this.correctAnswer + this.wrongAnswer + "");
			this.wrongAnswerLabel.setText(this.wrongAnswer + "");
			CreateAlert.withoutHeader(AlertType.ERROR, "오답", "오답입니다");
		}
		if (this.tList.size() > this.problemNumber + 1) {
			this.problemNumber++;
			setProblem(this.problemNumber);
		} else {
			final Optional<ButtonType> result = CreateAlert.withHeader(AlertType.CONFIRMATION, "정보", "마지막 문제",
					"성적을 저장하고 끝내시겠습니까?\r\n(기존의 성적을 덮어씁니다)");
			if (result.get().equals(ButtonType.OK)) {
				final StudentManagementService sm = StudentManagementService.getInstance();
				sm.recordScore(score);
				ElseTestStage.stage.hide();
			} else {
				// ... user chose CANCEL or closed the dialog
			}
		}
	}

	private void goBack() { // 이전 문제로 가기
		if (this.startTestButton.getText().equals("시작")) {
			CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 시작 버튼을 눌러주세요");
		} else {
			if ((this.problemNumber - 1) != -1) {
				--this.skippedAnswer;
				score.setSkippedAnswer(skippedAnswer);
				this.skippedAnswerLabel.setText(this.skippedAnswer + "");
				setProblem(--this.problemNumber);
			} else {
				CreateAlert.withoutHeader(AlertType.INFORMATION, "정보", "더 이상 뒤로 갈 수 없습니다");
			}
		}
	}

	private void goNext() { // 다음 문제로 가기
		if (this.startTestButton.getText().equals("시작")) {
			CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 시작 버튼을 눌러주세요");
		} else {
			if ((this.problemNumber + 1) < this.tList.size()) {
				++this.skippedAnswer;
				score.setSkippedAnswer(skippedAnswer);
				this.skippedAnswerLabel.setText(this.skippedAnswer + "");
				setProblem(++this.problemNumber);
			} else {
				CreateAlert.withoutHeader(AlertType.INFORMATION, "정보", "더 이상 앞으로 갈 수 없습니다");
			}
		}
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		this.firstChoice_radio.setToggleGroup(this.group);
		this.secondChoice_radio.setToggleGroup(this.group);
		this.thirdChoice_radio.setToggleGroup(this.group);
		this.forthChoice_radio.setToggleGroup(this.group);
		this.startTestButton.setOnAction(event -> startTest());
		this.submitAnswerButton.setOnAction(event -> submitAnswer());
		this.previousProblemButton.setOnAction(event -> goBack());
		this.nextProblemButton.setOnAction(event -> goNext());
	}
}