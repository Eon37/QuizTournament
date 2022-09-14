package com.company.Quiz_Tournament.configs;

import com.company.Quiz_Tournament.models.ErrorPageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandling {

    private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandling.class);

    @ExceptionHandler({Exception.class})
    public ModelAndView handleBindException(Exception e) {
        logger.error(e.toString());

        return ErrorPageModel.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler({BindException.class})
    public ModelAndView handleBindException(BindException e) {
        logger.error(e.toString());

        return ErrorPageModel.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(e.getFieldErrors().stream()
                        .map(fe -> fe.getField() + " - " + fe.getDefaultMessage())
                        .collect(Collectors.joining("; ")))
                .build();
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ModelAndView handleResponseStatusException(ResponseStatusException e) {
        logger.error(e.toString());

        return ErrorPageModel.builder()
                .code(e.getStatus())
                .message(e.getReason())
                .build();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ModelAndView handleValidatedException(ConstraintViolationException e) {
        return ErrorPageModel.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(e.getConstraintViolations().stream()
                        .map(c -> "New password - " + c.getMessage()) //currently, only new password checked here
                        .collect(Collectors.joining("; ")))
                .build();
    }
}
