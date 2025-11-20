package kz.simple.project.service;


import kz.simple.project.entity.User;
import java.util.List;
import java.util.Optional;


public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUser(Long id);
    User addUser(User user);
    void deleteUser(Long id);
    User updateUser(User user);

    int getSumOfSalariesAllUsers();
}
