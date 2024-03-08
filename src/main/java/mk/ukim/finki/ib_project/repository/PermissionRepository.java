package mk.ukim.finki.ib_project.repository;

import mk.ukim.finki.ib_project.model.Permission;
import mk.ukim.finki.ib_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    List<Permission> findByFileNameAndUsers(String fileName, User user);
    Permission findByFileName(String fileName);

    List<Permission> findPermissionsByUsersContaining(User user);

}
