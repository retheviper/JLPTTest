package com.jlpt.retheviper.test.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jlpt.retheviper.test.bean.Problem;
import com.jlpt.retheviper.test.constant.Subject;

import lombok.Getter;

@Getter
public class TestManagementService {

	private static final TestManagementService INSTANCE = new TestManagementService();

	private List<Problem> vList = new ArrayList<>(); // 문자어휘 문제 목록

	private List<Problem> gList = new ArrayList<>(); // 문법 문제 목록

	private List<Problem> rList = new ArrayList<>(); // 독해 문제 목록

	private List<Problem> lList = new ArrayList<>(); // 청해 문제 목록

	private final File n2vFile = new File("DATA_N2V.dat"); // 문자어휘 저장 파일

	private final File n2gFile = new File("DATA_N2G.dat"); // 문법 저장 파일

	private final File n2rFile = new File("DATA_N2R.dat"); // 독해 저장 파일

	private final File n2lFile = new File("DATA_N2L.dat"); // 청해 저장 파일

	private TestManagementService() {
		loadDataFromFile();
	}

	public static TestManagementService getInstance() {
		return INSTANCE;
	}

	public boolean registProblem(final Problem problem) { // 문제 등록: 과목 내에 동일한 문제 번호와 하위 번호 조합이 있을 경우 false
		if (problem.getImgSource() == null) {
			problem.setImgSource("nah");
		}

		final List<Problem> pList = selectProblemType(problem.getSubject());
		if (pList.stream().anyMatch(
				p -> p.getPNumber().equals(problem.getPNumber()) && p.getSubNumber().equals(problem.getSubNumber()))) {
			return false;
		}
		pList.add(problem);
		saveDataToFile(pList, selectDataFile(problem.getSubject(), problem.getNlevel()));
		return true;
	}

	public void modifyProblem(final Problem problem) { // 문제 수정
		if (problem.getImgSource() == null) {
			problem.setImgSource("nah");
		}

		final List<Problem> pList = selectProblemType(problem.getSubject());
		// TODO

		saveDataToFile(pList, selectDataFile(problem.getSubject(), problem.getNlevel()));
	}

	public void removeProblem(final Problem problem) { // 문제 삭제
		final List<Problem> pList = selectProblemType(problem.getSubject());
		pList.removeIf(
				p -> p.getPNumber().equals(problem.getPNumber()) && p.getSubNumber().equals(problem.getSubNumber()));
		saveDataToFile(pList, selectDataFile(problem.getSubject(), problem.getNlevel()));
	}

	public void copyFile(final File file) { // 파일 복사 메소드(mp3일 경우 mp3 폴더에, 이미지 파일일 경우 img 폴더에)
		final String fileName = file.getName();
		final String extension = Optional.ofNullable(fileName).filter(n -> n.contains(".")).map(n -> n.substring(fileName.lastIndexOf(".") + 1)).get();
		final Path target = Paths.get(extension.equals("mp3") || extension.equals("MP3") ? "./mp3" : "./img", fileName);

		try {
			Files.copy(file.toPath(), target, StandardCopyOption.COPY_ATTRIBUTES);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void saveDataToFile(final List<Problem> problems, final String fileName) { // 문제 정보 저장

	 // TODO

		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(n2lFile)));
			oos.writeObject(this.lList);
			oos.close();
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(n2gFile)));
			oos.writeObject(this.gList);
			oos.close();
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(n2rFile)));
			oos.writeObject(this.rList);
			oos.close();
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(n2vFile)));
			oos.writeObject(this.vList);
		} catch (Exception e) {
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void loadDataFromFile() { // 문제 정보 불러오기
		ObjectInputStream ois = null;
		if (n2lFile.exists() && n2gFile.exists() && n2rFile.exists() && n2vFile.exists()) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(n2lFile)));
				this.lList = (List<Problem>) ois.readObject();
				ois.close();
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(n2gFile)));
				this.gList = (List<Problem>) ois.readObject();
				ois.close();
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(n2rFile)));
				this.rList = (List<Problem>) ois.readObject();
				ois.close();
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(n2vFile)));
				this.vList = (List<Problem>) ois.readObject();
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
				n2lFile.createNewFile();
				n2gFile.createNewFile();
				n2rFile.createNewFile();
				n2vFile.createNewFile();
			} catch (Exception e) {
			}
		}
	}

	public List<Problem> selectProblemType(final Subject subject) {
		switch (subject) {
		case VOCABULARY:
			return getVList();
		case GRAMMER:
			return getGList();
		case READ:
			return getRList();
		case LISTEN:
			return getLList();
		default:
			return null;
		}
	}

	public String selectDataFile(final Subject subject, final int nLevel) {
		switch (subject) {
		case VOCABULARY:
			return "DATA_N2V.dat";
		case GRAMMER:
			return "DATA_N2G.dat";
		case READ:
			return "DATA_N2R.dat";
		case LISTEN:
			return "DATA_N2L.dat";
		default:
			return null;
		}
	}
}
