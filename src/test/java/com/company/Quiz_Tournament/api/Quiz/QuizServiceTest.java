package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.QuizImage.ImageType;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.api.QuizImage.QuizImageService;
import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetails;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.utils.ContextUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @InjectMocks
    private QuizService quizService;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuizImageService quizImageService;

    private final MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[]{});

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
    void exceptionWhenNoOptions() {
        //Given
        Quiz quiz = Quiz.newEmptyQuiz(false);

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> quizService.save(quiz, mockMultipartFile));

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(exception.getReason(), "There should be more than one unique option");
    }

    @Test
    void exceptionWhenNoDistinctOptions() {
        //Given
        Quiz quiz = Quiz.newEmptyQuiz(true); //contains 10 string options of the same value

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> quizService.save(quiz, mockMultipartFile));

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(exception.getReason(), "There should be more than one unique option");
    }

    @Test
    void exceptionWhenOneUniqueOptions() {
        //Given
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(Arrays.asList("1", "1", "1"), true)
                .build();

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> quizService.save(quiz, mockMultipartFile));

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(exception.getReason(), "There should be more than one unique option");
    }

    @Test
    void noExceptionOptions() throws IOException {
        //Given
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(Arrays.asList("1", "2", "69"), true)
                .build();
        QuizImage emptyImage = QuizImage.emptyQuizImage();

        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);
        Mockito.when(quizRepository.save(Mockito.any(Quiz.class))).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
    }

    @Test
    void noExceptionOptionsWithDuplicates() throws IOException {
        //Given
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(Arrays.asList("1", "1", "2", "69", "69"), true)
                .build();
        QuizImage emptyImage = QuizImage.emptyQuizImage();

        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);
        Mockito.when(quizRepository.save(Mockito.any(Quiz.class))).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
    }
}
