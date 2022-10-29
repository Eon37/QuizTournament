package com.company.Quiz_Tournament.api.User;

import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzes;
import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzesService;
import com.company.Quiz_Tournament.models.LoginPageModel;
import com.company.Quiz_Tournament.models.ProfilePageModel;
import com.company.Quiz_Tournament.models.RegisterPageModel;
import com.company.Quiz_Tournament.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@Validated
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompletedQuizzesService completedQuizzesService;

    @PostMapping(path = "/login-request")
    public ModelAndView loginRequest() {
        return LoginPageModel.builder().build();
    }

    @GetMapping(path = "/login")
    public ModelAndView login() {
        return LoginPageModel.builder().build();
    }

    @GetMapping(path = "/api/register")
    public ModelAndView getRegisterPage() {
        return RegisterPageModel.builder()
                .newUser(User.newEmptyUser())
                .build();
    }

    @PostMapping(path = "/api/register")
    public ModelAndView register(@Valid @ModelAttribute("emptyUser") User user,
                                 @RequestParam("newPassword") @Size(min = 8) String newPassword,
                                 @RequestPart("avatar") MultipartFile image) throws IOException {
        userService.create(user, newPassword, image);
        return LoginPageModel.builder().build();
    }

    @GetMapping(path = "/api/users/current")
    public ModelAndView getUser() {
        User currentUser = ContextUtils.getCurrentUserOrThrow();
        List<CompletedQuizzes> completedQuizzes = completedQuizzesService.getByUser(currentUser, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        return ProfilePageModel.builder()
                .completedQuizzes(completedQuizzes)
                .build();
    }

    @PostMapping(path = "/api/users/current")
    public ModelAndView updateUser(@Valid @ModelAttribute("currentUser") User user,
                                   @RequestParam("newPassword") @Pattern(regexp = "^$|.{8,}") String newPassword,
                                   @RequestPart("avatar") MultipartFile image) throws IOException {
        user = userService.update(user, newPassword, image);
        List<CompletedQuizzes> completedQuizzes = completedQuizzesService.getByUser(user, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        return ProfilePageModel.builder()
                .completedQuizzes(completedQuizzes)
                .build();
    }
}
