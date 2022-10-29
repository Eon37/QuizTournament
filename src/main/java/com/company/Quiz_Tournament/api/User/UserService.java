package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.QuizImage.ImageType;
import com.company.Quiz_Tournament.api.QuizImage.QuizImage;
import com.company.Quiz_Tournament.utils.ContextUtils;
import com.company.Quiz_Tournament.api.QuizImage.QuizImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public User create(User user, String newPassword, MultipartFile image) throws IOException {
        if (isRegistered(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such an amazing email already exists");
        }

        String password = passwordEncoder.encode(newPassword);

        QuizImage userImage = image.isEmpty()
                ? quizImageService.save(quizImageService.getRandomDefaultImage(ImageType.DEFAULT_USER))
                : quizImageService.save(new QuizImage(image.getBytes(), image.getContentType(), ImageType.USER));

        User userToSave = new User(user.getEmail(), user.getNickname(), password, userImage);

        return userRepository.save(userToSave);
    }

    public User update(User user, String newPassword, MultipartFile image) throws IOException {
        User userToUpdate = ContextUtils.getCurrentUserOrThrow();

        if (checkPasswordUpdate(user, newPassword, userToUpdate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong email or password");
        }

        userToUpdate.setEmail(StringUtils.isEmptyOrWhitespace(user.getEmail())
                ? userToUpdate.getEmail()
                : user.getEmail());

        userToUpdate.setPassword(StringUtils.isEmptyOrWhitespace(newPassword)
                ? userToUpdate.getPassword()
                : passwordEncoder.encode(newPassword));

        userToUpdate.setNickname(StringUtils.isEmptyOrWhitespace(user.getNickname())
                ? userToUpdate.getNickname()
                : user.getNickname());

        userToUpdate.setImage(image.isEmpty()
                ? userToUpdate.getImage()
                : quizImageService.save(new QuizImage(image.getBytes(), image.getContentType(), ImageType.USER)));

        userToUpdate = userRepository.save(userToUpdate);
        ContextUtils.updateContextUser(userToUpdate);

        return userToUpdate;
    }

    private boolean checkPasswordUpdate(User user, String newPassword, User persistedUser) {
        return !StringUtils.isEmptyOrWhitespace(newPassword)
                && !passwordEncoder.matches(user.getPassword(), persistedUser.getPassword());
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
}
