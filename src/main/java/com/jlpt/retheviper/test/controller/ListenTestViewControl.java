package com.jlpt.retheviper.test.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jlpt.retheviper.test.bean.Problem;
import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.gui.ListenTestStage;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.service.TestManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ListenTestViewControl implements Initializable {
	// 청해 시험용 컨트롤러

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
	private ProgressBar timeProgress, mp3Progress;

	private int problemNumber = 0;

	private int selectedAnswer = 0;

	private TestManagementService service = TestManagementService.getInstance();

	private List<Problem> tList = service.getLList();

	private String theImg = ""; // 이미지 파일 이름

	private String theMp3 = ""; // mp3 파일 이름

	private Media media; // mp3에 담을 미디어

	public static MediaPlayer mediaPlayer; // mp3 플레이어

	private Score score = Score.builder().subject(Subject.LISTEN).build(); // 임시로 담을 성적

	private int correctAnswer = 0; // 정답 기록용

	private int wrongAnswer = 0; // 오답 기록용

	private int skippedAnswer = 0; // 넘긴 문제 기록용

	private int settedTime = 0; // 설정된 시험 시간

	private boolean getStarted = false; // MP3 세팅 확인용

	public static int timerSetting = 0; // 시험 시간 세팅

	private Task<Void> task; // 시험 시간 측정용

	private Thread thread; // task 설정

	private void startTest() { // 시험 시작
		switch (this.startTestButton.getText()) {
		case "시작":
			this.startTestButton.setText("중단");
			if (this.tList.size() > this.problemNumber) {
				setProblem(problemNumber);
				setTimer();
				startTimerProgress();
			}
			break;
		case "중단":
			this.startTestButton.setText("시작");
			final Optional<ButtonType> result = CreateAlert.withoutHeader(AlertType.CONFIRMATION, "정보", "성적을 저장하고 끝내시겠습니까?\r\n(기존의 성적을 덮어씁니다)");
			if (result.get().equals(ButtonType.OK)) {
				StudentManagementService sm = StudentManagementService.getInstance();
				sm.recordScore(this.score);
				ListenTestStage.stage.hide();
			}
			break;
		}
	}

	private void setTimer() { // 시험 시간 표시
		switch (timerSetting) {
		case 0:
			timer.setText("50분");
			settedTime = 3000;
			break;
		case 1:
			timer.setText("35분");
			settedTime = 2100;
			break;
		case 2:
			timer.setText("20분");
			settedTime = 1200;
			break;
		}
	}

	private void startTimerProgress() { // 진행에 따른 경과 표시
		task = new Task<Void>() {
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
		timeProgress.progressProperty().bind(task.progressProperty());
		thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	private void setProblem(int i) { // 문제를 세팅
		problemNumberLabel.setText(
				this.tList.get(i).getPNumber() + "-"
						+ this.tList.get(i).getSubNumber());
		passageArea.setText(tList.get(i).getPassage());
		firstChoice_radio.setText("1번: " + this.tList.get(i).getFirstChoice());
		secondChoice_radio.setText("2번: " + this.tList.get(i).getSecondChoice());
		thirdChoice_radio.setText("3번: " + this.tList.get(i).getThirdChoice());
		forthChoice_radio.setText("4번: " + this.tList.get(i).getForthChoice());
		if (!this.tList.get(i).getImgSource().equals("nah")
				&& this.tList.get(i).getImgSource() != null) {
			theImg = this.tList.get(i).getImgSource();
			// 이미지 파일이 있을 경우 팝업에 표시
			imagePopupWindowShow();
		}
		final Problem forMp3 = this.tList.get(i);
		try { // mp3 파일을 불러옴
			if (theMp3 == null || !theMp3.equals("mp3/" + forMp3.getMp3Source())) {
				if (getStarted && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING
						&& problemNumber < this.tList.size()) {
					mp3CurrentTime.setText("00:00");
					mediaPlayer.stop();
					changeMp3PlayButton();
				}
				theMp3 = "mp3/" + forMp3.getMp3Source();
				media = new Media(new File(theMp3).toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				getStarted = true;
				mediaPlayer.setOnReady(new Runnable() {
					@Override
					public void run() {
						double millis = mediaPlayer.getTotalDuration().toMillis();
						int totalSeconds = (int) (millis / 1000) % 60;
						int totalMinutes = (int) mediaPlayer.getTotalDuration().toMinutes();
						mp3Length.setText((totalMinutes < 10 ? "0" : "") + totalMinutes + ":" + totalSeconds);
						mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
							@Override
							public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
									Duration newValue) {
								double progress = mediaPlayer.getCurrentTime().toSeconds()
										/ mediaPlayer.getTotalDuration().toSeconds();
								mp3Progress.setProgress(progress);
								int seconds = (int) (mediaPlayer.getCurrentTime().toMillis() / 1000) % 60;
								int minutes = (int) mediaPlayer.getCurrentTime().toMinutes();
								mp3CurrentTime.setText((minutes < 10 ? "0" : "") + minutes + ":"
										+ (seconds < 10 ? "0" : "") + seconds);
							}
						});
					}
				});
				mediaPlayer.setOnEndOfMedia(() -> {
					mp3Progress.setProgress(1.0);
				});
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public void imagePopupWindowShow() { // 첨부 이미지를 띄우는 팝업
		final ImageView imageView = new ImageView(new Image(new File("img/" + theImg).toURI().toString()));
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

	private void submitAnswer() { // 선택지에 따른 답을 입력
		if (startTestButton.getText().equals("시작")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("오류");
			alert.setHeaderText("");
			alert.setContentText("먼저 시작 버튼을 눌러주세요");
			alert.showAndWait();
		} else if (this.service.getLList().size() > problemNumber) {
			if (firstChoice_radio.isSelected()) {
				selectedAnswer = 1;
			} else if (secondChoice_radio.isSelected()) {
				selectedAnswer = 2;
			} else if (thirdChoice_radio.isSelected()) {
				selectedAnswer = 3;
			} else if (forthChoice_radio.isSelected()) {
				selectedAnswer = 4;
			} else if (!firstChoice_radio.isSelected() && !secondChoice_radio.isSelected()
					&& !thirdChoice_radio.isSelected() && !forthChoice_radio.isSelected()) {
				CreateAlert.withoutHeader(AlertType.ERROR, "오류", "제출할 답을 선택해주세요");
			}
			compareWithProblem();
		}
	}

	private void compareWithProblem() { // 입력한 답을 정답과 비교후 기록
		if (this.service.getLList().get(problemNumber).getAnswer() == selectedAnswer) {
			correctAnswer++;
			score.setCorrectAnswer(correctAnswer);
			totalProblemLabel.setText(correctAnswer + wrongAnswer + "");
			correctAnswerLabel.setText(correctAnswer + "");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("아타리");
			alert.setHeaderText("");
			alert.setContentText("정답입니다");
			alert.showAndWait();
		} else {
			wrongAnswer++;
			score.setWrongAnswer(wrongAnswer);
			totalProblemLabel.setText(correctAnswer + wrongAnswer + "");
			wrongAnswerLabel.setText(wrongAnswer + "");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("잔넨");
			alert.setHeaderText("");
			alert.setContentText("오답입니다");
			alert.showAndWait();
		}
		if (this.service.getLList().size() > problemNumber + 1) {
			problemNumber++;
			setProblem(problemNumber);
		} else {
			final Optional<ButtonType> result = CreateAlert.withHeader(AlertType.CONFIRMATION, "정보", "마지막 문제",
					"성적을 저장하고 끝내시겠습니까?\r\n(기존의 성적을 덮어씁니다)");
			if (result.get().equals(ButtonType.OK)) {
				final StudentManagementService sm = StudentManagementService.getInstance();
				sm.recordScore(score);
				ListenTestStage.stage.hide();
			}
		}
	}

	private void goBack() { // 이전 문제로 가기
		if (startTestButton.getText().equals("시작")) {
			CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 시작 버튼을 눌러주세요");
		} else {
			if ((problemNumber - 1) != -1) {
				--skippedAnswer;
				score.setSkippedAnswer(skippedAnswer);
				skippedAnswerLabel.setText(skippedAnswer + "");
				--problemNumber;
				setProblem(problemNumber);
			} else {
				CreateAlert.withoutHeader(AlertType.INFORMATION, "정보", "더 이상 뒤로 갈 수 없습니다");
			}
		}
	}

	private void goNext() { // 다음 문제로 가기
		if (startTestButton.getText().equals("시작")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("오류");
			alert.setHeaderText("");
			alert.setContentText("먼저 시작 버튼을 눌러주세요");
			alert.showAndWait();
		} else {
			if ((problemNumber + 1) < this.tList.size()) {
				++skippedAnswer;
				score.setSkippedAnswer(skippedAnswer);
				skippedAnswerLabel.setText(skippedAnswer + "");
				++problemNumber;
				setProblem(problemNumber);
			} else {
				CreateAlert.withoutHeader(AlertType.INFORMATION, "정보", "더 이상 앞으로 갈 수 없습니다");
			}
		}
	}

	private void playMp3() { // mp3 재생 버튼 클릭시
		if (startTestButton.getText().equals("시작")) {
			CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 시작 버튼을 눌러주세요");
		} else {
			changeMp3PlayButton();
			if (Mp3PlayButton.getText().equals("■")) {
				mediaPlayer.play();
			} else if (Mp3PlayButton.getText().equals("▶")) {
				mediaPlayer.pause();
			}
		}
	}

	private void changeMp3PlayButton() { // mp3 버튼 세팅
		if (mediaPlayer != null && !(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)) {
			Mp3PlayButton.setText("■");
			mediaPlayer.setOnEndOfMedia(() -> {
				mediaPlayer.stop();
			});
		} else {
			Mp3PlayButton.setText("▶");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.firstChoice_radio.setToggleGroup(this.group);
		this.secondChoice_radio.setToggleGroup(this.group);
		this.thirdChoice_radio.setToggleGroup(this.group);
		this.forthChoice_radio.setToggleGroup(this.group);
		this.startTestButton.setOnAction(event -> startTest());
		this.submitAnswerButton.setOnAction(event -> submitAnswer());
		this.previousProblemButton.setOnAction(event -> goBack());
		this.nextProblemButton.setOnAction(event -> goNext());
		this.Mp3PlayButton.setOnAction(event -> playMp3());

	}

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		ListenTestViewControl.mediaPlayer = mediaPlayer;
	}
}
