package com.company.Quiz_Tournament.api.QuizComments;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import com.company.Quiz_Tournament.api.User.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity(name = "Quiz_Comment")
public class QuizComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Quiz quiz;

    @NotBlank
    private String text;

    private Timestamp postedAt;

    QuizComment() {}

    QuizComment(User user, Quiz quiz, String text, Timestamp postedAt) {
        this.quiz = quiz;
        this.user = user;
        this.text = text;
        this.postedAt = postedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostedAt(Timestamp postedAt) {
        this.postedAt = postedAt;
    }

    public Long getId() {
        return id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Timestamp getPostedAt() {
        return postedAt;
    }
}
