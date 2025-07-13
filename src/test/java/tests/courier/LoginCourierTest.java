package tests.courier;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginCourierTest {
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
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void loginCourierSuccess() {
        Response response = ScooterApi.loginCourier(
                courier.getLogin(),
                courier.getPassword()
        );

        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithWrongPassword() {
        Response response = ScooterApi.loginCourier(
                courier.getLogin(),
                "wrong_password"
        );

        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин без пароля")
    public void loginWithoutPassword() {
        Response response = ScooterApi.loginCourier(
                courier.getLogin(),
                ""
        );

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
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