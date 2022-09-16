package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderingControllerUnauthTest {
    @Autowired
    private TestRestTemplate client;

    @Test
    public void testFindAllOrdersAccessDenied() throws Exception {
        ResponseEntity<String> response = client.getForEntity("/ordering/orders", String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testInvalidInstantCheckoutDataRejection() {
        Order newOrder = new Order();
        newOrder.setCustomerPhone("222");
        newOrder.setCustomerEmail("foo");
        newOrder.setCustomerAddress("Treasure Island");
        newOrder.setProductSku("PPP-333");

        ResponseEntity<CheckoutResult> instantCheckoutResponse = client.postForEntity(
                "/ordering/instant-checkout",
                newOrder,
                CheckoutResult.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, instantCheckoutResponse.getStatusCode());

        String errorMessage = instantCheckoutResponse.getBody().getErrorMessage();
        Assertions.assertTrue(errorMessage.contains("Field error in object 'order' on field 'customerEmail'"));
    }
}
