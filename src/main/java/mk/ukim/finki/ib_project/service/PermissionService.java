package mk.ukim.finki.ib_project.service;

import mk.ukim.finki.ib_project.model.User;

import java.util.List;

public interface PermissionService {

    void addPermission(List<User> userList, String fileName);

    boolean checkPermission(User user, String fileName);
}
