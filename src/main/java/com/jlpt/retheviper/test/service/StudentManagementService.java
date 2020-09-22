package com.jlpt.retheviper.test.service;

import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.bean.Student;
import com.jlpt.retheviper.test.constant.Subject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentManagementService {

    @Getter
    private static final StudentManagementService Instance = new StudentManagementService();

    private static final Map<String, Student> STUDENT_DATA = new HashMap<>(); // 학습자 정보

    private static final Path PATH = Paths.get("StudentData.dat"); // 학습자 정보를 저장할 파일명

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String loginUser = ""; // 로그인한 유저 아이디를 기록

    @Getter
    @Setter
    private boolean login = false; // 로그인 여부

    @Getter
    @Setter
    private boolean admin = false; // 관리자 로그인 여부

    static {
        if (Files.exists(PATH)) {
            try (final ObjectInputStream stream = new ObjectInputStream(
                    new BufferedInputStream(Files.newInputStream(PATH)))) {
                @SuppressWarnings("unchecked") final Map<String, Student> loadedData = (HashMap<String, Student>) stream.readObject();
                loadedData.forEach(STUDENT_DATA::put);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.createFile(PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Student getStudentData() {
        return STUDENT_DATA.get(loginUser);
    }

    public boolean registerStudent(final Student student) { // 학습자 등록
        if (!STUDENT_DATA.containsKey(student.getId())) {
            final Score.ScoreBuilder builder = Score.builder().correctAnswer(0).wrongAnswer(0).skippedAnswer(0);
            final List<Score> defaultScore = Arrays.asList(
                    builder.subject(Subject.VOCABULARY).build(),
                    builder.subject(Subject.GRAMMER).build(),
                    builder.subject(Subject.READ).build(),
                    builder.subject(Subject.LISTEN).build());
            student.setScores(defaultScore);
            STUDENT_DATA.put(student.getId(), student);
            saveDataToFile();
            return true;
        } else {
            return false;
        }
    }

    public boolean removeStudent(final String id, final String password) { // 학습자 삭제
        if (login(id, password)) {
            STUDENT_DATA.remove(id);
            saveDataToFile();
            return true;
        }
        return false;
    }

    public boolean login(final String id, final String password) { // 로그인 체크
        if (STUDENT_DATA.containsKey(id) && STUDENT_DATA.get(id).getPassword().equals(password)) {
            setLoginUser(id);
            this.login = true;
            return true;
        }
        return false;
    }

    public void recordScore(final Score score) { // 시험 당 점수 기록
        final Student student = STUDENT_DATA.get(loginUser);
        final List<Score> scores = student.getScores();
        for (Score oldScore : scores) {
            if (oldScore.getSubject().equals(score.getSubject())) {
                scores.set(scores.indexOf(oldScore), score);
                break;
            }
        }
        STUDENT_DATA.put(loginUser, student);
        saveDataToFile();
    }

    private void saveDataToFile() { // 학습자 정보를 저장
        try (final ObjectOutputStream stream = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(PATH)))) {
            stream.writeObject(STUDENT_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
