package com.company.Quiz_Tournament.api.QuizImage;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.util.Arrays;

@Entity(name = "Quiz_Image")
public class QuizImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private byte[] data;

    private ImageType type;

    private String mimeType;

    private QuizImage() {}

    public QuizImage(byte[] data, String mimeType, ImageType type) {
        this.data = data;
        this.mimeType = mimeType;
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getId() {
        return id;
    }

    public String getData() {
        return data == null ? "" : Base64.encodeBase64String(data);
    }

    public ImageType getType() {
        return type;
    }

    public String getMimeType() {
        return mimeType == null ? "" : mimeType;
    }

    public static QuizImage emptyQuizImage() {
        return new QuizImage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuizImage quizImage = (QuizImage) o;

        return Arrays.equals(data, quizImage.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
