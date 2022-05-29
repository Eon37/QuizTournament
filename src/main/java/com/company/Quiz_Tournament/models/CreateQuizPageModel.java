package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.Quiz.Quiz;

public class CreateQuizPageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "CreateQuiz";
    private static final String QUIZ = "emptyQuiz";

    CreateQuizPageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static CreateQuizPageModelBuilder builder() {
        return new CreateQuizPageModelBuilder();
    }

    public interface TemplateQuiz {
        Build quiz(Quiz quiz);
    }

    public interface Build {
        CreateQuizPageModel build();
    }

    public static class CreateQuizPageModelBuilder implements TemplateQuiz, Build {
        private Quiz quiz;

        @Override
        public Build quiz(Quiz quiz) {
            this.quiz = quiz;
            return this;
        }

        @Override
        public CreateQuizPageModel build() {
            CreateQuizPageModel cqpm = new CreateQuizPageModel(PAGE_NAME);
            cqpm.addObject(QUIZ, quiz);

            return cqpm;
        }
    }
}
