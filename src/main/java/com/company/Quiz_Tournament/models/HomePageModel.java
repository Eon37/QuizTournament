package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.Quiz.Quiz;

import java.util.List;

public class HomePageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "Home";
    private static final String ALL_QUIZZES = "quizzes";

    HomePageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static Quizzes builder() {
        return new HomePageModelBuilder();
    }

    public interface Quizzes {
        PageSize quizzes(List<Quiz> quizzes);
    }

    public interface PageSize {
        CurrentPage pageSize(int pageSize);
    }

    public interface CurrentPage {
        Forward currentPage(int currentPage);
    }

    public interface Forward {
        Build quizCont(double quizCount);
        Build showForwardButton(boolean showForwardButton);
    }

    public interface Build {
        HomePageModel build();
    }

    public static class HomePageModelBuilder implements Quizzes, PageSize, CurrentPage, Forward, Build {
        private List<Quiz> quizzes;
        private int pageSize;
        private int currentPage;
        private Double quizCount;
        private Boolean showForwardButton;

        @Override
        public PageSize quizzes(List<Quiz> quizzes) {
            this.quizzes = quizzes;
            return this;
        }

        @Override
        public CurrentPage pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        @Override
        public Forward currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        /**
         * Calculate forward button neediness depending on quiz amount
         */
        @Override
        public Build quizCont(double quizCount) {
            this.quizCount = quizCount;
            return this;
        }

        /**
         * Calculate forward button neediness depending on boolean
         */
        @Override
        public Build showForwardButton(boolean showForwardButton) {
            this.showForwardButton = showForwardButton;
            return this;
        }

        @Override
        public HomePageModel build() {
            HomePageModel hpm = new HomePageModel(PAGE_NAME);
            hpm.addObject(ALL_QUIZZES, quizzes);

            if (showForwardButton != null) {
                hpm.addPageableObjects(showForwardButton, pageSize, currentPage);
            } else {
                hpm.addPageableObjects(quizCount, pageSize, currentPage);
            }

            return hpm;
        }
    }
}
