package com.company.Quiz_Tournament.api;

import java.util.ArrayList;
import java.util.List;

public class Answer {

    private List<String> answer;

    public Answer() {
        answer = new ArrayList<>();
    }

    public Answer(List<String> answer) {
        this.answer = new ArrayList<>(answer);
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer == null ? new ArrayList<>() : answer;
    }

    public List<String> getAnswer() {
        return answer;
    }

    @Override
    public int hashCode() {
        return answer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Answer && this.equals(obj);
    }
}
