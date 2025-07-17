package tests.order;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class AcceptOrderTest {
    private final DataGenerator dataGenerator = new DataGenerator();
    private Courier courier;
    private int courierId;
    private int orderId;

    @Before
    public void setUp() {
        // Создаем курьера
        courier = dataGenerator.generateRandomCourier();
        ScooterApi.createCourier(
                courier.getLogin(),
                courier.getPassword(),
                courier.getFirstName()
        );

        Response loginResponse = ScooterApi.loginCourier(
                courier.getLogin(),
                courier.getPassword()
        );
        courierId = loginResponse.path("id");

        // Создаем заказ
        Order order = dataGenerator.generateRandomOrder();
        Response createResponse = ScooterApi.createOrder(order);
        int track = createResponse.path("track");

        Response trackResponse = ScooterApi.getOrderByTrack(track);
        orderId = trackResponse.path("order.id");
    }

    @Test
    @DisplayName("Успешное принятие заказа")
    public void acceptOrderSuccess() {
        Response response = ScooterApi.acceptOrder(orderId, courierId);
        response.then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принятие заказа без указания курьера")
    public void acceptOrderWithoutCourier() {
        Response response = ScooterApi.acceptOrder(orderId, 0);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @After
    public void tearDown() {
        ScooterApi.deleteCourier(courierId);
    }
}