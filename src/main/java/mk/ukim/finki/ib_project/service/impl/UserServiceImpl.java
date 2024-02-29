package mk.ukim.finki.ib_project.service.impl;
import mk.ukim.finki.ib_project.config.PasswordHashing;
import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.repository.UserRepository;
import mk.ukim.finki.ib_project.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordHashing ph;

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    public UserServiceImpl(PasswordHashing ph, UserRepository userRepository, JavaMailSender javaMailSender) {
        this.ph = ph;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    @Override
    public User create(String name, String surname, String email, String password) {
        String encodedPassword = ph.passwordEncoder().encode(password);
        User newAccount = new User(name,surname,email,encodedPassword);
        return userRepository.save(newAccount);
    }


    @Override
    public boolean authenticate(String email, String password) {
        User user = findUser(email);
        return user != null && ph.passwordEncoder().matches(password,user.getPassword()) && user.getEmail().matches(email);
    }
    @Override
    public boolean isValidPassword(String password)
    {
        boolean hasMinimumLength = password.length() >= 8;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasSpecialCharacter = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        return hasMinimumLength && hasUppercase && hasSpecialCharacter;
    }
    @Override
    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    public boolean checkPasswords(String password, String repeatedPassword)
    {
        return password.matches(repeatedPassword);
    }

    @Override
    public int mailVerification(String user_email)
    {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user_email);
        msg.setFrom("dmgameplays829@gmail");

        msg.setSubject("Welcome");
        msg.setText("Hello \n\n" +"Your Email Verification Code: " + randomPIN + " Please Verify.");
        javaMailSender.send(msg);
        return randomPIN;
    }
    @Override
    public void generateOtp(User account) {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        account.setTwoFactorCode(randomPIN);
        userRepository.save(account);
        SimpleMailMessage msg = new SimpleMailMessage();
        String email = account.getEmail();
        msg.setTo(email);
        msg.setFrom("dmgameplays829@gmail");

        msg.setSubject("Welcome");
        msg.setText("Hello \n\n" +"Your Login OTP: " + randomPIN + " Please Verify.");

        javaMailSender.send(msg);
    }

    @Override
    public User findUser(String email)
    {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> listAll()
    {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersById(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }
}
