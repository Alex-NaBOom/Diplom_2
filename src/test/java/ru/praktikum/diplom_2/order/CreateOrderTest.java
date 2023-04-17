package ru.praktikum.diplom_2.order;

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
import ru.praktikum.diplom_2.clients.OrderClient;
import ru.praktikum.diplom_2.clients.UserClient;
import ru.praktikum.diplom_2.data.UserDataGenerator;
import ru.praktikum.diplom_2.model.Order;
import ru.praktikum.diplom_2.model.User;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTest {
    private final OrderClient orderClient = new OrderClient();
    private Order orderIngredientsIdList;
    private final UserClient userClient = new UserClient();
    private String accessToken;
    private User user;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        orderIngredientsIdList = new Order(orderClient.getIngredientsId());
        user = UserDataGenerator.getGeneratedUser();
    }

    @Test
    @DisplayName("Creating order with authorization")
    public void createOrderWithAuthorizationTest() {
        Response response1 = userClient.createUser(user);
        accessToken = response1.path("accessToken");

        Response response2 = orderClient.createOrder(orderIngredientsIdList, accessToken);
        response2
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Creating order without authorization")
    public void createOrderWithoutAuthorizationTest() {
        Response response = orderClient.createOrder(orderIngredientsIdList, "");
        response
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Creating order with valid id ingredient (hash)")
    public void createOrderWithValidIdIngredientTest() {
        ArrayList<String> listWrongId = new ArrayList<>();
        listWrongId.add("61c0c5a71d1f82001bdaaa7a");

        Response response = orderClient.createOrder(new Order(listWrongId), "");
        response
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Creating order without ingredients")
    public void createOrderWithoutIngredientsTest() {
        Response response = orderClient.createOrder(new Order(), "");
        response
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating order with invalid id ingredient (hash)")
    public void createOrderWithInvalidIdIngredientTest() {
        ArrayList<String> listWrongId = new ArrayList<>();
        listWrongId.add("000000000");

        Response response = orderClient.createOrder(new Order(listWrongId), "");
        response
                .then()
                .statusCode(500)
                .body("$", hasItem("Internal Server Error"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) userClient.deleteUser(accessToken);
    }

}
