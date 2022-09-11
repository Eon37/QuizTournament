package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.AnswerInterceptor;
import com.company.Quiz_Tournament.api.FeedBack;
import com.company.Quiz_Tournament.api.Quiz.Quiz;

public class QuizPageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "CurrentQuiz";
    private static final String QUIZ = "quiz";
    private static final String FEEDBACK = "feedback";
    private static final String COMMENTS_COUNT = "commentsCount";
    private static final String ANSWER_INTERCEPTOR = "answerInterceptor";

    QuizPageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static CurrentQuiz builder() {
        return new QuizPageModelBuilder();
    }

    public interface CurrentQuiz {
        QuizFeedBack currentQuiz(Quiz currentQuiz);
    }

    public interface QuizFeedBack {
        Answer feedback(FeedBack feedback);
    }

    public interface Answer {
        CommentsCount answerInterceptor(AnswerInterceptor answerInterceptor);
    }

    public interface CommentsCount {
        Build commentsCount(long commentsCount);
    }

    public interface Build {
        QuizPageModel build();
    }

    public static class QuizPageModelBuilder implements CurrentQuiz, QuizFeedBack, Answer, CommentsCount, Build {
        private Quiz currentQuiz;
        private FeedBack feedback;
        private long commentsCount;
        private AnswerInterceptor answerInterceptor;

        @Override
        public QuizFeedBack currentQuiz(Quiz currentQuiz) {
            this.currentQuiz = currentQuiz;
            return this;
        }

        @Override
        public Answer feedback(FeedBack feedback) {
            this.feedback = feedback;
            return this;
        }
        @Override
        public CommentsCount answerInterceptor(AnswerInterceptor answerInterceptor) {
            this.answerInterceptor = answerInterceptor;
            return this;
        }

        @Override
        public Build commentsCount(long commentsCount) {
            this.commentsCount = commentsCount;
            return this;
        }

        @Override
        public QuizPageModel build() {
            QuizPageModel qpm = new QuizPageModel(PAGE_NAME);
            qpm.addObject(QUIZ, currentQuiz);
            qpm.addObject(FEEDBACK, feedback);
            qpm.addObject(COMMENTS_COUNT, commentsCount);
            qpm.addObject(ANSWER_INTERCEPTOR, answerInterceptor);

            return qpm;
        }
    }
}
