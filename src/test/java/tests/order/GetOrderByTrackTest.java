package tests.order;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.junit.Test;
import utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class GetOrderByTrackTest {
    private final DataGenerator dataGenerator = new DataGenerator();

    @Test
    @DisplayName("Получение заказа по треку")
    public void getOrderByTrackSuccess() {
        Order order = dataGenerator.generateRandomOrder();
        Response createResponse = ScooterApi.createOrder(order);
        int track = createResponse.path("track");

        Response response = ScooterApi.getOrderByTrack(track);
        response.then()
                .statusCode(SC_OK)
                .body("order.track", equalTo(track));
    }

    @Test
    @DisplayName("Поиск заказа без трека")
    public void getOrderWithoutTrack() {
        Response response = ScooterApi.getOrderByTrack(0);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Поиск несуществующего заказа")
    public void getNonExistingOrder() {
        Response response = ScooterApi.getOrderByTrack(999999);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Заказ не найден"));
    }
}