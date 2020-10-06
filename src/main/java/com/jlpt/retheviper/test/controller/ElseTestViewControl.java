package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.bean.Problem;
import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.gui.ElseTestStage;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.service.TestManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ElseTestViewControl implements Initializable {
    // 청해 제외 3과목 시험용 컨트롤러

    public static int timerSetting = 0; // 시험 시간 세팅
    @Getter
    @Setter
    private static Subject subject; // 과목 이름
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
    private final TestManagementService service = TestManagementService.getInstance();
    private String imageFileName = ""; // 이미지 파일 이름
    private final Score score = Score.builder().build();
    private List<Problem> tList; // 임시로 담을 리스트
    private int correctAnswer = 0; // 정답 기록용
    private int wrongAnswer = 0; // 오답 기록용
    private int skippedAnswer = 0; // 넘긴 문제 기록용
    private int setTime = 0; // 설정된 시험 시간

    private void startTest() { // 시험 시작
        switch (this.startTestButton.getText()) {
            case "시작":
                this.startTestButton.setText("중단");
                this.tList = this.service.getProblem(subject);
                this.score.setSubject(subject);
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
                result.ifPresent(this::exiting);
                break;
        }
    }

    private void setTimer() { // 시험 시간 표시
        switch (timerSetting) {
            case 0:
                this.timer.setText("55분");
                this.setTime = 3300;
                break;
            case 1:
                this.timer.setText("40분");
                this.setTime = 2400;
                break;
            case 2:
                this.timer.setText("25분");
                this.setTime = 1500;
                break;
        }
    }

    private void startTimerProgress() { // 진행에 따른 경과 표시
        // 시험 시간 측정용
        final Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                for (int i = 0; i <= setTime; i++) {
                    updateProgress(i, setTime);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ignored) {
                    }
                }
                return null;
            }
        };
        this.timeProgress.progressProperty().bind(task.progressProperty());
        // task 설정
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void setProblem(final int index) { // 문제를 세팅
        this.problemNumberLabel
                .setText(this.tList.get(index).getPNumber() + "-" + this.tList.get(index).getSubNumber());
        this.passageArea.setText(this.tList.get(index).getPassage());
        this.firstChoice_radio.setText("1번: " + this.tList.get(index).getFirstChoice());
        this.secondChoice_radio.setText("2번: " + this.tList.get(index).getSecondChoice());
        this.thirdChoice_radio.setText("3번: " + this.tList.get(index).getThirdChoice());
        this.forthChoice_radio.setText("4번: " + this.tList.get(index).getForthChoice());
        if (!this.tList.get(index).getImgSource().equals("nah") && this.tList.get(index).getImgSource() != null) {
            this.imageFileName = this.tList.get(index).getImgSource();
            // 이미지 파일이 있을 경우 창에 표시
            imagePopupWindowShow();
        }
    }

    private void submitAnswer() { // 선택지에 따른 답을 입력
        if (this.startTestButton.getText().equals("시작")) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 시작 버튼을 눌러주세요");
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
            this.correctAnswerLabel.setText(String.format("%d", this.correctAnswer));
            CreateAlert.withoutHeader(AlertType.INFORMATION, "정답", "정답입니다");
        } else {
            this.wrongAnswer++;
            score.setWrongAnswer(wrongAnswer);
            this.wrongAnswerLabel.setText(this.wrongAnswer + "");
            CreateAlert.withoutHeader(AlertType.ERROR, "오답", "오답입니다");
        }
        this.totalProblemLabel.setText(String.format("%d", this.correctAnswer + this.wrongAnswer));
        if (this.tList.size() > this.problemNumber + 1) {
            this.problemNumber++;
            setProblem(this.problemNumber);
        } else {
            final Optional<ButtonType> result = CreateAlert.withHeader(AlertType.CONFIRMATION, "정보", "마지막 문제",
                    "성적을 저장하고 끝내시겠습니까?\r\n(기존의 성적을 덮어씁니다)");
            result.ifPresent(this::exiting);
        }
    }

    private void goBack() { // 이전 문제로 가기
        if (this.startTestButton.getText().equals("시작")) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 시작 버튼을 눌러주세요");
        } else {
            if ((this.problemNumber - 1) != -1) {
                --this.skippedAnswer;
                setSkip(skippedAnswer);
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
                setSkip(skippedAnswer);
                setProblem(++this.problemNumber);
            } else {
                CreateAlert.withoutHeader(AlertType.INFORMATION, "정보", "더 이상 앞으로 갈 수 없습니다");
            }
        }
    }

    private void setSkip(final int skippedAnswer) {
        score.setSkippedAnswer(skippedAnswer);
        this.skippedAnswerLabel.setText(String.valueOf(this.skippedAnswer));
    }

    private void exiting(final ButtonType type) {
        if (type == ButtonType.OK) {
            final StudentManagementService sm = StudentManagementService.getInstance();
            sm.recordScore(score);
            ElseTestStage.getStage().hide();
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