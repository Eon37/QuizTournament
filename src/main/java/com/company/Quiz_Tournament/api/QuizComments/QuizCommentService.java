package com.company.Quiz_Tournament.api.QuizComments;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import com.company.Quiz_Tournament.api.Quiz.QuizService;
import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class QuizCommentService {
    private final QuizCommentRepository quizCommentsRepository;

    private final QuizService quizService;

    @Autowired
    QuizCommentService(QuizCommentRepository quizCommentsRepository, QuizService quizService) {
        this.quizCommentsRepository = quizCommentsRepository;
        this.quizService = quizService;
    }

    public void save(Long quizId, String text) {
        User user = ContextUtils.getCurrentUserOrThrow();

        QuizComment quizComment = new QuizComment(
                user,
                quizService.getById(quizId),
                text,
                new Timestamp(System.currentTimeMillis())
        );

        quizCommentsRepository.save(quizComment);
    }

    public Page<QuizComment> getComments(Long id, int pageNo, int pageSize) {
        return quizCommentsRepository.findByQuiz(
                quizService.getById(id),
                PageRequest.of(pageNo, pageSize, Sort.by("postedAt").descending())
        );
    }

    public long count() {
        return quizCommentsRepository.count();
    }

    public long countById(Quiz quiz) {
        return quizCommentsRepository.findByQuiz(quiz, PageRequest.of(0, Integer.MAX_VALUE)).getContent().size();
    }

    public long countById(Long id) {
        Quiz quiz = quizService.getById(id);
        return quizCommentsRepository.findByQuiz(quiz, PageRequest.of(0, Integer.MAX_VALUE)).getContent().size();
    }
}
