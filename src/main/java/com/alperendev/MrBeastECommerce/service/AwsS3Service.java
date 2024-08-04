package com.alperendev.MrBeastECommerce.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class AwsS3Service {

    private final String bucketName = "mrbeastecommerce";

    @Value("${aws.s3.access}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret}")
    private String awsS3SecretKey;

    public String saveImageToS3(MultipartFile photo) {
        try {
            String s3FileName = photo.getOriginalFilename();
            //create aws credentials using the access and secret key
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            //create a s3 client with config credentials
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            //GET input stream from photo
            InputStream inputStream = photo.getInputStream();

            //set metadata for the object
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            //create a put request to upload the image to S3
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);
            s3Client.putObject(putObjectRequest);

            return "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;


        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to upload image to S3 bucket: " + e.getMessage());
        }
    }

}
