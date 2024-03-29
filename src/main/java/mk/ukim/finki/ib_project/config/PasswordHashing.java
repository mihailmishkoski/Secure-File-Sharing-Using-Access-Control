package mk.ukim.finki.ib_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashing {

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
