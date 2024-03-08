package mk.ukim.finki.ib_project.controller;

import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.service.PermissionService;
import mk.ukim.finki.ib_project.service.StorageService;
import mk.ukim.finki.ib_project.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@SessionAttributes({"account","currentUserId"})
public class StorageController {

    private final StorageService storageService;

    private final PermissionService permissionService;

    private final UserService userService;


    public StorageController(StorageService storageService, PermissionService permissionService, UserService userService){
        this.storageService = storageService;
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam(required = false) List<Long> userIds, Model model) {

        List<User> userList = new ArrayList<>();
        if(userIds!=null)
        {
            userList = userService.findUsersById(userIds);
        }
        userList.add(userService.findUserById((Long)model.getAttribute("currentUserId")));


        permissionService.addPermission(userList, file.getOriginalFilename());
        storageService.uploadFile(file);

        return "redirect:/home";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName, Model model) {
        Long currentUserId = (Long) model.getAttribute("currentUserId");
        User currentUser = (User) userService.findUserById(currentUserId);
        if(permissionService.checkPermission(currentUser, fileName))
        {
            byte[] data = storageService.downloadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        }
        else return null;
    }

    @PostMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName, Model model) {
        User currentUser = userService.findUserById((Long)model.getAttribute("currentUserId"));
        if(permissionService.checkPermission(currentUser,fileName))
        {
            storageService.deleteFile(fileName);
        }

        return "redirect:/home";
    }
}
