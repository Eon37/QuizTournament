package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.QuizImage.ImageType;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetails;
import com.company.Quiz_Tournament.utils.ContextUtils;
import com.company.Quiz_Tournament.api.QuizImage.QuizImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QuizImageService quizImageService;

    public User create(User user, MultipartFile image) throws IOException {
        if (isRegistered(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such an amazing email already exists");
        }

        return save(user, image, false);
    }

    public User save(User user, MultipartFile image, boolean isRegistered) throws IOException {
        if (!isRegistered) {
            user.setPassword(passwordEncoder.encode(user.getNewPassword())); //todo try newPass in @RequestPart

            user.setImage(image.isEmpty()
                    ? quizImageService.save(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_USER))
                    : quizImageService.save(new QuizImage(image.getBytes(), image.getContentType(), ImageType.USER)));

            return userRepository.save(user);
        }

        User persistedUser = ContextUtils.getCurrentUserOrThrow();
        user.setEmail(StringUtils.isEmptyOrWhitespace(user.getEmail())
                ? persistedUser.getEmail()
                : user.getEmail());

        user.setPassword(StringUtils.isEmptyOrWhitespace(user.getNewPassword())
                ? persistedUser.getPassword()
                : passwordEncoder.encode(user.getNewPassword()));

        user.setNickname(StringUtils.isEmptyOrWhitespace(user.getNickname())
                ? persistedUser.getNickname()
                : user.getNickname());

        user.setImage(image.isEmpty()
                ? persistedUser.getImage()
                : quizImageService.save(new QuizImage(image.getBytes(), image.getContentType(), ImageType.USER)));

        user.setQuizzes(persistedUser.getQuizzes());
        user.setId(persistedUser.getId());

        userRepository.save(user);
        updateSpringSecurityContext(user);

        return user;
    }

    private void updateSpringSecurityContext(User user) {
        CustomUserDetails cud = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                cud,
                user.getPassword(),
                ContextUtils.getCurrentAuthentication().getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public boolean isRegistered(String email) {
        return getByEmail(email).isPresent();
    }

    /**
     * @param email
     * @return user with specified email or null
     */
    public Optional<User> getByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public User update(User user, MultipartFile image) throws IOException {
        Optional<User> persistedUser = ContextUtils.getCurrentUser();

        if (persistedUser.isEmpty() || (!StringUtils.isEmptyOrWhitespace(user.getNewPassword())
                && !persistedUser.get().getPassword().equals(passwordEncoder.encode(user.getPassword())))) { //todo Auth
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong email or password");
        }

        return save(user, image, true);
    }
}
