package com.company.Quiz_Tournament.models;

import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.utils.ContextUtils;
import org.springframework.web.servlet.ModelAndView;

public class AbstractPageModel extends ModelAndView {
    private static final String CURRENT_USER = "currentUser";
    private static final String PAGE_SIZE = "pageSize";
    private static final String NEXT_PAGE = "nextPage";
    private static final String PREV_PAGE = "prevPage";
    private static final String SHOW_FORWARD_BUTTON = "showForward";

    AbstractPageModel(String pageName) {
        super(pageName);
        addObject(CURRENT_USER, ContextUtils.getCurrentUser().orElse(null));
    }

    /**
     * Calculate pages for pageable objects
     * @param count number of all quizzes in db
     * @param pageSize how many quizzes to show on page
     * @param currentPage what page to show
     */
    public void addPageableObjects(double count, int pageSize, int currentPage) {
        addObject(NEXT_PAGE, currentPage + 1);
        addObject(PREV_PAGE, currentPage - 1);
        addObject(SHOW_FORWARD_BUTTON, currentPage + 1 < Math.ceil(count / pageSize));
        addObject(PAGE_SIZE, pageSize);
    }

    /**
     * Calculate pages for pageable objects with predefined showForwardButton value
     * @param showForwardButton show "Forward" button if true
     * @param pageSize how many quizzes to show on page
     * @param currentPage what page to show
     */
    public void addPageableObjects(boolean showForwardButton, int pageSize, int currentPage) {
        addObject(NEXT_PAGE, currentPage + 1);
        addObject(PREV_PAGE, currentPage - 1);
        addObject(SHOW_FORWARD_BUTTON, showForwardButton);
        addObject(PAGE_SIZE, pageSize);
    }
}
