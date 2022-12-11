package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.Image.ImageType;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.api.QuizImage.QuizImageService;
import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetails;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private QuizImageService quizImageService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private final MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[]{});

    private void addContextUser(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                Collections.emptyList());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createUserAlreadyRegisteredException() {
        //Given
        String email = "test@email.com";
        User newUser = User.builder()
                .email(email)
                .nickname("New")
                .password("")
                .build();
        User persistedUser = User.builder()
                .email(email)
                .nickname("Persisted")
                .password("")
                .build();
        String newPassword = "fluggegecheimen";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(persistedUser);

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> userService.create(newUser, newPassword, mockMultipartFile));

        //Then
        assertSame(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User with such an amazing email already exists", exception.getReason());
    }

    @Test
    void createUserSuccess() throws IOException {
        //Given
        String email = "test@email.com";
        User user = User.builder()
                .email(email)
                .nickname("New")
                .password("")
                .build();
        String newPassword = "fluggegecheimen";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        QuizImage emptyImage = QuizImage.emptyQuizImage();
        Mockito.when(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_USER)).thenReturn(emptyImage);
        Mockito.when(quizImageService.save(emptyImage)).thenReturn(emptyImage);

        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        Mockito.when(userRepository.save(savedUser.capture())).thenReturn(user);

        //When
        userService.create(user, newPassword, mockMultipartFile);

        //Then
        assertEquals(email, savedUser.getValue().getEmail());
        assertEquals(newPassword, savedUser.getValue().getPassword());
    }

    @Test
    void updateUserWithoutUpdatingPasswordSuccess() throws IOException {
        //Given
        User persistedUser = User.builder()
                .email("test@email.com")
                .nickname("test")
                .password("password")
                .build();
        addContextUser(persistedUser);

        String newNickname = "Chief Twit";
        User user = User.builder(persistedUser)
                .nickname(newNickname)
                .build();

        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        Mockito.when(userRepository.save(savedUser.capture())).thenReturn(user);

        //When
        userService.update(user, "", mockMultipartFile);

        //Then
        assertEquals(newNickname, savedUser.getValue().getNickname());
        assertEquals(persistedUser.getEmail(), savedUser.getValue().getEmail());
        assertEquals(persistedUser.getPassword(), savedUser.getValue().getPassword());
        assertEquals(persistedUser.getImage(), savedUser.getValue().getImage());
    }

    @Test
    void updateUserWithUpdatingPasswordSuccess() throws IOException {
        //Given
        String oldPassword = "password";
        String newPassword = "newPassword";
        User persistedUser = User.builder()
                .email("test@email.com")
                .nickname("test")
                .password(oldPassword)
                .build();
        addContextUser(persistedUser);

        User user = User.builder(persistedUser).build();

        Mockito.when(passwordEncoder.matches(user.getPassword(), persistedUser.getPassword())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        Mockito.when(userRepository.save(savedUser.capture())).thenReturn(user);

        //When
        userService.update(user, newPassword, mockMultipartFile);

        //Then
        assertEquals(newPassword, savedUser.getValue().getPassword());
        assertEquals(persistedUser.getEmail(), savedUser.getValue().getEmail());
        assertEquals(persistedUser.getNickname(), savedUser.getValue().getNickname());
    }

    @Test
    void updateUserWithUpdatingPasswordOldDoesntMatch() {
        //Given
        String oldPassword = "password";
        String oldWrongPassword = "wrongPassword";
        String newPassword = "newPassword";
        User persistedUser = User.builder()
                .email("test@email.com")
                .nickname("test")
                .password(oldPassword)
                .build();
        addContextUser(persistedUser);

        User user = User.builder(persistedUser)
                .password(oldWrongPassword)
                .build();

        Mockito.when(passwordEncoder.matches(user.getPassword(), persistedUser.getPassword()))
                .thenReturn(user.getPassword().equals(persistedUser.getPassword()));

        //When
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> userService.update(user, newPassword, mockMultipartFile));

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Wrong email or password", exception.getReason());
    }
}
