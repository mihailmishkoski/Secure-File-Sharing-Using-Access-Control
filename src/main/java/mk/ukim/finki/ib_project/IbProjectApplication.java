package mk.ukim.finki.ib_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class IbProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbProjectApplication.class, args);
	}

}
