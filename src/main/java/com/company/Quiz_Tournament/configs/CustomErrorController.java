package com.company.Quiz_Tournament.configs;

import com.company.Quiz_Tournament.models.ErrorPageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public ModelAndView handleError() {
        logger.warn("WhiteLabelErrorPage error");

        return ErrorPageModel.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}