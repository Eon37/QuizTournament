package com.company.Quiz_Tournament.api.CompletedQuizzes;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import com.company.Quiz_Tournament.api.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CompletedQuizzesService {

    @Autowired
    CompletedQuizzesRepository completedQuizzesRepository;

    public void save(CompletedQuizzes completedQuizzes) {
        completedQuizzesRepository.save(completedQuizzes);
    }

    public Page<CompletedQuizzes> getByUser(User user, Pageable pageable) {
        return completedQuizzesRepository.findByUser(user, pageable);
    }

    public boolean isAlreadySolved(Quiz quiz, User user) {
        return completedQuizzesRepository.findByUserAndQuiz(user, quiz) != null;
    }

}
