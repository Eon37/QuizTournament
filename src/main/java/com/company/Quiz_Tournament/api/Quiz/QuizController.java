package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzes;
import com.company.Quiz_Tournament.api.QuizComments.QuizCommentService;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.models.CreateQuizPageModel;
import com.company.Quiz_Tournament.models.HomePageModel;
import com.company.Quiz_Tournament.models.QuizPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;

@Controller
public class QuizController {

    private final QuizService quizService;
    private final QuizCommentService quizCommentService;

    @Autowired
    QuizController(QuizService quizService, QuizCommentService quizCommentService) {
        this.quizService = quizService;
        this.quizCommentService = quizCommentService;
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ModelAndView answerQuiz(@ModelAttribute(name = "quiz") Quiz quiz, @PathVariable Long id) {
        Quiz fullQuiz = quizService.getById(id);
        fullQuiz.setAnswerInterceptor(quiz.getAnswerInterceptor());

        return QuizPageModel.builder()
                .currentQuiz(fullQuiz)
                .feedback(quizService.solve(quiz, id))
                .commentsCount(quizCommentService.countById(fullQuiz))
                .build();
    }

    @GetMapping("/api/quizzes/create")
    public ModelAndView getCreateQuizPage() {
        return CreateQuizPageModel.builder()
                .quiz(Quiz.emptyQuiz())
                .build();
    }

    @PostMapping("/api/quizzes/update")
    public ModelAndView getCreateQuizPageForUpdate(@ModelAttribute("quiz") Quiz quiz) {
        Collection<String> options = Quiz.addEmptyOptions(quiz.getOptions(), CommonConstants.DEFAULT_INT_OPTIONS_SIZE - quiz.getOptions().size());
        quiz.setOptions(options);

        return CreateQuizPageModel.builder()
                .quiz(quiz)
                .build();
    }

    @PostMapping(path = "/api/quizzes")
    public ModelAndView createQuiz(@RequestPart("file") MultipartFile image,
                             @Valid @ModelAttribute(name="emptyQuiz") Quiz newQuiz) throws IOException {
        quizService.save(newQuiz, image);
        Collection<String> options = Quiz.addEmptyOptions(newQuiz.getOptions(), CommonConstants.DEFAULT_INT_OPTIONS_SIZE - newQuiz.getOptions().size());
        newQuiz.setOptions(options);

        return CreateQuizPageModel.builder()
                .quiz(newQuiz)
                .build();
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ModelAndView getQuiz(@PathVariable Long id) {
        Quiz quiz = quizService.getById(id);

        return QuizPageModel.builder()
                .currentQuiz(quiz)
                .feedback(null)
                .commentsCount(quizCommentService.countById(quiz))
                .build();
    }

    @GetMapping(path = "/api/quizzes")
    public ModelAndView getAllQuizzes(@RequestParam(defaultValue = CommonConstants.DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = CommonConstants.DEFAULT_PAGE_SIZE) int pageSize) {
        return HomePageModel.builder()
                .quizzes(quizService.getAll(page, pageSize).getContent())
                .pageSize(pageSize)
                .currentPage(page)
                .quizCont(quizService.count())
                .build();
    }

    @GetMapping(path = "/api/quizzes/completed")
    public Page<CompletedQuizzes> getCompletedQuizzes(@RequestParam(defaultValue = CommonConstants.DEFAULT_PAGE) int page,
                                                      @RequestParam(defaultValue = CommonConstants.DEFAULT_PAGE_SIZE) int pageSize) {
        return quizService.getCompleted(page, pageSize);
    }


    @DeleteMapping(path = "/api/quizzes/{id}")
    public ModelAndView deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);

        return HomePageModel.builder()
                .quizzes(quizService.getAll(0, CommonConstants.DEFAULT_INT_PAGE_SIZE).getContent())
                .pageSize(CommonConstants.DEFAULT_INT_PAGE_SIZE)
                .currentPage(0)
                .showForwardButton(quizService.count() != null && quizService.count() > 8)
                .build();
    }
}
