package com.company.Quiz_Tournament.api.QuizImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class QuizImageService {

    @Autowired
    private QuizImageRepository quizImageRepository;

    public QuizImage save(MultipartFile image, ImageType imageType) throws IOException {
        QuizImage quizImage = new QuizImage(image.getBytes(), image.getContentType(), imageType);

        quizImageRepository.save(quizImage);
        return quizImage;
    }

    public QuizImage getById(Long id) {
        return quizImageRepository.findById(id).orElse(null);
    }

    public QuizImage getRandomDefaultImage(ImageType type) {
        return quizImageRepository.findRandomImage(type.ordinal());
    }
}
