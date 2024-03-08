package mk.ukim.finki.ib_project.service.impl;

import mk.ukim.finki.ib_project.model.Permission;
import mk.ukim.finki.ib_project.model.User;
import mk.ukim.finki.ib_project.repository.PermissionRepository;
import mk.ukim.finki.ib_project.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void addPermission(List<User> userList, String fileName) {
        permissionRepository.save(new Permission(userList,fileName));
    }

    @Override
    public boolean checkPermission(User user, String fileName) {
        List<Permission> permissions = permissionRepository.findByFileNameAndUsers(fileName, user);

        for (Permission permission : permissions) {
            if (permission.getFileName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteFileName(String fileName) {
        Permission permission = permissionRepository.findByFileName(fileName);
        permissionRepository.delete(permission);
    }

    @Override
    public List<Permission> findAllDocumentsWithPermission(User user) {
        return permissionRepository.findPermissionsByUsersContaining(user);
    }
}
