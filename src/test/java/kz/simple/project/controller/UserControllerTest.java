package kz.simple.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.simple.project.entity.User;
import kz.simple.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void addUser() throws Exception{
        User user = new User(1L, "Test", 200000);
        String userJson = objectMapper.writeValueAsString(user);

        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                // 3. Проверяем HTTP-статус и тело ответа
                .andExpect(status().isCreated()) // Ожидаем 201 Created
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.salary").value(200000));
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void getAll() throws Exception {
        User user1 = new User(1L, "Ramazan", 300000);
        User user2 = new User(2L, "Arman", 400000);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Ramazan"))
                .andExpect(jsonPath("$[0].salary").value(300000))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Arman"))
                .andExpect(jsonPath("$[1].salary").value(400000));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getById() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "Test", 200000);

        when(userService.getUser(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем 201 Created
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.salary").value(200000));
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(post("/api/users/delete/{id}", userId))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(userId);
    }
}
