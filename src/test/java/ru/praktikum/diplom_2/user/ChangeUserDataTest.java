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

public class ChangeUserDataTest {
    private final UserClient userClient = new UserClient();
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
        User user = UserDataGenerator.getGeneratedUser();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Change user data with authorization")
    public void changeUserDataWithAuthorizationTest() {
        User newUser = UserDataGenerator.getGeneratedUser();
        Response responseChangedData = userClient.changeUserData(newUser, accessToken);
        responseChangedData
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newUser.email))
                .body("user.name", equalTo(newUser.name));

    }

    @Test
    @DisplayName("Change user data without authorization")
    public void changeUserDataWithoutAuthorizationTest() {
        User newUser = UserDataGenerator.getGeneratedUser();
        Response responseChangedData = userClient.changeUserData(newUser, "");
        responseChangedData
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }
}
