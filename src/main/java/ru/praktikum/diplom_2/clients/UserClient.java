package ru.praktikum.diplom_2.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom_2.model.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient{
    private final String AUTH_URL = BASE_URL + "/api/auth";

    @Step("Create user")

    public Response createUser(User user) {
        return given()
                .header("Content-type", JSON)
                .and()
                .body(user)
                .when()
                .post(AUTH_URL + "/register");
    }

    @Step("Delete user")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Content-type", JSON)
                .header("Authorization", accessToken)
                .delete(AUTH_URL + "/user");
    }


    @Step("User login")
    public Response loginUser(User user) {
        return given()
                .header("Content-type", JSON)
                .and()
                .body(user)
                .when()
                .post(AUTH_URL + "/login");
    }

    @Step("Change user data")
    public Response changeUserData(User user, String  accessToken) {
        return given()
                .header("Content-type", JSON)
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(AUTH_URL + "/user");
    }
}
