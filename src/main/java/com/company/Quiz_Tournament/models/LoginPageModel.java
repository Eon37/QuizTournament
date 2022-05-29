package com.company.Quiz_Tournament.models;

public class LoginPageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "Login";

    LoginPageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static Build builder() {
        return new LoginPageModelBuilder();
    }

    public interface Build {
        LoginPageModel build();
    }

    public static class LoginPageModelBuilder implements Build {

        @Override
        public LoginPageModel build() {
            LoginPageModel lpm = new LoginPageModel(PAGE_NAME);

            return lpm;
        }
    }
}
