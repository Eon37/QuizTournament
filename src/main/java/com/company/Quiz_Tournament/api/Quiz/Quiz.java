package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.constants.EmptyQuizConstants;
import com.company.Quiz_Tournament.utils.ContextUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity(name = "Quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String text;

    @ManyToOne
    @JoinColumn(name = "imageID")
    private QuizImage image = QuizImage.emptyQuizImage();

    @ManyToOne
    @JoinColumn(name = "UserID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @ElementCollection
    @NotNull
    @Size(min = 2, max = CommonConstants.DEFAULT_INT_OPTIONS_SIZE)
    private Collection<String> options = new LinkedHashSet<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

    @ElementCollection
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<Integer> answer = new HashSet<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

    @JsonIgnore
    @Transient
    private Collection<Integer> answerInterceptor = new HashSet<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

    public Quiz() {}

    private Quiz(Long id, String title, String description, String text, QuizImage image, User user,
                 Collection<String> options, Collection<Integer> answer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
        this.image = image;
        this.user = user;
        this.options = options;
        this.answer = answer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setImage(QuizImage image) {
        this.image = image;
    }

    public void setOptions(Collection<String> options) {
        this.options = options;
    }

    public void setAnswer(Collection<Integer> answer) {
        this.answer = answer == null ? new HashSet<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE) : answer;
    }

    public void setAnswerInterceptor(Collection<Integer> answerInterceptor) {
        this.answerInterceptor = answerInterceptor;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public QuizImage getImage() {
        return image;
    }

    public Collection<String> getOptions() {
        return options;
    }

    public Collection<Integer> getAnswer() {
        return answer;
    }

    public Collection<Integer> getAnswerInterceptor() {
        return answerInterceptor;
    }

    public static Quiz newEmptyQuiz(boolean fillEmptyOptions) {
        return builder()
                .title(EmptyQuizConstants.TITLE)
                .description(EmptyQuizConstants.DESCRIPTION)
                .text(EmptyQuizConstants.TEXT)
                .image(QuizImage.emptyQuizImage())
                .user(ContextUtils.getCurrentUserOrThrow())
                .options(new ArrayList<>(), fillEmptyOptions)
                .answer(Collections.emptyList())
                .build();
    }

    /**
     * Adds empty options up to default options size
     * @return this quiz
     */
    public Quiz addEmptyOptions() {
        options = new ArrayList<>(options);

        for (int i = options.size(); i < CommonConstants.DEFAULT_INT_OPTIONS_SIZE; i++) {
            options.add(EmptyQuizConstants.OPTION);
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        return Objects.equals(title, quiz.title)
                && Objects.equals(description, quiz.description)
                && Objects.equals(text, quiz.text)
                && Objects.equals(image, quiz.image)
                && Objects.equals(user, quiz.user)
                && Objects.equals(options, quiz.options)
                && Objects.equals(answer, quiz.answer);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (options != null ? options.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }

    public static QuizBuilder builder() {
        return new QuizBuilder();
    }

    public static QuizBuilder builder(@NotNull @Valid Quiz other) {
        return new QuizBuilder(other);
    }

    public static class QuizBuilder {
        private Long id;
        private String title;
        private String description;
        private String text;
        private QuizImage image = QuizImage.emptyQuizImage();
        private User user;
        private Collection<String> options = new LinkedHashSet<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);
        private Collection<Integer> answer = new HashSet<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);
        private boolean isAddEmptyOptions = true;

        private QuizBuilder() {}

        private QuizBuilder(Quiz other) {
            id = other.getId();
            title = other.getTitle();
            description = other.getDescription();
            text = other.getText();
            user = ContextUtils.getCurrentUserOrThrow();
            image = other.getImage();
            options = other.getOptions();
            answer = other.getAnswer();
        }

        public QuizBuilder id(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public QuizBuilder title(@NotBlank String title) {
            this.title = title;
            return this;
        }

        public QuizBuilder description(@NotBlank String description) {
            this.description = description;
            return this;
        }

        public QuizBuilder text(@NotBlank String text) {
            this.text = text;
            return this;
        }

        public QuizBuilder user(@NotNull @Valid User user) {
            this.user = user;
            return this;
        }

        public QuizBuilder image(@NotNull QuizImage image) {
            this.image = image;
            return this;
        }

        public QuizBuilder options(@NotNull Collection<String> options, boolean addEmptyOptions) {
            this.options = new ArrayList<>(options);
            this.isAddEmptyOptions = addEmptyOptions;
            return this;
        }

        public QuizBuilder answer(@NotNull Collection<Integer> answer) {
            this.answer = new ArrayList<>(answer);
            return this;
        }

        public Quiz build() {
            Quiz newQuiz = new Quiz(id, title, description, text, image, user, options, answer);

            return isAddEmptyOptions
                    ? newQuiz.addEmptyOptions()
                    : newQuiz;
        }
    }
}
