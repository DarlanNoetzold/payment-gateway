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
import tech.noetzold.controller.PaymentController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PaymentController.class)
public class PaymentControllerTest {

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
    @Order(5)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testFindPaymentModelById() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("http://localhost:7000/api/payment/v1/payment/{id}", "e8f98317-d29a-453c-8557-47d3f26040d1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testFindPaymentModelByOrderId() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("http://localhost:7000/api/payment/v1/payment/order/{orderId}", "orderId")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testFindPaymentModelByUserId() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("http://localhost:7000/api/payment/v1/user/{userId}", "12345678-1234-1234-1234-123456789013")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSavePayment() {
        JsonObject json = Json.createObjectBuilder()
                .add("customer", Json.createObjectBuilder()
                        .add("customerId", "12345678-1234-1234-1234-123456789013")
                )
                .add("paymentMethod", "CREDIT_CARD")
                .add("hasErrors", false)
                .add("totalAmount", 100.50)
                .add("orderId", "orderId")
                .add("registerDate", "2023-10-06T15:30:00Z")
                .add("discountAmount", 10.00)
                .add("boletoModel", Json.createObjectBuilder()
                        .add("codigoDeBarras", "1234567890123456789012345678901234567890")
                        .add("linhaDigitavel", "12345.67890 12345.678901 23456.789012 3 45678901234567890")
                        .add("beneficiarioNome", "Empresa Beneficiária")
                        .add("beneficiarioCnpjCpf", "123.456.789-00")
                        .add("beneficiarioEndereco", "Rua da Empresa Beneficiária, 123")
                        .add("sacadoNome", "João Sacado")
                        .add("sacadoCnpjCpf", "987.654.321-00")
                        .add("sacadoEndereco", "Rua do João Sacado, 456")
                        .add("valor", 100.50)
                        .add("dataVencimento", "2023-10-20")
                        .add("numeroDocumento", "DOC123456789")
                        .add("instrucoesPagamento", "Pague até a data de vencimento")
                        .add("multa", 5.00)
                        .add("jurosAtraso", 2.50)
                        .add("logoBanco", "logo.png")
                        .add("codigoBanco", "001")
                        .add("identificacaoBoleto", "Boleto de Pagamento")
                        .add("dataEmissao", "2023-10-06")
                        .add("localPagamento", "Qualquer agência bancária")
                        .add("codigoAutenticacao", "ABC123")
                        .add("informacoesContato", "Fale conosco em contato@empresa.com")
                )
                .add("cardModel", Json.createObjectBuilder()
                        .add("cardNumber", "1234-5678-9012-3456")
                        .add("cardHolderName", "John Doe")
                        .add("expirationDate", "12/25")
                        .add("cvv", "123")
                        .add("cardType", "CREDIT")
                )
                .add("pixModel", Json.createObjectBuilder()
                        .add("chavePix", "1234567890")
                        .add("descricao", "Pagamento via PIX")
                        .add("identificadorTransacao", "PIX123")
                )
                .add("paypalModel", Json.createObjectBuilder()
                        .add("email", "john.doe@example.com")
                        .add("descricao", "Pagamento via PayPal")
                        .add("identificadorTransacao", "PAYPAL123")
                )
                .build();

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("http://localhost:7000/api/payment/v1/payment")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testUpdatePayment() {
        JsonObject json = Json.createObjectBuilder()
                .add("customer", Json.createObjectBuilder()
                        .add("customerId", "12345678-1234-1234-1234-123456789013")
                )
                .add("paymentMethod", "CREDIT_CARD")
                .add("hasErrors", false)
                .add("totalAmount", 100.50)
                .add("orderId", "orderId")
                .add("registerDate", "2023-10-06T15:30:00Z")
                .add("discountAmount", 10.00)
                .add("boletoModel", Json.createObjectBuilder()
                        .add("codigoDeBarras", "1234567890123456789012345678901234567890")
                        .add("linhaDigitavel", "12345.67890 12345.678901 23456.789012 3 45678901234567890")
                        .add("beneficiarioNome", "Empresa Beneficiária")
                        .add("beneficiarioCnpjCpf", "123.456.789-00")
                        .add("beneficiarioEndereco", "Rua da Empresa Beneficiária, 123")
                        .add("sacadoNome", "João Sacado")
                        .add("sacadoCnpjCpf", "987.654.321-00")
                        .add("sacadoEndereco", "Rua do João Sacado, 456")
                        .add("valor", 100.50)
                        .add("dataVencimento", "2023-10-20")
                        .add("numeroDocumento", "DOC123456789")
                        .add("instrucoesPagamento", "Pague até a data de vencimento")
                        .add("multa", 5.00)
                        .add("jurosAtraso", 2.50)
                        .add("logoBanco", "logo.png")
                        .add("codigoBanco", "001")
                        .add("identificacaoBoleto", "Boleto de Pagamento")
                        .add("dataEmissao", "2023-10-06")
                        .add("localPagamento", "Qualquer agência bancária")
                        .add("codigoAutenticacao", "ABC123")
                        .add("informacoesContato", "Fale conosco em contato@empresa.com")
                )
                .add("cardModel", Json.createObjectBuilder()
                        .add("cardId", "12345678-1234-1234-1234-123456789014")
                        .add("cardNumber", "1234-5678-9012-3456")
                        .add("cardHolderName", "John Doe")
                        .add("expirationDate", "12/25")
                        .add("cvv", "123")
                        .add("cardType", "CREDIT")
                )
                .add("pixModel", Json.createObjectBuilder()
                        .add("chavePix", "1234567890")
                        .add("descricao", "Pagamento via PIX")
                        .add("identificadorTransacao", "PIX123")
                )
                .add("paypalModel", Json.createObjectBuilder()
                        .add("email", "john.doe@example.com")
                        .add("descricao", "Pagamento via PayPal")
                        .add("identificadorTransacao", "PAYPAL123")
                )
                .build();

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .put("http://localhost:7000/api/payment/v1/payment/{id}", "12345678-1234-1234-1234-123456789017")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(6)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testDeletePaymentModel() {

        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("http://localhost:7000/api/payment/v1/payment/{id}", "12345678-1234-1234-1234-123456789023")
                .then()
                .statusCode(202);
    }
}
