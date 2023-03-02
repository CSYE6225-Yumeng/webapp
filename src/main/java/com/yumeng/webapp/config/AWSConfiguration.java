package com.yumeng.webapp.config;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {
//    @Value("${aws.access.key.id}")
//    private String accessKey;
//
//    @Value("${aws.secret.access.key}")
//    private String secretAccess;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

//    @Value("${aws.s3.bucket.name}")
//    private String profileName;


    @Bean
    public AmazonS3 s3() {
        System.out.println("PROFILE_NAME:");
        System.out.println(System.getenv("PROFILE_NAME"));
//        AWSCredentials awsCredentials =
//                new BasicAWSCredentials(accessKey, secretAccess);
//        return AmazonS3ClientBuilder
//                .standard()
//                .withRegion(region)
//                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                .build();
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();

    }
}
