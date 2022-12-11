package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.Quiz.Quiz;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Quiz> quizzes = new ArrayList<>();

    private String image;

    private User() {}

    public User(Long id, String email, String nickname, String password, List<Quiz> quizzes, String image) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.quizzes = new ArrayList<>(quizzes == null? Collections.emptyList() : quizzes);
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

    public String getImage() {
        return image;
    }

    public static User newEmptyUser() {
        return builder().email("").nickname("").password("").build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static UserBuilder builder(User other) {
        return new UserBuilder(other);
    }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String nickname;
        private String password;
        private List<Quiz> quizzes = new ArrayList<>();
        private String imgUrl;

        private UserBuilder() {}

        private UserBuilder(User other) {
            id = other.getId();
            email = other.getEmail();
            nickname = other.getNickname();
            password = other.getPassword();
            quizzes = other.getQuizzes();
        }

        public UserBuilder id(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(@Pattern(regexp = "(\\w|\\d)+@\\w+\\.\\w+") String email) {
            this.email = email;
            return this;
        }

        public UserBuilder nickname(@NotBlank String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserBuilder password(@Size(min = 8) String password) {
            this.password = password;
            return this;
        }

        public UserBuilder quizzes(@NotNull List<Quiz> quizzes) {
            this.quizzes = new ArrayList<>(quizzes);
            return this;
        }

        public UserBuilder image(@NotNull String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public User build() {
            return new User(id, email, nickname, password, quizzes, imgUrl);
        }
    }
}
