package com.snowmanvillage.server.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        dirName = dirName + "/";
        String fileName = createFileName(multipartFile.getOriginalFilename(), dirName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        putS3(multipartFile, fileName, objectMetadata);
        removeNewFile(convert(multipartFile));
        return fileName;
    }

    private String createFileName(String originalFileName, String dirName) {
        return dirName + UUID.randomUUID() + getFileExtension(originalFileName);
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일입니다. [%s]", fileName));
        }
    }

    private void putS3(MultipartFile uploadFile, String fileName, ObjectMetadata objectMetadata) {
        try(InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 업로드 중 에러가 발생하였습니다. [%s]", fileName));
        }
    }

    private File convert(MultipartFile multipartFile) {
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try(FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. [%s]", multipartFile.getOriginalFilename()));
        }
        return convertFile;
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
            throw new IllegalArgumentException(String.format("파일 삭제 중 에러가 발생하였습니다. [%s]", targetFile.getName()));
        }
    }
}
