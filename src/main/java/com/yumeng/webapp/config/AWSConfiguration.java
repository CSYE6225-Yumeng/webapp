package com.yumeng.webapp.config;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AWSConfiguration.class);


    @Bean
    public AmazonS3 s3() {
        logger.info("Connect to Amazon S3...");
        logger.info("PROFILE_NAME: {}", System.getenv("PROFILE_NAME"));
        logger.info("REGION: {}", region);
        logger.info("BUCKET_NAME: {}", bucketName);

        System.out.println("PROFILE_NAME:");
        System.out.println(System.getenv("PROFILE_NAME"));
//        AWSCredentials awsCredentials =
//                new BasicAWSCredentials(accessKey, secretAccess);
//        return AmazonS3ClientBuilder
//                .standard()
//                .withRegion(region)
//                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                .build();
        AmazonS3 amazons3 =  AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();

        logger.info("[SUCCESS]Connect to Amazon S3 SUCCESS.");

        return amazons3;

    }
}
