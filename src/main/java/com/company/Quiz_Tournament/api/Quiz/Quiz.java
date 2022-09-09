package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.constants.EmptyQuizConstants;
import com.company.Quiz_Tournament.utils.ContextUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
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

    public static Quiz createQuiz(String title, String description, String text, User user,
                                  Collection<String> options, Collection<Integer> answer) {
        Quiz newQuiz =  new Quiz();
        newQuiz.title = title;
        newQuiz.description = description;
        newQuiz.text = text;
        newQuiz.user = user;
        newQuiz.options = options;
        newQuiz.answer = answer;

        return newQuiz;
    }

    public static Quiz newEmptyQuiz() {
        return createQuiz(EmptyQuizConstants.TITLE,
                EmptyQuizConstants.DESCRIPTION,
                EmptyQuizConstants.TEXT,
                ContextUtils.getCurrentUserOrThrow(),
                addEmptyOptions(Collections.emptyList(), CommonConstants.DEFAULT_INT_OPTIONS_SIZE),
                Collections.emptyList());
    }

    /**
     * Adds additional empty options to specified collection in order to be drawn on UI
     * @param initialOptions initial collection of options
     * @param size number of options to be added
     * @return collection of empty options
     */
    public static Collection<String> addEmptyOptions(Collection<String> initialOptions, int size) {
        Collection<String> options = new ArrayList<>(initialOptions);

        for (int i = initialOptions.size(); i < size; i++) {
            options.add(EmptyQuizConstants.OPTION);
        }

        return options;
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
}
