package com.company.Quiz_Tournament.api.QuizImage;

import com.company.Quiz_Tournament.constants.CommonConstants;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuizImageService {

    @Autowired
    private QuizImageRepository quizImageRepository;

    public QuizImage save(QuizImage image) {
        quizImageRepository.save(image);
        return image;
    }

    public QuizImage getById(Long id) {
        return quizImageRepository.findById(id).orElse(null);
    }

    public QuizImage getRandomDefaultImage(ImageType type) {
        try {
            byte[] data = IOUtils.toByteArray(new ClassPathResource(constructFileName(type)).getInputStream());

            return new QuizImage(data, "image/png", type);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default images currently can't be set");
        }
    }

    private String constructFileName(ImageType type) {
        StringBuilder sb = new StringBuilder("/static/images/");

        switch (type) {
            case DEFAULT_QUIZ:
                sb.append("quizzes/quiz");
                sb.append(ThreadLocalRandom.current().nextInt(CommonConstants.DEFAULT_QUIZ_IMG_CNT));
                sb.append(".png");

                return sb.toString();
            case DEFAULT_USER:
                sb.append("users/defaults/animal");
                sb.append(ThreadLocalRandom.current().nextInt(CommonConstants.DEFAULT_USER_IMG_CNT));
                sb.append(".png");

                return sb.toString();
        }

        return "";
    }
}
