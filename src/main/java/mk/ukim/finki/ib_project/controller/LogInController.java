package mk.ukim.finki.ib_project.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("account")
public class LogInController {

    private final UserService userService;

    public LogInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/login"})
    public String logInPage(@RequestParam(required = false) String email,
                            @RequestParam(required = false) String password,
                            Model model)
    {
        if (email != null && password != null) {

            if (userService.authenticate(email, password)) {
                User user = userService.findUser(email);
                userService.generateOtp(user);

                model.addAttribute("account", user);
                return "twoFactorPage";
            }
            model.addAttribute("errorMessage", "No user found with those credentials");
        }

        return "logInForm";
    }
    @GetMapping("/register")
    public String getRegisterForm()
    {
        return "registerForm";
    }

    @PostMapping("/register")
    public String Register(@RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           Model model,
                           HttpSession session)
    {
        boolean allGood = true;
        if (!userService.checkPasswords(password, repeatedPassword)) {
            allGood = false;
            model.addAttribute("error", "Passwords do not match");

        }
        if (!userService.isValidPassword(password)) {
            allGood = false;
            model.addAttribute("passwordError", "Password should contain at least 1 special character, uppercase and to be longer than 8 characters");
        }
        if (!userService.isValidEmail(email)) {
            allGood = false;
            model.addAttribute("emailError", "Email is not valid");
        }

        model.addAttribute("name", name);
        model.addAttribute("surname", surname);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("repeatPassword", repeatedPassword);
        if (allGood) {
            int code = userService.mailVerification(email);
            session.setAttribute("userDetails", new User(name,surname,email,password));
            session.setAttribute("verificationCode", code);
            return "emailVerificationForm";
        }
        return "registerForm";
    }
    @PostMapping("/emailValidation")
    public String EmailVerification(@RequestParam Integer code,
                                    Model model, HttpSession session) {
        User userDetails = (User) session.getAttribute("userDetails");
        Integer storedCode = (Integer) session.getAttribute("verificationCode");

        if (userDetails != null && storedCode != null && storedCode.equals(code)) {
            userService.create(userDetails.getName(), userDetails.getSurname(), userDetails.getEmail(), userDetails.getPassword());

            session.removeAttribute("userDetails");
            session.removeAttribute("verificationCode");

            return "redirect:/login";
        }

        model.addAttribute("emailError", "Email verification failed.");
        return "registerForm";
    }

    @PostMapping("/twoFactor")
    public String twoFactorAuthentication(@RequestParam Integer code,
                                          @RequestParam String email,
                                          HttpSession session,
                                          Model model) {
        User user = userService.findUser(email);
        if (code == user.getTwoFactorCode()) {
            session.setAttribute("account", user);
            return "redirect:/home";
        }
        model.addAttribute("errorMessage", "No user found with those credentials");
        return "logInForm";
    }
}
