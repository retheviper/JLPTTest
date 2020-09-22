package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.util.Calculater;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreViewControl implements Initializable {

    private static final String COUNT = " 개";
    private static final String IN_PROBLEM = " 문제 중 ";
    private static final String OF_PROBLEM = " 문제";
    @FXML
    private Label totalCorrectLabel;
    @FXML
    private Label totalWrongLabel;
    @FXML
    private Label totalSolvedLabel;
    @FXML
    private Label totalGradeLabel;
    @FXML
    private Label vocaCorrectLabel;
    @FXML
    private Label vocaWrongLabel;
    @FXML
    private Label vocaSolvedLabel;
    @FXML
    private Label gramCorrectLabel;
    @FXML
    private Label gramWrongLabel;
    @FXML
    private Label gramSolvedLabel;
    @FXML
    private Label readCorrectLabel;
    @FXML
    private Label readWrongLabel;
    @FXML
    private Label readSolvedLabel;
    @FXML
    private Label listenCorrectLabel;
    @FXML
    private Label listenWrongLabel;
    @FXML
    private Label listenSolvedLabel;
    private int totalCorrect;
    private int totalWrong;
    private int totalSolved;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final List<Score> scores = StudentManagementService.getInstance().getStudentData().getScores();

        scores.forEach(s -> {
            totalCorrect += s.getCorrectAnswer();
            totalWrong += s.getWrongAnswer();
            totalSolved += s.getCorrectAnswer() + s.getWrongAnswer();
        });

        scores.stream().filter(s -> s.getSubject().equals(Subject.VOCABULARY)).findAny().ifPresent(s -> {
            this.vocaCorrectLabel.setText(s.getCorrectAnswer() + COUNT);
            this.vocaWrongLabel.setText(s.getWrongAnswer() + COUNT);
            this.vocaSolvedLabel.setText(scores.size() + IN_PROBLEM +
                    this.totalSolved + OF_PROBLEM); // TODO 전체문제사이즈
        });

        scores.stream().filter(s -> s.getSubject().equals(Subject.GRAMMER)).findAny().ifPresent(s -> {
            this.gramCorrectLabel.setText(s.getCorrectAnswer() + COUNT);
            this.gramWrongLabel.setText(s.getWrongAnswer() + COUNT);
            this.gramSolvedLabel.setText(scores.size() + IN_PROBLEM +
                    this.totalSolved + OF_PROBLEM); // TODO 전체문제사이즈
        });

        scores.stream().filter(s -> s.getSubject().equals(Subject.READ)).findAny().ifPresent(s -> {
            this.readCorrectLabel.setText(s.getCorrectAnswer() + COUNT);
            this.readWrongLabel.setText(s.getWrongAnswer() + COUNT);
            this.readSolvedLabel.setText(scores.size() + IN_PROBLEM +
                    this.totalSolved + OF_PROBLEM); // TODO 전체문제사이즈
        });

        scores.stream().filter(s -> s.getSubject().equals(Subject.LISTEN)).findAny().ifPresent(s -> {
            this.listenCorrectLabel.setText(s.getCorrectAnswer() + COUNT);
            this.listenWrongLabel.setText(s.getWrongAnswer() + COUNT);
            this.listenSolvedLabel.setText(scores.size() + IN_PROBLEM +
                    this.totalSolved + OF_PROBLEM); // TODO 전체문제사이즈
        });

        totalCorrectLabel.setText(totalCorrect + " 개");
        totalWrongLabel.setText(totalWrong + " 개");
        totalSolvedLabel.setText(totalSolved + " 문제");

        totalGradeLabel.setText(Calculater.calculateGrade(totalCorrect, totalSolved));
    }
}
