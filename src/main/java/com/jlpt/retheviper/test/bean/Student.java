package com.jlpt.retheviper.test.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Student implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1041155330353381585L;

    /**
     * ID
     */
    private String id;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 과목별 성적
     */
    private List<Score> scores;
}