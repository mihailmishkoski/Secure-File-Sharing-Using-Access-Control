package mk.ukim.finki.ib_project.service;

import mk.ukim.finki.ib_project.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface StorageService {

    String uploadFile(MultipartFile file);

    File convertMultiPartFileToFile(MultipartFile file);

    String deleteFile(String fileName);

    byte[] downloadFile(String fileName);

    List<String> listAllFiles();

}
