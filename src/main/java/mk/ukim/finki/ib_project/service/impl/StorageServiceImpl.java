package mk.ukim.finki.ib_project.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.ib_project.model.Permission;
import mk.ukim.finki.ib_project.repository.PermissionRepository;
import mk.ukim.finki.ib_project.security.AESUtil;
import mk.ukim.finki.ib_project.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final AmazonS3 amazonS3;
    private final PermissionRepository permissionRepository;

    @Value("${application.bucket.name}")
    private String bucketName;

    public StorageServiceImpl(AmazonS3 amazonS3, PermissionRepository permissionRepository) {
        this.amazonS3 = amazonS3;
        this.permissionRepository = permissionRepository;
    }


    @Override
    public List<String> listAllFiles() {
        List<String> fileList = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result result;
        do {
            result = amazonS3.listObjectsV2(req);

            result.getObjectSummaries().forEach(object -> {
                String fileName = object.getKey();
                fileList.add(fileName);
            });
            String token = result.getNextContinuationToken();
            req.setContinuationToken(token);
        } while (result.isTruncated());
        return fileList;
    }
    @Override
    public String uploadFile(MultipartFile file){
        try {
            byte[] fileContent = file.getBytes();
            SecretKey aesKey = AESUtil.generateAESKey();
            byte[] encryptedContent = AESUtil.encrypt(fileContent, aesKey);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptedContent);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(encryptedContent.length);
            amazonS3.putObject(bucketName, file.getOriginalFilename(), inputStream, metadata);
            Permission permission = permissionRepository.findByFileName(file.getOriginalFilename());
            permission.setDecryptionKey(aesKey);
            permissionRepository.save(permission);
            // Return the filename or any other information you need
            return "File uploaded: " + file.getOriginalFilename();
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error uploading file", e);
            return "Error uploading file";
        }
    }


    @Override
    public File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    @Override
    public String deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            SecretKey decryptionKey = permissionRepository.findByFileName(fileName).getDecryptionKey();
            System.out.println(decryptionKey);
            byte[] decryptedContent = AESUtil.decrypt(content, decryptionKey);
            return decryptedContent;
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
