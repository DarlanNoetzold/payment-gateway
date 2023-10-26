package tech.noetzold;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import tech.noetzold.controller.CustomerController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(CustomerController.class)
public class CustomerControllerTest {

    private String accessToken;

    @BeforeEach
    public void obtainAccessToken() {
        final String username = "admin";
        final String password = "admin";

        final String tokenEndpoint = "http://localhost:8180/realms/quarkus/protocol/openid-connect/token";

        final Map<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        requestData.put("grant_type", "password");

        final Response response = given()
                .auth().preemptive().basic("backend-service", "secret")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParams(requestData)
                .when()
                .post(tokenEndpoint);

        response.then().statusCode(200);

        this.accessToken = response.jsonPath().getString("access_token");
    }

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetCustomerModelByUserId() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("http://localhost:7000/api/payment/v1/customer/{id}", "yourUserIdHere")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetCustomerModelById() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("http://localhost:7000/api/payment/v1/customer/paymentId/{id}", "yourCustomerIdHere")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveCustomerModel() {
        JsonObject json = Json.createObjectBuilder()
                .add("userId", "yourUserIdHere")
                .add("registerDate", "2023-10-26T00:00:00")  // Adjust with the date you want
                .build();

        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("http://localhost:7000/api/payment/v1/customer")
                .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testUpdateCustomerModel() {
        JsonObject json = Json.createObjectBuilder()
                .add("userId", "yourUserIdHere")
                .add("registerDate", "2023-10-26T00:00:00")  // Adjust with the date you want
                .build();

        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .put("http://localhost:7000/api/payment/v1/customer/{id}", "yourCustomerIdHere")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testDeleteCustomerModel() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete("http://localhost:7000/api/payment/v1/customer/{id}", "yourCustomerIdHere")
                .then()
                .statusCode(200);
    }
}
