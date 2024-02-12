package com.mmos.mmos.src.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.entity.Files;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_FILE;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Files findById(Long fileIndex) throws BaseException {
        return fileRepository.findById(fileIndex)
                .orElseThrow(() -> new BaseException(EMPTY_FILE));

    }

    @Transactional
    public Files uploadFile(MultipartFile multipartFile, Post post) throws BaseException {
        try {
            System.out.println(multipartFile);
            System.out.println(post);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            String originalFilename = multipartFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String ext = originalFilename.substring(index + 1);

            String storeFileName = UUID.randomUUID() + "." + ext;
            String key = "test/" + storeFileName;

            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }

            String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();
            Files uploadFile = new Files(storeFileUrl, post);

            return fileRepository.save(uploadFile);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteFile(Files file) throws BaseException {
        try {
            fileRepository.delete(file);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
