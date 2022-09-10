package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzes;
import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzesService;
import com.company.Quiz_Tournament.api.FeedBack;
import com.company.Quiz_Tournament.api.QuizImage.ImageType;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.api.QuizImage.QuizImageService;
import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetails;
import com.company.Quiz_Tournament.utils.ContextUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @InjectMocks
    private QuizService quizService;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuizImageService quizImageService;
    @Mock
    private CompletedQuizzesService completedQuizzesService;

    private final MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[]{});

    @BeforeEach
    void beforeEach() {
        User user = User.newEmptyUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                Collections.emptyList());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(auth);
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
        assertEquals("There should be more than one unique option", exception.getReason());
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
        assertEquals("There should be more than one unique option", exception.getReason());
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
        assertEquals("There should be more than one unique option", exception.getReason());
    }

    @Test
    void noExceptionOptions() throws IOException {
        //Given
        List<String> initialOptions = Arrays.asList("1", "2", "69");
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(initialOptions, true)
                .build();
        QuizImage emptyImage = QuizImage.emptyQuizImage();

        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);

        ArgumentCaptor<Quiz> savedQuiz = ArgumentCaptor.forClass(Quiz.class);
        Mockito.when(quizRepository.save(savedQuiz.capture())).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
        assertEquals(initialOptions, savedQuiz.getValue().getOptions());
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

        ArgumentCaptor<Quiz> savedQuiz = ArgumentCaptor.forClass(Quiz.class);
        Mockito.when(quizRepository.save(savedQuiz.capture())).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
        assertEquals(Arrays.asList("1", "2", "69"), savedQuiz.getValue().getOptions());
    }

    @Test
    void filterAnswerBelowOptionsSize() throws IOException {
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(Arrays.asList("1", "2", "69"), true)
                .answer(Arrays.asList(1, 2))
                .build();
        QuizImage emptyImage = QuizImage.emptyQuizImage();

        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);

        ArgumentCaptor<Quiz> savedQuiz = ArgumentCaptor.forClass(Quiz.class);
        Mockito.when(quizRepository.save(savedQuiz.capture())).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
        assertEquals(Arrays.asList(1, 2), savedQuiz.getValue().getAnswer());
    }

    @Test
    void filterAnswerAboveOptionsSize() throws IOException {
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(Arrays.asList("1", "2", "69"), true)
                .answer(Arrays.asList(1, 2, 3))
                .build();
        QuizImage emptyImage = QuizImage.emptyQuizImage();

        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);

        ArgumentCaptor<Quiz> savedQuiz = ArgumentCaptor.forClass(Quiz.class);
        Mockito.when(quizRepository.save(savedQuiz.capture())).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
        assertEquals(Arrays.asList(1, 2), savedQuiz.getValue().getAnswer());
    }

    @Test
    void filterAnswerWhenDuplicates() throws IOException {
        Quiz quiz = Quiz.builder(Quiz.newEmptyQuiz(false))
                .options(Arrays.asList("1", "1", "2", "2", "69"), true)
                .answer(Arrays.asList(1, 2, 3))
                .build();
        QuizImage emptyImage = QuizImage.emptyQuizImage();

        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_QUIZ)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);

        ArgumentCaptor<Quiz> savedQuiz = ArgumentCaptor.forClass(Quiz.class);
        Mockito.when(quizRepository.save(savedQuiz.capture())).thenReturn(quiz);

        //When
        quizService.save(quiz, mockMultipartFile);

        //Then
        assertEquals(1, ContextUtils.getCurrentUserOrThrow().getQuizzes().size());
        assertEquals(Arrays.asList(1, 2), savedQuiz.getValue().getAnswer());
    }

    @Test
    void solveQuizIncorrectAnswer() {
        //Given
        Quiz quiz = Quiz.builder()
                .options(Arrays.asList("1", "2", "69"), false)
                .answer(Arrays.asList(1, 2))
                .build();
        Quiz incorrectAnswer = Quiz.builder().build();
        incorrectAnswer.setAnswerInterceptor(Arrays.asList(0));

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.of(quiz));

        //When
        FeedBack fb = quizService.solve(incorrectAnswer, 0L);

        //Then
        assertFalse(fb.isSuccess());
    }

    @Test
    void solveQuizQuizNotFoundException() {
        //Given
        Quiz answer = Quiz.builder().build();
        answer.setAnswerInterceptor(Arrays.asList(0, 1));

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.empty());

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> quizService.solve(answer, 0L));

        //Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(exception.getReason(), "Quiz not found");
    }

    @Test
    void solveQuizCorrectAnswer() {
        //Given
        Quiz quiz = Quiz.builder()
                .options(Arrays.asList("1", "2", "69"), false)
                .answer(Arrays.asList(1, 2))
                .build();
        Quiz answer = Quiz.builder().build();
        answer.setAnswerInterceptor(Arrays.asList(1, 2));

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.of(quiz));

        //When
        FeedBack fb = quizService.solve(answer, 0L);

        //Then
        assertTrue(fb.isSuccess());
    }

    @Test
    void solveQuizCorrectAnswerAlreadySolved() {
        //Given
        Quiz quiz = Quiz.builder()
                .options(Arrays.asList("1", "2", "69"), false)
                .answer(Arrays.asList(1, 2))
                .build();
        Quiz answer = Quiz.builder().build();
        answer.setAnswerInterceptor(Arrays.asList(1, 2));

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.of(quiz));
        Mockito.when(completedQuizzesService.isAlreadySolved(quiz, ContextUtils.getCurrentUserOrThrow())).thenReturn(true);

        //When
        FeedBack fb = quizService.solve(answer, 0L);

        //Then
        assertTrue(fb.isSuccess());
    }

    @Test
    void solveQuizCorrectAnswerSaveToCompletedQuizzes() {
        //Given
        Quiz quiz = Quiz.builder()
                .options(Arrays.asList("1", "2", "69"), false)
                .answer(Arrays.asList(1, 2))
                .build();
        Quiz answer = Quiz.builder().build();
        answer.setAnswerInterceptor(Arrays.asList(1, 2));

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.of(quiz));
        Mockito.when(completedQuizzesService.isAlreadySolved(quiz, ContextUtils.getCurrentUserOrThrow())).thenReturn(false);
        Mockito.doNothing().when(completedQuizzesService).save(Mockito.any(CompletedQuizzes.class));

        //When
        FeedBack fb = quizService.solve(answer, 0L);

        //Then
        assertTrue(fb.isSuccess());
    }

    @Test
    void deleteForbiddenException() {
        //Given
        User quizUser = User.newEmptyUser();
        quizUser.setEmail("nagibator3000@gmail.com");

        Quiz quiz = Quiz.builder()
                .user(quizUser)
                .build();

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.of(quiz));

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> quizService.delete(0L));

        //Then
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("You have no rights to delete this quiz", exception.getReason());
    }

    @Test
    void deleteNoException() {
        //Given
        Quiz quiz = Quiz.builder()
                .user(ContextUtils.getCurrentUserOrThrow())
                .build();

        Mockito.when(quizRepository.findById(0L)).thenReturn(Optional.of(quiz));
        Mockito.doNothing().when(quizRepository).delete(quiz);

        //When
        quizService.delete(0L);

        //Then no exception thrown
    }
}
