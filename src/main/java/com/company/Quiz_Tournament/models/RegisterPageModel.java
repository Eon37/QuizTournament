package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.User.User;

public class RegisterPageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "Register";
    private static final String NEW_USER = "emptyUser";

    RegisterPageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static NewUser builder() {
        return new RegisterPageModelBuilder();
    }

    public interface NewUser {
        Build newUser(User user);
    }

    public interface Build {
        RegisterPageModel build();
    }

    public static class RegisterPageModelBuilder implements NewUser, Build {
        private User user;

        @Override
        public Build newUser(User user) {
            this.user = user;
            return this;
        }

        @Override
        public RegisterPageModel build() {
            RegisterPageModel rpm = new RegisterPageModel(PAGE_NAME);
            rpm.addObject(NEW_USER, user);

            return rpm;
        }
    }
}
