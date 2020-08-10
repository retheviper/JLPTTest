package com.jlpt.retheviper.test.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Subject {

	/** 언어지식(문자·어휘) */
	VOCABULARY("언어지식(문자·어휘)"),

	/** 언어지식(문법) */
	GRAMMER("언어지식(문법)"),

	/** 독해 */
	READ("독해"),

	/** 청해 */
	LISTEN("청해");

	private final String value;

	public static Subject getTypeByValues(final String value) {
		for (final Subject subject : values()) {
			if (subject.getValue().equals(value)) {
				return subject;
			}
		}
		return null;
	}
}
