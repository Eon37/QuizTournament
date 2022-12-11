package com.company.Quiz_Tournament.configs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2Config {
    @Bean
    public AmazonS3 r2Client(@Value("${r2.baseUrl}") String r2BaseUrl,
                             @Value("${r2.accessKey}") String accessKeyValue,
                             @Value("${r2.secretKey}") String secretKeyValue) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyValue, secretKeyValue);
        AwsClientBuilder.EndpointConfiguration endpointCfg
                = new AwsClientBuilder.EndpointConfiguration(r2BaseUrl, null);

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointCfg)
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
