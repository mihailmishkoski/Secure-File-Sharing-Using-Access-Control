package mk.ukim.finki.ib_project.controller;

import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.repository.UserRepository;
import mk.ukim.finki.ib_project.service.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("account")
public class MainPageController {

    private final UserRepository userRepository;

    private final StorageService storageService;
    public MainPageController(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @GetMapping("/home")
    public String getHomePage(Model model)
    {
        User user = (User) model.getAttribute("account");
        User updated_account = userRepository.findById(user.getId()).orElse(null);
        model.addAttribute("account",updated_account);

        List<String> files = storageService.listAllFiles();

        model.addAttribute("files", files);
        return "mainPage";
    }
}
