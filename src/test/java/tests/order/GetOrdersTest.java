package tests.order;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class GetOrdersTest {
    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersList() {
        Response response = ScooterApi.getOrders();
        response.then()
                .statusCode(SC_OK)
                .body("orders", not(empty()))
                .body("pageInfo.total", greaterThan(0));
    }
}