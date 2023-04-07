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
import ru.praktikum.diplom_2.model.User;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetUserOrderTest {
    private final OrderClient orderClient = new OrderClient();
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
    public void setUp() throws InterruptedException {
        User user = UserDataGenerator.getGeneratedUser();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
        // Для избежания ответа 429 Too Many Requests
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    @DisplayName("Get user's orders with authorization")
    public void getUserOrdersWithAuthorizationTest() {
        Response response = orderClient.getUserOrders(accessToken);
        response
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Get user's orders without authorization")
    public void getUserOrdersWithoutAuthorizationTest() {
        Response response = orderClient.getUserOrders("");
        response
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) userClient.deleteUser(accessToken);
    }

}
