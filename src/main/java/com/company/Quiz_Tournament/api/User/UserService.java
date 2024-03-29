package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.Image.ImageService;
import com.company.Quiz_Tournament.api.Image.ImageType;
import com.company.Quiz_Tournament.utils.ContextUtils;
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
    private ImageService imageService;

    public User create(User user, String newPassword, MultipartFile image) {
        if (isRegistered(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such an amazing email already exists");
        }

        String password = passwordEncoder.encode(newPassword);

        String userImage = image.isEmpty()
                ? ImageService.getRandomDefaultImageUrl(ImageType.DEFAULT_USER)
                : imageService.save(image, ImageType.USER);

        User userToSave = User.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password(password)
                .image(userImage)
                .build();

        return userRepository.save(userToSave);
    }

    public User update(User user, String newPassword, MultipartFile image) throws IOException {
        User userToUpdate = ContextUtils.getCurrentUserOrThrow();

        if (checkPasswordUpdate(user, newPassword, userToUpdate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong email or password");
        }

        String email = StringUtils.isEmptyOrWhitespace(user.getEmail())
                ? userToUpdate.getEmail()
                : user.getEmail();
        String password = StringUtils.isEmptyOrWhitespace(newPassword)
                ? userToUpdate.getPassword()
                : passwordEncoder.encode(newPassword);
        String nickname = StringUtils.isEmptyOrWhitespace(user.getNickname())
                ? userToUpdate.getNickname()
                : user.getNickname();
        String userImage = image.isEmpty()
                ? userToUpdate.getImage()
                : imageService.save(image, ImageType.USER);

        User userToSave = User.builder()
                .id(userToUpdate.getId())
                .email(email)
                .password(password)
                .nickname(nickname)
                .image(userImage)
                .build();

        userToSave = userRepository.save(userToSave);
        ContextUtils.updateContextUser(userToSave);

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
