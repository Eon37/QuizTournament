package com.company.Quiz_Tournament.api.CompletedQuizzes;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import com.company.Quiz_Tournament.api.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CompletedQuizzesRepository extends PagingAndSortingRepository<CompletedQuizzes, Long> {

    Page<CompletedQuizzes> findByUser(@Param("user") User user, Pageable pageable);

    CompletedQuizzes findByUserAndQuiz(@Param("user") User user, @Param("Quiz") Quiz quiz);
}
