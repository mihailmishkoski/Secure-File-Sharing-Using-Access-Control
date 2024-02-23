package mk.ukim.finki.ib_project.controller;

import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("account")
public class MainPageController {

    private final UserRepository userRepository;

    public MainPageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String getHomePage(Model model)
    {
        User user = (User) model.getAttribute("account");
        User updated_account = userRepository.findById(user.getId()).orElse(null);
        model.addAttribute("account",updated_account);
        return "mainPage";
    }
}
