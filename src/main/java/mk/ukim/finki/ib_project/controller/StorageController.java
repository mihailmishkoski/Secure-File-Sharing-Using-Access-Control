package mk.ukim.finki.ib_project.controller;

import mk.ukim.finki.ib_project.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(value = "file") MultipartFile file) {
        storageService.uploadFile(file);
        return "redirect:/home";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        storageService.deleteFile(fileName);
        return "redirect:/home";
    }
}
