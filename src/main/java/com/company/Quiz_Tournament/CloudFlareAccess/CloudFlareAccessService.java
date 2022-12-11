package com.company.Quiz_Tournament.CloudFlareAccess;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class CloudFlareAccessService {
    private final Logger logger = LoggerFactory.getLogger(CloudFlareAccessService.class);

    private final AmazonS3 r2Client;
    private final String publicUrl;
    private final String bucketName;

    @Autowired
    public CloudFlareAccessService(AmazonS3 r2Client,
                                   @Value("${r2.publicUrl}") String publicUrl,
                                   @Value("${r2.bucketName}") String bucketName) {
        this.r2Client = r2Client;
        this.publicUrl = publicUrl;
        this.bucketName = bucketName;
    }

    public String putObject(String path, File content) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, content)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult result = r2Client.putObject(putObjectRequest);
        return r2Client.getUrl(bucketName, path).getPath();
    }

    public String putObject(String path, MultipartFile content) {
        try {
            String uniqueName = UUID.nameUUIDFromBytes(content.getOriginalFilename().getBytes()).toString();
            ObjectMetadata data = new ObjectMetadata();
            data.setContentType(content.getContentType());
            data.setContentLength(content.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    path + uniqueName,
                    content.getInputStream(),
                    data);

            PutObjectResult result = r2Client.putObject(putObjectRequest);
            return publicUrl + r2Client.getUrl(bucketName, path + uniqueName).getPath().replace("/" + bucketName, "");
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry, service currently unavailable");
        }
    }
}
