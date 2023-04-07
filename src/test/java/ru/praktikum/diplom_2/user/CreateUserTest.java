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

public class CreateUserTest {

        private final UserClient userClient = new UserClient();
        private User user;
        private String accessToken;
        private final static String FIELDLESS_ERROR = "Email, password and name are required fields"; // TODO
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
        }

        @Test
        @DisplayName("Creating user")
        public void createUserTest() {
            Response response = userClient.createUser(user);
            accessToken = response.path("accessToken");
            response
                    .then()
                    .statusCode(200)
                    .body("success", equalTo(true))
                    .body("user", notNullValue())
                    .body("accessToken", notNullValue())
                    .body("refreshToken", notNullValue());
        }

        @Test
        @DisplayName("Creating existing user")
        public void createUserAlreadyExistsTest(){
            User oldUser = UserDataGenerator.getGeneratedUser();
            userClient.createUser(oldUser);

            Response response2 = userClient.createUser(oldUser);
            response2
                    .then()
                    .statusCode(403)
                    .body("success", equalTo(false))
                    .body("message",equalTo("User already exists"));
        }

        @Test
        @DisplayName("Creating user without e-mail")
        public void createUserWithoutEmail() {
            User newUser = UserDataGenerator.getGeneratedUser();
            newUser.email = "";
            Response response = userClient.createUser(newUser);
            response
                    .then()
                    .statusCode(403)
                    .body("success", equalTo(false))
                    .body("message", equalTo("Email, password and name are required fields"));
        }
    @Test
    @DisplayName("Creating user without e-mail is Null")
    public void createUserWithoutEmailNull() {
        User newUser = UserDataGenerator.getGeneratedUser();
        newUser.email = null;
        Response response = userClient.createUser(newUser);
        response
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
          @DisplayName("Creating user without password")
          public void createUserWithoutPassword() {
              User newUser = UserDataGenerator.getGeneratedUser();
              newUser.password = "";
              Response response = userClient.createUser(newUser);
              response
                      .then()
                      .statusCode(403)
                      .body("success", equalTo(false))
                      .body("message", equalTo("Email, password and name are required fields"));
          }
    @Test
    @DisplayName("Creating user without password is Null")
    public void createUserWithoutPasswordNull() {
        User newUser = UserDataGenerator.getGeneratedUser();
        newUser.password = null;
        Response response = userClient.createUser(newUser);
        response
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
        @Test
        @DisplayName("Creating user without name")
        public void createUserWithoutName() {
            User newUser = UserDataGenerator.getGeneratedUser();
            newUser.name = "";
            Response response = userClient.createUser(newUser);
            response
                    .then()
                    .statusCode(403)
                    .body("success", equalTo(false))
                    .body("message", equalTo("Email, password and name are required fields"));
        }
    @Test
    @DisplayName("Creating user without name")
    public void createUserWithoutNameNull() {
        User newUser = UserDataGenerator.getGeneratedUser();
        newUser.name = null;
        Response response = userClient.createUser(newUser);
        response
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
        @Test
        @DisplayName("Creating user without data")
        public void createUserWithoutData() {
            User user = new User();
            Response response = userClient.createUser(user);
            response
                    .then()
                    .statusCode(403)
                    .body("success", equalTo(false))
                    .body("message",equalTo("Email, password and name are required fields"));
        }

        @After
        public void tearDown() {
            if (accessToken != null) userClient.deleteUser(accessToken);
        }
}
