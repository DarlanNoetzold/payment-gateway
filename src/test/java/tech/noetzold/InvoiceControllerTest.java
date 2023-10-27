package tech.noetzold;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import io.quarkus.test.security.TestSecurity;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import tech.noetzold.controller.InvoiceController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(InvoiceController.class)
public class InvoiceControllerTest {

    private String accessToken;

    @BeforeEach
    public void obtainAccessToken() {
        final String username = "admin";
        final String password = "admin";

        final String tokenEndpoint = "http://localhost:8180/realms/quarkus1/protocol/openid-connect/token";

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
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testFindInvoiceModelById() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("http://localhost:7000/api/payment/v1/invoice/{id}", "12345678-1234-1234-1234-123456789018")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveInvoiceModel() {
        JsonObject json = Json.createObjectBuilder()
                .add("customer", Json.createObjectBuilder().add("customerId", "12345678-1234-1234-1234-123456789013"))
                .add("payment", Json.createObjectBuilder().add("paymentId", "12345678-1234-1234-1234-123456789017"))
                .add("invoiceNumber", "INV-12345")
                .add("invoiceDate", "2023-10-15T00:00:00Z")
                .add("totalAmount", 90.0)
                .add("discountAmount", 10.0)
                .add("sellerName", "Empresa Exemplo")
                .add("sellerAddress", "Rua Exemplo, 123")
                .add("buyerName", "Comprador Exemplo")
                .add("buyerAddress", "Avenida Exemplo, 456")
                .add("itemsId", Json.createArrayBuilder(Arrays.asList("item1", "item2", "item3"))
                        .build()).build();

        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("http://localhost:7000/api/payment/v1/invoice")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testUpdateInvoiceModel() {
        JsonObject json = Json.createObjectBuilder()
                .add("customer", Json.createObjectBuilder().add("customerId", "12345678-1234-1234-1234-123456789013"))
                .add("payment", Json.createObjectBuilder().add("paymentId", "12345678-1234-1234-1234-123456789017"))
                .add("invoiceNumber", "INV-12345")
                .add("invoiceDate", "2023-10-15T00:00:00Z")
                .add("totalAmount", 90.0)
                .add("discountAmount", 10.0)
                .add("sellerName", "Empresa Exemplo")
                .add("sellerAddress", "Rua Exemplo, 123")
                .add("buyerName", "Comprador Exemplo")
                .add("buyerAddress", "Avenida Exemplo, 456")
                .add("itemsId", Json.createArrayBuilder(Arrays.asList("item1", "item2", "item3"))
                        .build()).build();

        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .put("http://localhost:7000/api/payment/v1/invoice/{id}", "12345678-1234-1234-1234-123456789018")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testDeleteInvoiceModel() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete("http://localhost:7000/api/payment/v1/invoice/{id}", "12345678-1234-1234-1234-123456789024")
                .then()
                .statusCode(200);
    }

}
