package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.QuizComments.QuizComment;

import java.util.List;

public class CommentsPageModel extends AbstractPageModel {
    private static final String PAGE_NAME = "Comments";
    private static final String COMMENTS = "comments";
    private static final String QUIZ_ID = "id";
    private static final String COMMENT = "comment";

    CommentsPageModel(String pageName) {
        super(pageName);
    }

    ///STEP BUILDER
    public static QuizId builder() {
        return new CommentsPageModelBuilder();
    }

    public interface QuizId {
        Comments quizId(Long quizId);
    }

    public interface Comments {
        NewComment comments(List<QuizComment> comments);
    }

    public interface NewComment {
        CommentsCount newComment(QuizComment newComment);
    }

    public interface CommentsCount {
        PageSize commentsCount(double commentsCount);
    }

    public interface PageSize {
        CurrentPage pageSize(int pageSize);
    }

    public interface CurrentPage {
        Build currentPage(int currentPage);
    }

    public interface Build {
        CommentsPageModel build();
    }

    public static class CommentsPageModelBuilder implements QuizId, Comments, NewComment, CommentsCount, PageSize,
                                                            CurrentPage, Build {
        private Long quizId;
        private List<QuizComment> comments;
        private QuizComment newComment;
        private double commentsCount;
        private int pageSize;
        private int currentPage;

        @Override
        public Comments quizId(Long quizId) {
            this.quizId = quizId;
            return this;
        }

        @Override
        public NewComment comments(List<QuizComment> comments) {
            this.comments = comments;
            return this;
        }

        @Override
        public CommentsCount newComment(QuizComment newComment) {
            this.newComment = newComment;
            return this;
        }

        @Override
        public PageSize commentsCount(double commentsCount) {
            this.commentsCount = commentsCount;
            return this;
        }

        @Override
        public CurrentPage pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        @Override
        public Build currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        @Override
        public CommentsPageModel build() {
            CommentsPageModel cpm = new CommentsPageModel(PAGE_NAME);
            cpm.addObject(QUIZ_ID, quizId);
            cpm.addObject(COMMENTS, comments);
            cpm.addObject(COMMENT, newComment);

            cpm.addPageableObjects(commentsCount, pageSize, currentPage);
            return cpm;
        }
    }
}
