package mk.ukim.finki.ib_project.service;

import mk.ukim.finki.ib_project.model.User;

public interface UserService {
    User create (String name, String surname, String email, String password);
    boolean authenticate(String email, String password);

    boolean isValidPassword(String password);

    boolean isValidEmail(String email);

    boolean checkPasswords(String password, String repeatedPassword);


    int mailVerification(String user_email);

    void generateOtp(User account);

    User findUser(String email);
}
