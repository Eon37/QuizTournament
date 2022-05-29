package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.CompletedQuizzes.CompletedQuizzes;

import java.util.List;

public class ProfilePageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "Profile";
    private static final String COMPLETED_QUIZZES = "solvedQuizzes";

    ProfilePageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static SolvedQuizzes builder() {
        return new ProfilePageModelBuilder();
    }

    public interface SolvedQuizzes {
        Build completedQuizzes(List<CompletedQuizzes> completedQuizzes);
    }

    public interface Build {
        ProfilePageModel build();
    }

    public static class ProfilePageModelBuilder implements SolvedQuizzes, Build {
        private List<CompletedQuizzes> completedQuizzes;

        @Override
        public Build completedQuizzes(List<CompletedQuizzes> completedQuizzes) {
            this.completedQuizzes = completedQuizzes;
            return this;
        }

        @Override
        public ProfilePageModel build() {
            ProfilePageModel ppm = new ProfilePageModel(PAGE_NAME);
            ppm.addObject(COMPLETED_QUIZZES, completedQuizzes);

            return ppm;
        }
    }
}
