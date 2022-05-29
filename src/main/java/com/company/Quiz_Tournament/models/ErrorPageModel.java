package com.company.Quiz_Tournament.models;

import org.springframework.http.HttpStatus;


public class ErrorPageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "error";
    private static final String CODE = "errorCode";
    private static final String MESSAGE = "errorMessage";

    ErrorPageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static ErrorPageModelBuilder builder() {
        return new ErrorPageModelBuilder();
    }

    public interface Build {
        Build code(HttpStatus code);
        Build message(String message);
        ErrorPageModel build();
    }

    public static class ErrorPageModelBuilder implements Build {
        private HttpStatus code;
        private String message = "We are already working on it (or not)";

        @Override
        public Build code(HttpStatus code) {
            this.code = code;
            return this;
        }

        @Override
        public Build message(String message) {
            this.message = message;
            return this;
        }

        @Override
        public ErrorPageModel build() {
            ErrorPageModel epm = new ErrorPageModel(PAGE_NAME);
            if (code != null) epm.addObject(CODE, code);
            if (message != null) epm.addObject(MESSAGE, message);

            return epm;
        }
    }
}
