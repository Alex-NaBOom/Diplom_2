package ru.praktikum.diplom_2.user;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.diplom_2.clients.UserClient;
import ru.praktikum.diplom_2.data.UserDataGenerator;
import ru.praktikum.diplom_2.model.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserLoginTest {
    private final UserClient userClient = new UserClient();
    private User user;
    private String accessToken;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        user = UserDataGenerator.getGeneratedUser();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("User login")
    public void userLoginTest() {
        Response response = userClient.loginUser(user);
        response
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("User login with incorrect login and password")
    public void userLoginIncorrectLoginPasswordTest() {
        User newUser = UserDataGenerator.getGeneratedUser();
        newUser.password = "";
        Response response = userClient.loginUser(newUser);
        response
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }
}
