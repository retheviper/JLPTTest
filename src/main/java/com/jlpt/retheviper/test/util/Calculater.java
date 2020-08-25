package com.jlpt.retheviper.test.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Calculater {

    public static String calculateGrade(final int totalCorrect, final int totalSolved) {
        if (totalCorrect != 0 && totalSolved != 0) {
            final int score = (totalCorrect / totalSolved) * 100;
            if (score >= 80) {
                return "A";
            } else if (score < 80 && score >= 60) {
                return "B";
            } else if (score < 60 && score >= 40) {
                return "C";
            } else if (score < 40 && score >= 20) {
                return "D";
            }
        }
        return "F";
    }
}
