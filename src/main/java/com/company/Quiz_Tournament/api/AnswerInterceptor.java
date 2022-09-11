package com.company.Quiz_Tournament.api;

import java.util.ArrayList;
import java.util.List;

public class AnswerInterceptor {
    private List<Integer> answer = new ArrayList<>();

    public AnswerInterceptor() {}

    public AnswerInterceptor(List<Integer> answer) {
        this.answer = answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public List<Integer> getAnswer() {
        return answer;
    }
}
