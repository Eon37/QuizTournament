package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetails;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.constants.EmptyQuizConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class QuizTest {

    @BeforeEach
    void beforeEach() {
        User user = User.newEmptyUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                Collections.emptyList());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void emptyQuizOptionsSize() {
        assertEquals(Quiz.newEmptyQuiz(true).getOptions().size(), CommonConstants.DEFAULT_INT_OPTIONS_SIZE);
    }

    @Test
    void addEmptyOptions() {
        //Given
        List<String> initList = new ArrayList<>(Arrays.asList("1", "2", "3"));

        //When
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(initList, true)
                .build();

        //Then
        assertEquals(CommonConstants.DEFAULT_INT_OPTIONS_SIZE, quiz.getOptions().size());
        assertTrue(quiz.getOptions().containsAll(initList));
        assertEquals(CommonConstants.DEFAULT_INT_OPTIONS_SIZE - initList.size(),
                quiz.getOptions().stream().filter(EmptyQuizConstants.OPTION::equals).count());
    }

    @Test
    void addEmptyOptionsWhenAllFilled() {
        //Given
        List<String> initList = IntStream.range(0, CommonConstants.DEFAULT_INT_OPTIONS_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        //When
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(initList, true)
                .build();

        //Then
        assertEquals(CommonConstants.DEFAULT_INT_OPTIONS_SIZE, quiz.getOptions().size());
        assertFalse(quiz.getOptions().contains(EmptyQuizConstants.OPTION));
    }
}
