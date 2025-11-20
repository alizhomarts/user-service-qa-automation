package kz.simple.project.service.imp;

import kz.simple.project.entity.User;
import kz.simple.project.repository.UserRepository;
import kz.simple.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (getUser(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User updateUser(User user) {
        Optional<User> oldUser = getUser(user.getId());

        return oldUser
                .map(newUser -> {
                    newUser.setName(user.getName());
                    newUser.setSalary(user.getSalary());
                    return newUser;
                })
                .map(userRepository::save)
                .orElse(null);
    }

    @Override
    public int getSumOfSalariesAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .mapToInt(User::getSalary)
                .sum();
    }

}
