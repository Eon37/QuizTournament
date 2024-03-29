package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.Image.ImageService;
import com.company.Quiz_Tournament.api.Image.ImageType;
import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.constants.EmptyQuizConstants;
import com.company.Quiz_Tournament.utils.ContextUtils;
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

    private String image;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @ElementCollection
    @NotNull
    @Size(min = 2, max = CommonConstants.DEFAULT_INT_OPTIONS_SIZE)
    private List<String> options = new ArrayList<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

    @ElementCollection
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer = new ArrayList<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

    private Quiz() {}

    public Quiz(Long id, String title, String description, String text, String image, User user,
                 List<String> options, List<Integer> answer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
        this.image = image;
        this.user = user;
        this.options = options;
        this.answer = answer;
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

    public String getImage() {
        return image;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public static Quiz newEmptyQuiz(boolean fillEmptyOptions) {
        return builder()
                .title(EmptyQuizConstants.TITLE)
                .description(EmptyQuizConstants.DESCRIPTION)
                .text(EmptyQuizConstants.TEXT)
                .image(ImageService.getRandomDefaultImageUrl(ImageType.DEFAULT_QUIZ))
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
        private String image;
        private User user;
        private List<String> options = new ArrayList<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);
        private List<Integer> answer = new ArrayList<>(CommonConstants.DEFAULT_INT_OPTIONS_SIZE);
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

        public QuizBuilder image(@NotNull String image) {
            this.image = image;
            return this;
        }

        public QuizBuilder options(@NotNull List<String> options, boolean addEmptyOptions) {
            this.options = new ArrayList<>(options);
            this.isAddEmptyOptions = addEmptyOptions;
            return this;
        }

        public QuizBuilder answer(@NotNull List<Integer> answer) {
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
