package mk.ukim.finki.ib_project.repository;

import mk.ukim.finki.ib_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    List<User> findByIdIn(List<Long> ids);


}
