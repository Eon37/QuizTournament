package com.company.Quiz_Tournament.api.QuizComments;

import com.company.Quiz_Tournament.api.Quiz.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface QuizCommentRepository extends PagingAndSortingRepository<QuizComment, Long> {

    @Query("select qc from Quiz_Comment qc where qc.quiz = :quiz")
    Page<QuizComment> findByQuiz(@Param("quiz") Quiz quiz, Pageable pageable);
}
