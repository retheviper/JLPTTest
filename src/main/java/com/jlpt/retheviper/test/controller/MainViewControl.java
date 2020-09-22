package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.Main;
import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.gui.*;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.util.Calculator;
import com.jlpt.retheviper.test.util.CreateAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewControl implements Initializable {
    // 메인 화면 컨트롤러

    @FXML
    private MenuItem print_m, exit_m, registStudent_m, removeStudent_m, startTest_m, viewScore_m, admin_m,
            dataManagement_m, programInfo_m, loginStudent_m;

    private final StudentManagementService service = StudentManagementService.getInstance();

    private Score vocaScore;

    private Score gramScore;

    private Score readScore;

    private Score listenScore;

    private int totalCorrect;

    private int totalWrong;

    private int totalSkipped;

    private int totalSolved;

    private String grade = "F";

    private void printTestResult() { // 성적 출력
        if (this.service.isLogin()) {
            checkAndSetScore();
            if (this.totalSolved == 0) {
                CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제를 푼 이력이 없습니다");
                return;
            }
            checkAndSetScore();
            final LocalDate date = LocalDate.now();
            final String theDate = date.format(DateTimeFormatter.ISO_DATE);
            final String theScore = this.service.getLoginUser() +
                    "님의 시험 결과  (" +
                    theDate +
                    ")\r\n\r\n================\r\n◈ 언어지식(문자·어휘)\r\n================\r\n▶ 정답: " +
                    this.vocaScore.getCorrectAnswer() +
                    " 개\r\n▶ 오답: " +
                    this.vocaScore.getWrongAnswer() +
                    " 개\r\n▶ 넘긴 문제: " +
                    this.vocaScore.getSkippedAnswer() +
                    " 개\r\n\r\n================\r\n◈ 언어지식(문법)\r\n================\r\n▶ 정답: " +
                    this.gramScore.getCorrectAnswer() +
                    " 개\r\n▶ 오답: " +
                    this.gramScore.getWrongAnswer() +
                    " 개\r\n▶ 넘긴 문제: " +
                    this.gramScore.getSkippedAnswer() +
                    " 개\r\n\r\n================\r\n◈ 독해\r\n================\r\n▶ 정답: " +
                    this.readScore.getCorrectAnswer() +
                    " 개\r\n▶ 오답: " +
                    this.readScore.getWrongAnswer() +
                    " 개\r\n▶ 넘긴 문제: " +
                    this.readScore.getSkippedAnswer() +
                    " 개\r\n\r\n================\r\n◈ 청해\r\n================\r\n▶ 정답: " +
                    this.listenScore.getCorrectAnswer() +
                    " 개\r\n▶ 오답: " +
                    this.listenScore.getWrongAnswer() +
                    " 개\r\n▶ 넘긴 문제: " +
                    this.listenScore.getSkippedAnswer() +
                    " 개\r\n\r\n================\r\n▣ 종합 결과\r\n================\r\n▶ 총 정답 수: " +
                    this.totalCorrect +
                    " 개\r\n▶ 총 오답 수: " +
                    this.totalWrong +
                    " 개\r\n▶ 총 넘긴 문제: " +
                    this.totalSkipped +
                    " 개\r\n▶ 총 푼 문제: " +
                    this.totalSolved +
                    " 개\r\n\r\n▷ 종합 평가등급: " +
                    this.grade +
                    " 등급";
            final FileChooser fileChooser = new FileChooser();
            final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.setInitialFileName(theDate + "_" + this.service.getLoginUser() + "_시험결과");
            fileChooser.setTitle("학습자 성적 출력");
            fileChooser.getExtensionFilters().add(extFilter);
            final File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
            if (file != null) {
                saveFile(theScore, file);
            }
        } else {
            needLogin();
        }
    }

    private void checkAndSetScore() { // 점수 저장 여부 확인 및 세팅
        final List<Score> scores = this.service.getStudentData().getScores();
        scores.forEach(s -> {
            this.totalCorrect += s.getCorrectAnswer();
            this.totalWrong += s.getWrongAnswer();
            this.totalSkipped += s.getSkippedAnswer();
            switch (s.getSubject()) {
                case VOCABULARY:
                    this.vocaScore = s;
                    break;
                case GRAMMER:
                    this.gramScore = s;
                    break;
                case READ:
                    this.readScore = s;
                    break;
                case LISTEN:
                    this.listenScore = s;
                    break;
            }
        });
        this.totalSolved = this.totalCorrect + this.totalWrong;
        this.grade = Calculator.calculateGrade(this.totalCorrect, this.totalSolved);
    }

    private void saveFile(final String content, final File file) { // TXT 기록용
        try {
            Files.writeString(file.toPath(), content, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void needLogin() {
        CreateAlert.withoutHeader(AlertType.ERROR, "오류", "학습자 로그인이 필요합니다");
    }

    private void startTest() { // 시험 시작
        if (this.service.isLogin()) {
            PrepareForTestStage.createStage();
        } else {
            needLogin();
        }
    }

    private void registerStudent() { // 학습자 등록
        if (this.service.isLogin()) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 로그아웃 해 주십시오");
        } else {
            RegisterStudentStage.createStage();
        }
    }

    private void removeStudent() { // 학습자 삭제
        if (this.service.isLogin()) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "먼저 로그아웃 해 주십시오");
        } else {
            RemoveStudentStage.createStage();
        }
    }

    private void loginStudent() { // 학습자 로그인
        if (this.service.isLogin()) {
            this.service.setLogin(false);
            Main.getPrimaryStage().setTitle("JLPT Test Ver 0.1");
            CreateAlert.withoutHeader(AlertType.INFORMATION, "알림", "로그아웃 했습니다");
        } else {
            LoginStage.createStage();
        }
    }

    private void viewScore() { // 학습자 성적 확인 창
        if (this.service.isLogin()) {
            ScoreStage.createStage();
        } else {
            needLogin();
        }
    }

    private void adminLogin() { // 관리자 로그인
        if (this.service.isAdmin()) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "이미 로그인된 상태입니다");
        } else {
            final TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("관리자 로그인");
            dialog.setHeaderText(null);
            dialog.setContentText("관리자 비밀번호를 입력하세요");
            final Optional<String> result = dialog.showAndWait();
            result.ifPresent(password -> {
                if ("1234".equals(password)) {
                    this.service.setAdmin(true);
                    CreateAlert.withoutHeader(AlertType.INFORMATION, "성공", "관리자로 로그인했습니다");
                } else {
                    CreateAlert.withoutHeader(AlertType.ERROR, "오류", "관리자 비밀번호가 일치하지 않습니다");
                }
            });
        }
    }

    private void dataManagement() { // 문제 관리 창 띄우기
        if (this.service.isAdmin()) {
            DataManagementStage.createStage();
        } else {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "관리자 로그인이 필요합니다");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.print_m.setOnAction(event -> printTestResult());
        this.exit_m.setOnAction(event -> Platform.exit());
        this.registStudent_m.setOnAction(event -> registerStudent());
        this.removeStudent_m.setOnAction(event -> removeStudent());
        this.loginStudent_m.setOnAction(event -> loginStudent());
        this.startTest_m.setOnAction(event -> startTest());
        this.viewScore_m.setOnAction(event -> viewScore());
        this.admin_m.setOnAction(event -> adminLogin());
        this.dataManagement_m.setOnAction(event -> dataManagement());
        this.programInfo_m.setOnAction(event -> {
            CreateAlert.withHeader(AlertType.INFORMATION, "프로그램 정보", "JLPT Test Ver 0.1",
                    "35기 SCIT 마스터 1차 팀 프로젝트 제출용 프로토타입\r\nMade by: C반 김영빈, 박재준");
        });
    }
}
