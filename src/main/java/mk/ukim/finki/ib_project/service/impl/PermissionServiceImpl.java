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
}
