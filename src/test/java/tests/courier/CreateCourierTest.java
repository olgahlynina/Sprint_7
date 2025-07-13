package tests.courier;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Test;
import utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    private final DataGenerator dataGenerator = new DataGenerator();
    private Courier courier;
    private int courierId;

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccess() {
        courier = dataGenerator.generateRandomCourier();
        Response response = ScooterApi.createCourier(
                courier.getLogin(),
                courier.getPassword(),
                courier.getFirstName()
        );

        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    public void createDuplicateCourier() {
        courier = dataGenerator.generateRandomCourier();
        ScooterApi.createCourier(
                courier.getLogin(),
                courier.getPassword(),
                courier.getFirstName()
        );

        Response response = ScooterApi.createCourier(
                courier.getLogin(),
                courier.getPassword(),
                courier.getFirstName()
        );

        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        Response response = ScooterApi.createCourier(
                "",
                "password",
                "name"
        );

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courier != null) {
            Response loginResponse = ScooterApi.loginCourier(
                    courier.getLogin(),
                    courier.getPassword()
            );

            if (loginResponse.statusCode() == SC_OK) {
                courierId = loginResponse.path("id");
                ScooterApi.deleteCourier(courierId);
            }
        }
    }
}