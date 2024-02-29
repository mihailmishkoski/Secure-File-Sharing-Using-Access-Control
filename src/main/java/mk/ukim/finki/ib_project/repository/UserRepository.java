package mk.ukim.finki.ib_project.repository;

import mk.ukim.finki.ib_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    List<User> findByIdIn(List<Long> ids);
}
