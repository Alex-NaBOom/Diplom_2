package ru.praktikum.diplom_2.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom_2.model.Order;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private final String ORDERS_URL = BASE_URL + "/api/orders";

    @Step("Get ingredients id")

    public ArrayList<String> getIngredientsId() {
        return given()
                .header("Content-type", JSON)
                .get(BASE_URL + "/api/ingredients")
                .path("data._id");
    }

    @Step("Create order")
    public Response createOrder(Order data, String accessToken) {
        return given()
                .header("Content-type", JSON)
                .header("Authorization", accessToken)
                .and()
                .body(data)
                .when()
                .post(ORDERS_URL);
    }

    @Step("Get user's orders")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("Content-type", JSON)
                .header("Authorization", accessToken)
                .get(ORDERS_URL);

    }
}
