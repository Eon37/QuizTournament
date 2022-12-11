package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.AnswerInterceptor;
import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzes;
import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzesService;
import com.company.Quiz_Tournament.api.FeedBack;
import com.company.Quiz_Tournament.api.Image.ImageService;
import com.company.Quiz_Tournament.api.Image.ImageType;
import com.company.Quiz_Tournament.constants.EmptyQuizConstants;
import com.company.Quiz_Tournament.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import com.company.Quiz_Tournament.api.User.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CompletedQuizzesService completedQuizzesService;

    @Autowired
    private ImageService imageService;

    public Quiz save(Quiz quiz, MultipartFile image) throws IOException {
        User user = ContextUtils.getCurrentUserOrThrow();

        return quiz.getId() == null ? createQuiz(quiz, image, user) : updateQuiz(quiz, image, user);
    }

    private Quiz createQuiz(Quiz quiz, MultipartFile image, User user) throws IOException {
        List<String> options = prepareOptions(quiz.getOptions());
        List<Integer> answer = filteredAnswer(quiz.getAnswer(), options.size());
        String quizImage = image.isEmpty()
                ? ImageService.getRandomDefaultImageUrl(ImageType.DEFAULT_QUIZ)
                : imageService.save(image, ImageType.QUIZ);

        Quiz quizToSave = Quiz.builder()
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .text(quiz.getText())
                .image(quizImage)
                .user(user)
                .options(options, false)
                .answer(answer)
                .build();

        quizToSave = quizRepository.save(quizToSave);

        //Add quiz with new id
        user.getQuizzes().add(quizToSave);

        return quizToSave;
    }

    private Quiz updateQuiz(Quiz quiz, MultipartFile image, User user) throws IOException {
        Quiz persistedQuiz = getById(quiz.getId());

        if (!user.getEmail().equals(persistedQuiz.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no rights to update this quiz");
        }

        String title = StringUtils.isEmptyOrWhitespace(quiz.getTitle())
                ? persistedQuiz.getTitle()
                : quiz.getTitle();
        String text = StringUtils.isEmptyOrWhitespace(quiz.getText())
                ? persistedQuiz.getText()
                : quiz.getText();
        String quizImage = image.isEmpty()
                ? persistedQuiz.getImage()
                : imageService.save(image, ImageType.QUIZ);
        List<String> options = prepareOptions(quiz.getOptions());
        List<Integer> answer = filteredAnswer(quiz.getAnswer(), options.size());

        Quiz quizToSave = Quiz.builder()
                .id(quiz.getId())
                .title(title)
                .description(quiz.getDescription())
                .text(text)
                .image(quizImage)
                .user(user)
                .options(options, false)
                .answer(answer)
                .build();

        return quizRepository.save(quizToSave);
    }

    private List<String> prepareOptions(List<String> options) {
        //remove default values and duplicates
        List<String> prepared = filterOptions(options);

        //validate remaining
        validateOptions(prepared);

        return prepared;
    }

    private void validateOptions(List<String> options) {
        if (options.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There should be more than one unique option");
        }
    }

    private List<String> filterOptions(List<String> options) {
        return options.stream()
                .filter(op -> !StringUtils.isEmptyOrWhitespace(op))
                .filter(op -> !EmptyQuizConstants.OPTION.equals(op))
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Integer> filteredAnswer(List<Integer> answer, int optionsSize) {
        answer.removeIf(a -> a >= optionsSize);
        return answer;
    }

    public FeedBack solve(Quiz quiz, AnswerInterceptor answer) {
        if (!CollectionUtils.isEqualCollection(answer.getAnswer(), quiz.getAnswer())) {
            return new FeedBack(false);
        }

        if (ContextUtils.isCurrentUserAuthenticated()) {
            User user = ContextUtils.getCurrentUserOrThrow();

            if (!completedQuizzesService.isAlreadySolved(quiz, user)) {
                CompletedQuizzes cquizzes = new CompletedQuizzes(user, quiz, new Timestamp(System.currentTimeMillis()));
                completedQuizzesService.save(cquizzes);
            }
        }

        return new FeedBack(true);
    }

    public Page<Quiz> getAll(int pageNo, int pageSize) {
        return quizRepository.findAll(PageRequest.of(pageNo, pageSize));
    }

    public Page<CompletedQuizzes> getCompleted(int pageNo, int pageSize) {
        return completedQuizzesService.getByUser(
                ContextUtils.getCurrentUserOrThrow(),
                PageRequest.of(pageNo, pageSize, Sort.by("completedAt").descending())
        );
    }

    public Quiz getById(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    public void delete(Long id) {
        Quiz quiz = getById(id);

        String currentUserEmail = ContextUtils.getCurrentUserOrThrow().getEmail();
        if (!currentUserEmail.equals(quiz.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no rights to delete this quiz");
        }

        quizRepository.delete(quiz);
    }

    public Long count() {
        return quizRepository.count();
    }
}
