package mk.ukim.finki.ib_project.repository;

import mk.ukim.finki.ib_project.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
