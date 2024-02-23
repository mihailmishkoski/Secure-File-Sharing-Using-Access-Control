package mk.ukim.finki.ib_project.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.ib_project.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String bucketName;

    public StorageServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
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
        File fileObject = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName,fileName,fileObject));
        fileObject.delete();
        return "File uploaded" + fileName;
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
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
