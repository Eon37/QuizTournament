package com.company.Quiz_Tournament.api;

import com.company.Quiz_Tournament.api.Quiz.QuizService;
import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.models.HomePageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainPageController {

    QuizService quizService;

    MainPageController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        Long count = quizService.count();

        return HomePageModel.builder()
                .quizzes(quizService.getAll(0, CommonConstants.DEFAULT_INT_PAGE_SIZE).getContent())
                .pageSize(CommonConstants.DEFAULT_INT_PAGE_SIZE)
                .currentPage(0)
                .showForwardButton(count != null && count > 8)
                .build();
    }
}
