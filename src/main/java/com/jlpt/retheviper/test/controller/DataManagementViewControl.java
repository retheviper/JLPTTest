package com.jlpt.retheviper.test.controller;

import com.jlpt.retheviper.test.bean.Problem;
import com.jlpt.retheviper.test.bean.Problem.ProblemBuilder;
import com.jlpt.retheviper.test.constant.Subject;
import com.jlpt.retheviper.test.gui.DataManagementStage;
import com.jlpt.retheviper.test.service.TestManagementService;
import com.jlpt.retheviper.test.util.CreateAlert;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class DataManagementViewControl implements Initializable {
    // 데이터 관리 창 컨트롤러

    @FXML
    private final ObservableList<String> subjects = FXCollections.observableArrayList(Subject.VOCABULARY.getValue(),
            Subject.GRAMMER.getValue(), Subject.READ.getValue(), Subject.LISTEN.getValue()); // 과목
    @FXML
    private final ObservableList<String> answers = FXCollections.observableArrayList("1번", "2번", "3번", "4번"); // 정답 목록
    private final TestManagementService service = TestManagementService.getInstance();
    private FileChooser fileChooser;
    private String imgSource = null; // 이미지 파일 이름
    private String mp3Source = null; // mp3 파일 이름
    private File imgFile; // 이미지 파일 처리용
    private File mp3File; // mp3 파일 처리용
    @FXML
    private ComboBox<String> subject_left, subject_right, answer_right;
    @FXML
    private CheckBox imgIncluded_check;
    @FXML
    private Button chooseImgSourceButton, chooseMp3Button, registButton, modifyButton, removeButton, endButton;
    @FXML
    private TextField number_right, subNumber_right, firstChoice_right, secondChoice_right, thirdChoice_right,
            forthChoice_right;
    @FXML
    private TextArea passage_right;
    @FXML
    private TableView<Problem> table_left;
    @FXML
    private TableColumn<Problem, String> number_left, subNumber_left, passage_left;
    @FXML
    private ObservableList<Problem> problemList; // 테이블에 담을 임시 문제 목록

    private void setRightTableValue() { // 왼쪽 과목 콤보박스 선택시 테이블 변화
        final Subject subject = Subject.getTypeByValues(this.subject_left.getValue());
        this.problemList = FXCollections.observableArrayList(this.service.getProblem(subject));
        resetLeftTable();
    }

    private void refreshBothTable() { // 입력, 수정, 삭제시 테이블 변화
        this.subject_left.getSelectionModel().select(this.subject_right.getValue());
        setRightTableValue();
    }

    private void resetLeftTable() { // 테이블 초기화
        this.number_left.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPNumber()));
        this.subNumber_left.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSubNumber()));
        this.passage_left.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPassage()));
        this.table_left.setItems(this.problemList);
    }

    private void showProblemDetails(final Number newValue) { // 테이블 항목 선택 시 입력창에 반영
        if (this.table_left.getSelectionModel().getSelectedIndex() != -1) {
            if (this.subject_right.getSelectionModel().getSelectedItem() != this.subject_left.getSelectionModel()
                    .getSelectedItem()) {
                this.subject_right.getSelectionModel()
                        .select(this.table_left.getSelectionModel().getSelectedItem().getSubject().getValue());
            }
            this.number_right.setText(this.table_left.getSelectionModel().getSelectedItem().getPNumber());
            this.subNumber_right.setText(this.table_left.getSelectionModel().getSelectedItem().getSubNumber());
            this.passage_right.setText(this.table_left.getSelectionModel().getSelectedItem().getPassage());
            this.firstChoice_right.setText(this.table_left.getSelectionModel().getSelectedItem().getFirstChoice());
            this.secondChoice_right.setText(this.table_left.getSelectionModel().getSelectedItem().getSecondChoice());
            this.thirdChoice_right.setText(this.table_left.getSelectionModel().getSelectedItem().getThirdChoice());
            this.forthChoice_right.setText(this.table_left.getSelectionModel().getSelectedItem().getForthChoice());
            this.answer_right.getSelectionModel()
                    .select(this.table_left.getSelectionModel().getSelectedItem().getAnswer() - 1);

            if (this.table_left.getSelectionModel().getSelectedItem().getImgSource() != null
                    && !this.table_left.getSelectionModel().getSelectedItem().getImgSource().equals("nah")) { // 이미지 파일이 있을 경우 파일 첨부
                // 버튼에 반영
                this.imgIncluded_check.setSelected(true);
                this.chooseImgSourceButton.setDisable(false);
                this.chooseImgSourceButton.setText(table_left.getSelectionModel().getSelectedItem().getImgSource());
                this.imgFile = new File(table_left.getSelectionModel().getSelectedItem().getImgSource());
            } else {
                this.imgIncluded_check.setSelected(false);
                this.chooseImgSourceButton.setDisable(true);
                this.chooseImgSourceButton.setText("이미지 소스 선택");
            }

            if (this.table_left.getSelectionModel().getSelectedItem().getSubject().equals(Subject.LISTEN)) { // 청해의 경우 mp3를 첨부 버튼에 반영
                final Problem listen = this.table_left.getSelectionModel().getSelectedItem();
                if (listen.getMp3Source() != null) {
                    chooseMp3Button.setDisable(false);
                    chooseMp3Button.setText(listen.getMp3Source());
                    this.mp3File = new File(listen.getMp3Source());
                } else {
                    chooseMp3Button.setDisable(true);
                    chooseMp3Button.setText("MP3 소스 선택");
                }
            } else {
                chooseMp3Button.setDisable(true);
                chooseMp3Button.setText("MP3 소스 선택");
                this.mp3File = new File("");
            }
        }
    }

    private void clearInputForm() { // 입력창 초기화
        this.subject_right.getSelectionModel().clearSelection();
        this.number_right.setText("");
        this.subNumber_right.setText("");
        this.passage_right.setText("");
        this.firstChoice_right.setText("");
        this.secondChoice_right.setText("");
        this.thirdChoice_right.setText("");
        this.forthChoice_right.setText("");
        this.answer_right.getSelectionModel().clearSelection();
        this.imgIncluded_check.setSelected(false);
        this.chooseImgSourceButton.setDisable(true);
        this.chooseImgSourceButton.setText("이미지 소스 선택");
        this.chooseMp3Button.setDisable(true);
        this.chooseMp3Button.setText("MP3 소스 선택");
    }

    private void toListenTest() { // 청해 과목 선택시 mp3 버튼 활성/비활성화
        if (this.subject_right.getSelectionModel().isSelected(3)) {
            this.chooseMp3Button.setDisable(false);
        } else if (!this.chooseMp3Button.isDisable()) {
            this.chooseMp3Button.setDisable(true);
        }
    }

    private void registerProblem() { // 문제 등록
        if (checkForm()) {
            checkAudioIncluded();
            if (this.service.registProblem(getInputedProblem())) {
                refreshBothTable();
                CreateAlert.withoutHeader(AlertType.INFORMATION, "성공", "문제가 정상적으로 등록되었습니다");
            } else {
                refreshBothTable();
                CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제 번호가 중복되었습니다");
            }
        }
    }

    private boolean checkForm() { // 입력창 항목이 전부 채워졌는지 확인
        final String pattern = "([0-9]{1,2})";
        if (this.subject_right.getValue() == null || this.number_right.getText().equals("")
                || this.subNumber_right.getText().equals("") || this.passage_right.getText().equals("")
                || this.firstChoice_right.getText().equals("") || this.secondChoice_right.getText().equals("")
                || this.thirdChoice_right.getText().equals("") || this.forthChoice_right.getText().equals("")) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "필수 입력 항목에 빈 곳이 있습니다");
            return false;
        } else if (!Pattern.matches(pattern, this.number_right.getText())
                || !Pattern.matches(pattern, this.subNumber_right.getText())) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "문제 번호와 문제 하위 번호는 두 자리 숫자로만 입력해 주십시오");
            return false;
        }
        return true;
    }

    private boolean checkTableItemSelected() { // 테이블에서 항목이 선택되었는지 확인
        if (this.table_left.getSelectionModel().isEmpty()) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "대상을 테이블에서 선택해야 합니다");
            return false;
        }
        return true;
    }

    private int convertAnswerToInt(final String answer) {
        return Integer.parseInt(answer.substring(0, answer.length() - 1));
    }

    private void modifyProblem() { // 수정 버튼 클릭 시 테이블에서 대상을 선택하고 내용을 덮어씀
        if (checkForm() && checkTableItemSelected()) {
            checkAudioIncluded();
            service.modifyProblem(getInputedProblem());
            refreshBothTable();
            clearInputForm();
            CreateAlert.withoutHeader(AlertType.INFORMATION, "성공", "수정 내용이 반영되었습니다");
        }
    }

    private void checkAudioIncluded() {
        if (subject_right.getValue().equals(Subject.LISTEN.getValue()) && this.mp3File == null) {
            CreateAlert.withoutHeader(AlertType.ERROR, "오류", "청해 문제는 mp3 파일명까지 선택해야 합니다");
        }
    }

    private Problem getInputedProblem() {
        final ProblemBuilder builder = Problem.builder().subject(Subject.getTypeByValues(this.subject_right.getValue()))
                .pNumber(this.number_right.getText())
                .subNumber(this.subNumber_right.getText())
                .passage(this.passage_right.getText())
                .imgSource(this.imgSource)
                .firstChoice(this.firstChoice_right.getText())
                .secondChoice(this.secondChoice_right.getText())
                .thirdChoice(this.thirdChoice_right.getText())
                .forthChoice(this.forthChoice_right.getText())
                .answer(convertAnswerToInt(this.answer_right.getValue()));
        if (this.mp3File != null) {
            builder.mp3Source(this.mp3File.getName());
        }
        return builder.build();
    }

    private void removeProblem() { // 삭제 버튼을 누르면 문제를 테이블에서 삭제(과목, 문제 번호, 문제 하위 번호로 특정)
        if (checkTableItemSelected()) {
            this.service.removeProblem(this.table_left.getSelectionModel().getSelectedItem());
            this.problemList.remove(this.table_left.getSelectionModel().getSelectedIndex());
            this.table_left.setItems(this.problemList);
            clearInputForm();
            CreateAlert.withoutHeader(AlertType.INFORMATION, "성공", "문제가 정상적으로 삭제되었습니다");
        }
    }

    private void setChooseImgSourceButton() { // 이미지 포함 여부 체크박스 선택시 파일 경로 버튼 활성화
        if (this.chooseImgSourceButton.isDisable()) {
            this.chooseImgSourceButton.setDisable(false);
        } else {
            this.chooseImgSourceButton.setDisable(true);
            this.chooseImgSourceButton.setText("이미지 소스 선택");
            this.imgFile = new File("");
        }
    }

    private void chooseImageFile() { // 첨부할 이미지 파일을 선택시 img 폴더로 복사
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("이미지 소스 파일 선택");
        this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        if ((this.imgFile = this.fileChooser.showOpenDialog(DataManagementStage.getStage())) != null) {
            this.imgSource = this.imgFile.getName();
            this.chooseImgSourceButton.setText(this.imgSource);
            this.service.copyFile(this.imgFile);
        }
    }

    private void chooseMp3File() { // 첨부할 mp3 파일을 선택시 mp3 폴더로 복사
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("mp3 소스 파일 선택");
        this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        if ((this.mp3File = this.fileChooser.showOpenDialog(DataManagementStage.getStage())) != null) {
            this.mp3Source = this.mp3File.getName();
            this.chooseMp3Button.setText(this.mp3Source);
            this.service.copyFile(this.mp3File);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { // 컴포넌트 이벤트 처리
        this.subject_left.setItems(this.subjects);
        this.subject_right.setItems(this.subjects);
        this.answer_right.setItems(this.answers);
        this.table_left.getSelectionModel().selectedIndexProperty()
                .addListener((problem, oldValue, newValue) -> showProblemDetails(newValue));
        this.subject_right.setOnAction(event -> toListenTest());
        this.subject_left.setOnAction(event -> setRightTableValue());
        this.imgIncluded_check.setOnAction(event -> setChooseImgSourceButton());
        this.chooseImgSourceButton.setOnAction(event -> chooseImageFile());
        this.chooseMp3Button.setOnAction(event -> chooseMp3File());
        this.registButton.setOnAction(event -> registerProblem());
        this.modifyButton.setOnAction(event -> modifyProblem());
        this.removeButton.setOnAction(event -> removeProblem());
        this.endButton.setOnAction(event -> DataManagementStage.getStage().hide());
    }

}
