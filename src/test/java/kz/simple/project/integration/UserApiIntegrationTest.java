package kz.simple.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kz.simple.project.entity.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Можно использовать тестовый профиль, если необходимо
@ActiveProfiles("test")
public class UserApiIntegrationTest {

    // Сюда Spring внедрит случайный порт, на котором запустилось приложение
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        // Устанавливаем базовые параметры для всех запросов Rest Assured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/users";
    }

    @Test
    void addUser() {
        String userJson = "{\"name\": \"Alikhan\", \"salary\": 500000}";

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
        .when()
                .post("/add")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Alikhan"))
                .body("salary", equalTo(500000));
    }

    @Test
    void getAllUsers() {
        given()
        .when()
                .get()
        .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void getById(){
        Long userId = 1L;

        given()
        .when()
                .get("/{id}", userId)
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Arman"))
                .body("salary", equalTo(250000));
    }

    @Test
    void deleteUser(){
        Long userId = 3L;

        given()
        .when()
                .post("/delete/{id}", userId)
        .then()
                .statusCode(204);
    }

}
