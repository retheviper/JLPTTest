package com.jlpt.retheviper.test.bean;

import java.io.Serializable;

import com.jlpt.retheviper.test.constant.Subject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Score implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8775407648591275909L;

	/** 과목 */
	private Subject subject; // 과목

	/** 정답 수 */
	private int correctAnswer; // 정답 수

	/** 오답 수 */
	private int wrongAnswer; // 오답 수

	/** 풀지 않은 문제 수 */
	private int skippedAnswer;
}
