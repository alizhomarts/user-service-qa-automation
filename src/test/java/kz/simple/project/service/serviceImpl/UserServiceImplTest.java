package kz.simple.project.service.serviceImpl;


import kz.simple.project.entity.User;
import kz.simple.project.repository.UserRepository;

import kz.simple.project.service.imp.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void getUserTest(){
        // 1. Arrange (Подготовка данных)
        Long userId = 1L;
        User user = new User(userId, "Maksat", 300000);

        // 2. Mock (Мокирование)
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // 3. Act (Действие)
        Optional<User> result = userService.getUser(userId);

        // 4. Assert & Verify (Проверка)
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsersTest(){
        // 1. Arrange (Подготовка данных)
        User user1 = new User(1L, "Arman", 250000);
        User user2 = new User(1L, "Ramazan", 450000);

        // 2. Mock (Мокирование)
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // 3. Act (Действие)
        List<User> results = userService.getAllUsers();

        // 4. Assert & Verify (Проверка)
        Assertions.assertEquals(results, List.of(user1, user2), "Список пользователей должен совпадать.");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getSumOfSalariesAllUsersTest(){
        // 1. Arrange (Подготовка данных)
        User user1 = new User(1L, "Arman", 250000);
        User user2 = new User(1L, "Ramazan", 450000);
        int expectedSum = user1.getSalary()+ user2.getSalary();

        // 2. Mock (Мокирование)
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // 3. Act (Действие)
        int actualSum = userService.getSumOfSalariesAllUsers();

        // 4. Assert & Verify (Проверка)
        Assertions.assertEquals(expectedSum, actualSum, "Сумма зарплат должна совпадать.");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void addUserTest(){
        // 1. Arrange (Подготовка данных)
        User user = new User(1L, "Aruzhan", 150000);

        // 2. Act (Действие)
        userService.addUser(user);

        // 3. Verify (Проверка)
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addUserTest2(){
        User unsavedUser = new User(null, "Aruzhan", 150000);
        User savedUser = new User(5L, "Aruzhan", 150000); // Предполагаем, что репозиторий возвращает объект с ID

        // Добавляем мокирование возврата объекта
        when(userRepository.save(unsavedUser)).thenReturn(savedUser);

        User result = userService.addUser(unsavedUser);

        // Проверяем, что сервис вернул объект с присвоенным ID
        Assertions.assertEquals(savedUser.getId(), result.getId());

        // Проверяем, что метод save был вызван
        verify(userRepository, times(1)).save(unsavedUser);
    }

    @Test
    void deleteUserTest(){
        // 1. Arrange (Подготовка данных)
        long userId = 1L;

        // 2. Act (Действие)
        userService.deleteUser(userId);

        // 3. Verify (Проверка)
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void updateUserTest() {
        Long userId = 1L;
        User user = new User(userId, "Arman", 350000);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUser_NotFound_Test(){
        Long nonExistenUserId = 99L;

        when(userRepository.findById(nonExistenUserId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(nonExistenUserId);

        Assertions.assertFalse(result.isPresent(), "Optional должен быть пустым, если пользователь не найден.");

        verify(userRepository, times(1)).findById(nonExistenUserId);
    }
}
