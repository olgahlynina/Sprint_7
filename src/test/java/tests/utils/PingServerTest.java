package tests.utils;

import api.ScooterApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class PingServerTest {

    @Test
    @DisplayName("Проверка доступности сервера")
    public void pingServerSuccess() {
        Response response = ScooterApi.pingServer();
        response.then()
                .statusCode(SC_OK)
                .body(equalTo("pong"));
    }
}
