package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "(\\w|\\d)+@\\w+\\.\\w+")
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String nickname;

    /**
     * persisted password
     */
    private String password;

    /**
     * Password to set
     */
    @Transient
    @Size(min = 1) //todo change
    private String newPassword;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Quiz> quizzes;

    @ManyToOne
    @JoinColumn(name = "imageID")
    private QuizImage image;

    private User() {}

    public User(String email, String nickname, String password, String newPassword) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.newPassword = newPassword;
        this.quizzes = Collections.emptyList();
        this.image = QuizImage.emptyQuizImage();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void setImage(QuizImage image) {
        this.image = image;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public QuizImage getImage() {
        return image == null ? QuizImage.emptyQuizImage() : image;
    }

    public static User emptyUser() {
        return new User("", "", "", "");
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && this.id.equals(((User) obj).id);
    }
}
