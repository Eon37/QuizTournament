package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzes;
import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzesService;
import com.company.Quiz_Tournament.api.FeedBack;
import com.company.Quiz_Tournament.api.QuizImage.ImageType;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.api.QuizImage.QuizImageService;
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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CompletedQuizzesService completedQuizzesService;

    @Autowired
    private QuizImageService quizImageService;

    public Quiz save(Quiz quiz, MultipartFile image) throws IOException {
        User user = ContextUtils.getCurrentUserOrThrow();

        if (quiz.getId() == null) {
            quiz.setUser(user);

            quiz.setImage(image.isEmpty()
                    ? quizImageService.save(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ))
                    : quizImageService.save(new QuizImage(image.getBytes(), image.getContentType(), ImageType.QUIZ)));

            Collection<String> options = prepareOptions(quiz.getOptions());
            quiz.setOptions(options);

            quiz.setAnswer(filteredAnswer(quiz.getAnswer(), options.size()));

            quiz = quizRepository.save(quiz);

            user.getQuizzes().add(quiz);

            return quiz;
        }

        Quiz persistedQuiz = getById(quiz.getId());

        if (!user.getEmail().equals(persistedQuiz.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no rights to update this quiz");
        }

        quiz.setUser(user);

        //Title and text are required, should not be blank. Description can be blank.
        if (StringUtils.isEmptyOrWhitespace(quiz.getTitle())) {
            quiz.setTitle(persistedQuiz.getTitle());
        }

        if (StringUtils.isEmptyOrWhitespace(quiz.getText())) {
            quiz.setText(persistedQuiz.getText());
        }

        quiz.setImage(image.isEmpty()
                ? persistedQuiz.getImage()
                : quizImageService.save(new QuizImage(image.getBytes(), image.getContentType(), ImageType.QUIZ)));

        Collection<String> options = prepareOptions(quiz.getOptions());
        quiz.setOptions(CollectionUtils.isEmpty(options) ? persistedQuiz.getOptions() : options);

        quiz.setAnswer(filteredAnswer(quiz.getAnswer(), options.size()));

        return quizRepository.save(quiz);
    }

    private Collection<String> prepareOptions(Collection<String> options) {
        Collection<String> prepared = filterOptions(options);
        validateOptions(prepared);

        return prepared;
    }

    private void validateOptions(Collection<String> options) {
        if (options.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There should be more than one unique option");
        }
    }

    private Collection<String> filterOptions(Collection<String> options) {
        return options.stream()
                .filter(op -> !StringUtils.isEmptyOrWhitespace(op))
                .filter(op -> !EmptyQuizConstants.OPTION.equals(op))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Collection<Integer> filteredAnswer(Collection<Integer> answer, int optionsSize) {
        answer.removeIf(a -> a >= optionsSize);
        return answer;
    }

    public FeedBack solve(Quiz answer, Long id) {
        Quiz quiz = getById(id);

        if (!CollectionUtils.isEqualCollection(answer.getAnswerInterceptor(), quiz.getAnswer())) {
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

        if (quiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

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
