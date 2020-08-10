package com.jlpt.retheviper.test.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jlpt.retheviper.test.bean.Score;
import com.jlpt.retheviper.test.bean.Student;
import com.jlpt.retheviper.test.constant.Subject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unchecked")
public class StudentManagementService {

	private static final StudentManagementService INSTANCE = new StudentManagementService();

	private static Map<String, Student> STUDENT_DATA = new HashMap<>(); // 학습자 정보

	private final static File file = new File("StudentData.dat"); // 학습자 정보를 저장할 파일명

	@Getter
	@Setter(AccessLevel.PRIVATE)
	private String loginedUser = ""; // 로그인한 유저 아이디를 기록

	@Getter
	@Setter
	private boolean logined = false; // 로그인 여부

	@Getter
	@Setter
	private boolean admin = false; // 관리자 로그인 여부

	static {
		ObjectInputStream ois = null;
		if (file.exists()) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				final Map<String, Student> loadedData = (HashMap<String, Student>) ois.readObject();
				loadedData.forEach((k, v) -> {
					STUDENT_DATA.put(k, v);
				});
			} catch (Exception e) {
			} finally {
				try {
					if (ois != null) {
						ois.close();
					}
				} catch (Exception e2) {
				}
			}
		} else {
			try {
				file.createNewFile();
			} catch (Exception e) {
			}
		}
	}

	private StudentManagementService() {
	}

	public static StudentManagementService getInstance() {
		return INSTANCE;
	}

	public Map<String, Student> getStudentData() {
		return Collections.unmodifiableMap(STUDENT_DATA);
	}

	public boolean registStudent(final Student student) { // 학습자 등록
		final boolean regiseted = STUDENT_DATA.containsKey(student.getId());
		if (!regiseted) {
			final List<Score> defaultScore = Arrays.asList(
					Score.builder().subject(Subject.VOCABULARY).correctAnswer(0).wrongAnswer(0).skippedAnswer(0).build(),
					Score.builder().subject(Subject.GRAMMER).correctAnswer(0).wrongAnswer(0).skippedAnswer(0).build(),
					Score.builder().subject(Subject.READ).correctAnswer(0).wrongAnswer(0).skippedAnswer(0).build(),
					Score.builder().subject(Subject.LISTEN).correctAnswer(0).wrongAnswer(0).skippedAnswer(0).build());

			student.setScores(defaultScore);
			STUDENT_DATA.put(student.getId(), student);
			saveDataToFile();
		}
		return !regiseted;
	}

	public boolean removeStudent(final String id, final String password) { // 학습자 삭제
		final boolean check;
		if (login(id, password)) {
			STUDENT_DATA.remove(id);
			saveDataToFile();
			check = true;
		} else {
			check = false;
		}
		return check;
	}

	public boolean login(final String id, final String password) { // 로그인 체크
		if (STUDENT_DATA.containsKey(id) && STUDENT_DATA.get(id).getPassword().equals(password)) {
			setLoginedUser(id);
			this.logined = true;
			return true;
		} else {
			return false;
		}
	}

	public void recordScore(final Score score) { // 시험 당 점수 기록
		saveDataToFile();
	}

	// TODO Files
	private void saveDataToFile() { // 학습자 정보를 저장
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(STUDENT_DATA);
		} catch (Exception e) {
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (Exception e2) {
			}
		}
	}
}
