package com.jlpt.retheviper.test.bean;

import com.jlpt.retheviper.test.constant.Subject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Problem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 804010131929169997L;

    /**
     * 등급
     */
    private int nlevel; // ���(N1~N5) ��, ����� N2 ����

    /**
     * 과목
     */
    private Subject subject;

    /**
     * 문제 번호
     */
    private String pNumber;

    /**
     * 문제 하위 번호
     */
    private String subNumber; // ���� ���� ��ȣ

    /**
     * 지문
     */
    private String passage; // ���� ����

    /**
     * 이미지 파일명
     */
    private String imgSource; // �̹��� ���� ���� ����

    /**
     * 1번 선택지
     */
    private String firstChoice; // 1�� ������

    /**
     * 2번 선택지
     */
    private String secondChoice; // 2�� ������

    /**
     * 3번 선택지
     */
    private String thirdChoice; // 3�� ������

    /**
     * 4번 선택지
     */
    private String forthChoice; // 4�� ������

    /**
     * 정답 번호
     */
    private int answer; // ����

    /**
     * 듣기 파일명
     */
    private String mp3Source;

}