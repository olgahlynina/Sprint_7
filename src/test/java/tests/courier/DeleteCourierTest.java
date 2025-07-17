package tests.courier;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.Before;
import org.junit.Test;
import utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class DeleteCourierTest {
    private final DataGenerator dataGenerator = new DataGenerator();
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
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
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    public void deleteCourierSuccess() {
        Response response = ScooterApi.deleteCourier(courierId);
        response.then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление несуществующего курьера")
    public void deleteNonExistingCourier() {
        Response response = ScooterApi.deleteCourier(999999);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id нет"));
    }
}
