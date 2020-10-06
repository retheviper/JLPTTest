package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.service.StudentManagementService;
import com.jlpt.retheviper.test.util.Calculator;
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

        final int total = scores.size();

        scores.forEach(s -> {
            totalCorrect += s.getCorrectAnswer();
            totalWrong += s.getWrongAnswer();
            totalSolved += s.getCorrectAnswer() + s.getWrongAnswer();

            final String correct = String.format("%d%s", s.getCorrectAnswer(), COUNT);
            final String wrong = String.format("%d%s", s.getWrongAnswer(), COUNT);
            final String solved = String.format("%d%s%d%s", total, IN_PROBLEM, this.totalSolved, OF_PROBLEM);

            switch (s.getSubject()) {
                case GRAMMER:
                    this.gramCorrectLabel.setText(correct);
                    this.gramWrongLabel.setText(wrong);
                    this.gramSolvedLabel.setText(solved);
                    break;
                case VOCABULARY:
                    this.vocaCorrectLabel.setText(correct);
                    this.vocaWrongLabel.setText(wrong);
                    this.vocaSolvedLabel.setText(solved);
                    break;
                case READ:
                    this.readCorrectLabel.setText(correct);
                    this.readWrongLabel.setText(wrong);
                    this.readSolvedLabel.setText(solved);
                    break;
                case LISTEN:
                    this.listenCorrectLabel.setText(correct);
                    this.listenWrongLabel.setText(wrong);
                    this.listenSolvedLabel.setText(solved);
                    break;
            }
        });

        totalCorrectLabel.setText(String.format("%d 개", totalCorrect));
        totalWrongLabel.setText(String.format("%d 개", totalWrong));
        totalSolvedLabel.setText(String.format("%d 문제", totalSolved));

        totalGradeLabel.setText(Calculator.calculateGrade(totalCorrect, totalSolved));
    }
}
