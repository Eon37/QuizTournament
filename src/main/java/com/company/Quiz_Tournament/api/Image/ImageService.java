package com.company.Quiz_Tournament.api.Image;

import com.company.Quiz_Tournament.CloudFlareAccess.CloudFlareAccessService;
import com.company.Quiz_Tournament.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ImageService {
    private static final String QUIZ_IMG_FOLDER = "quizzes/img/";
    private static final String USER_IMG_FOLDER = "users/img/";

    private final CloudFlareAccessService cloudFlareAccessService;

    @Autowired
    public ImageService(CloudFlareAccessService cloudFlareAccessService) {
        this.cloudFlareAccessService = cloudFlareAccessService;
    }

    public String save(MultipartFile image, ImageType imageType) {
        if (image.getSize() > 2097152L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is too large. Allowed size is 2MB or less");
        }

        return cloudFlareAccessService.putObject(imageType == ImageType.USER ? USER_IMG_FOLDER : QUIZ_IMG_FOLDER, image);
    }

    public static String getRandomDefaultImageUrl(ImageType type) {
        StringBuilder sb = new StringBuilder("/images/");

        switch (type) {
            case DEFAULT_QUIZ:
                sb.append("quizzes/defaults/quiz");
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
