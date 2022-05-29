package com.company.Quiz_Tournament.api.CompletedQuizzes;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import com.company.Quiz_Tournament.api.User.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "Completed_Quizzes")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"quizId", "userId"})
})
public class CompletedQuizzes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("id")
    private Quiz quiz;

    @Column(unique = true)
    private Timestamp completedAt;

    public CompletedQuizzes() {}

    public CompletedQuizzes(User user, Quiz quiz, Timestamp completedAt) {
        this.user = user;
        this.quiz = quiz;
        this.completedAt = completedAt;
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

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }
}
