package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class ScooterApi {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Step("Создать курьера")
    public static Response createCourier(String login, String password, String firstName) {
        String body = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                login, password, firstName
        );

        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(body)
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public static Response loginCourier(String login, String password) {
        String body = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\"}",
                login, password
        );

        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(body)
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера")
    public static Response deleteCourier(int id) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .delete("/api/v1/courier/" + id);
    }

    @Step("Создать заказ")
    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Получить список заказов")
    public static Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .get("/api/v1/orders");
    }

    @Step("Получить заказ по треку")
    public static Response getOrderByTrack(int track) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .queryParam("t", track)
                .get("/api/v1/orders/track");
    }

    @Step("Принять заказ")
    public static Response acceptOrder(int orderId, int courierId) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .pathParam("id", orderId)
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/{id}");
    }

    @Step("Проверка доступности сервера")
    public static Response pingServer() {
        return given()
                .baseUri(BASE_URL)
                .get("/api/v1/ping");
    }
}