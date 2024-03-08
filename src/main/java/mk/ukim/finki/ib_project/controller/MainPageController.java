package mk.ukim.finki.ib_project.controller;

import mk.ukim.finki.ib_project.model.Permission;
import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.service.PermissionService;
import mk.ukim.finki.ib_project.service.StorageService;
import mk.ukim.finki.ib_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"account","currentUserId"})
public class MainPageController {


    private final StorageService storageService;

    private final UserService userService;

    private final PermissionService permissionService;
    public MainPageController(StorageService storageService, UserService userService, PermissionService permissionService) {
        this.storageService = storageService;
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @GetMapping("/home")
    public String getHomePage(Model model)
    {
        User user = (User) model.getAttribute("account");
        User updated_account = userService.findUserById(user.getId());
        model.addAttribute("account",updated_account);
        model.addAttribute("currentUserId", updated_account.getId());

        List<String> files = storageService.listAllFiles();

        List<String> allowedFiles = new ArrayList<>();
        List<Permission> permissions = permissionService.findAllDocumentsWithPermission(updated_account);
        for (Permission permission : permissions) {
            allowedFiles.add(permission.getFileName());
        }


        model.addAttribute("files", allowedFiles);
        model.addAttribute("users", userService.listAll());
        return "mainPage";
    }
}
