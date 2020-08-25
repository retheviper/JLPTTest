package com.jlpt.retheviper.test.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.jlpt.retheviper.test.bean.Problem;
import com.jlpt.retheviper.test.constant.Subject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestManagementService {

    @Getter
    private static final TestManagementService Instance = new TestManagementService();

    private static final Map<Subject, List<Problem>> PROBLEMS = new HashMap<>() {
        {
            put(Subject.VOCABULARY, new ArrayList<>());
            put(Subject.GRAMMER, new ArrayList<>());
            put(Subject.READ, new ArrayList<>());
            put(Subject.LISTEN, new ArrayList<>());

        }
    };

    private static final Map<Subject, String> DATA_FILES = new HashMap<>() {
        {
            put(Subject.VOCABULARY, "DATA_N2V.dat");
            put(Subject.GRAMMER, "DATA_N2V.dat");
            put(Subject.READ, "DATA_N2V.dat");
            put(Subject.LISTEN, "DATA_N2V.dat");

        }
    };

    static {
        PROBLEMS.forEach((k, v) -> loadDataFromFile(k));
    }

    public boolean registProblem(final Problem problem) { // 문제 등록: 과목 내에 동일한 문제 번호와 하위 번호 조합이 있을 경우 false
        if (problem.getImgSource() == null) {
            problem.setImgSource("nah");
        }
        final Subject subject = problem.getSubject();
        final List<Problem> pList = getProblem(subject);
        if (pList.stream().anyMatch(
                p -> p.getPNumber().equals(problem.getPNumber()) && p.getSubNumber().equals(problem.getSubNumber()))) {
            return false;
        }
        pList.add(problem);
        saveDataToFile(pList, subject);
        return true;
    }

    public void modifyProblem(final Problem problem) { // 문제 수정
        if (problem.getImgSource() == null) {
            problem.setImgSource("nah");
        }
        final Subject subject = problem.getSubject();
        final List<Problem> pList = getProblem(subject);
        saveDataToFile(pList, subject);
    }

    public void removeProblem(final Problem problem) { // 문제 삭제
        final Subject subject = problem.getSubject();
        final List<Problem> pList = getProblem(subject);
        pList.removeIf(
                p -> p.getPNumber().equals(problem.getPNumber()) && p.getSubNumber().equals(problem.getSubNumber()));
        saveDataToFile(pList, subject);
    }

    public void copyFile(final File file) { // 파일 복사 메소드(mp3일 경우 mp3 폴더에, 이미지 파일일 경우 img 폴더에)
        final String fileName = file.getName();
        final String extension = Optional.ofNullable(fileName).filter(n -> n.contains("."))
                .map(n -> n.substring(fileName.lastIndexOf(".") + 1)).get();
        final Path target = Paths.get(extension.equals("mp3") || extension.equals("MP3") ? "./mp3" : "./img", fileName);

        try {
            Files.copy(file.toPath(), target, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void saveDataToFile(final List<Problem> problems, final Subject subject) { // 문제 정보 저장
        try (final ObjectOutputStream stream = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(Paths.get(DATA_FILES.get(subject)))))) {
            stream.writeObject(problems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadDataFromFile(final Subject subject) { // 문제 정보 불러오기
        final Path path = Paths.get(DATA_FILES.get(subject));
        if (Files.exists(path)) {
            try (final ObjectInputStream stream = new ObjectInputStream(
                    new BufferedInputStream(Files.newInputStream(path)))) {
                PROBLEMS.put(subject, (List<Problem>) stream.readObject());
            } catch (IOException | ClassNotFoundException e) {
            }
        } else {
            try {
                Files.createFile(path);
            } catch (IOException e) {
            }
        }
    }

    public List<Problem> getProblem(final Subject subject) {
        return PROBLEMS.get(subject);
    }
}
