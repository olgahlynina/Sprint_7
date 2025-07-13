package tests.order;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import models.OrderColor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final Order order;
    private final DataGenerator dataGenerator = new DataGenerator();

    public CreateOrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        DataGenerator generator = new DataGenerator();
        return new Object[][]{
                {generator.generateOrderWithColor(OrderColor.BLACK)},
                {generator.generateOrderWithColor(OrderColor.GREY)},
                {generator.generateOrderWithTwoColors()},
                {generator.generateRandomOrder()}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrderWithDifferentColors() {
        Response response = ScooterApi.createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
}