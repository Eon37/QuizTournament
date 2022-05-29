package com.company.Quiz_Tournament.api.QuizComments;

import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.models.CommentsPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class QuizCommentController {

    private final QuizCommentService quizCommentService;

    @Autowired
    QuizCommentController(QuizCommentService quizCommentService) {
        this.quizCommentService = quizCommentService;
    }

    @GetMapping(path = "/api/quizzes/{id}/comments")
    public ModelAndView getComments(@PathVariable Long id,
                                    @RequestParam(defaultValue = CommonConstants.DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = CommonConstants.DEFAULT_COMMENTS_SIZE) int pageSize) {
        List<QuizComment> comments = quizCommentService.getComments(id, page, pageSize).getContent();

        return CommentsPageModel.builder()
                .quizId(id)
                .comments(comments)
                .newComment(new QuizComment())
                .commentsCount(quizCommentService.countById(id))
                .pageSize(pageSize)
                .currentPage(page)
                .build();
    }

    @PostMapping(path = "/api/quizzes/{id}/comments")
    public ModelAndView createComment(@PathVariable Long id,
                                      @ModelAttribute(name = "comment") QuizComment quizComment,
                                      @RequestParam(defaultValue = CommonConstants.DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = CommonConstants.DEFAULT_COMMENTS_SIZE) int pageSize) {
        quizCommentService.save(id, quizComment.getText());
        List<QuizComment> comments = quizCommentService.getComments(id, page, pageSize).getContent();

        return CommentsPageModel.builder()
                .quizId(id)
                .comments(comments)
                .newComment(quizComment)
                .commentsCount(quizCommentService.countById(id))
                .pageSize(pageSize)
                .currentPage(page)
                .build();
    }

}
