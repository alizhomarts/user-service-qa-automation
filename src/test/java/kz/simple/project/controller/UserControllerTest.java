package kz.simple.project.controller;

import kz.simple.project.entity.User;
import kz.simple.project.service.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void addUser_ReturnsCreatedUser() throws Exception{
        User userToSave = new User(null, "Test", 200000); // User без ID для отправки
        User savedUser = new User(1L, "Test", 200000);

        when(userService.addUser(any(User.class))).thenReturn(savedUser);

        String userJson = "{\"name\" : \"Test\", \"salary\" : 200000}";

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))

                // 3. Проверяем HTTP-статус и тело ответа
                .andExpect(status().isCreated()) // Ожидаем 201 Created
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"));
    }
}
