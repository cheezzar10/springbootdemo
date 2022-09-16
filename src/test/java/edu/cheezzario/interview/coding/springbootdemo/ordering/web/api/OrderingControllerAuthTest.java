package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import edu.cheezzario.interview.coding.springbootdemo.security.SecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderingControllerAuthTest {
    private TestRestTemplate client;

    @Autowired
    public void setTestRestTemplate(TestRestTemplate client) {
        this.client = client
                .withBasicAuth(SecurityConfig.TEST_USER_NAME, SecurityConfig.TEST_USER_PASSWORD);
    }

    @Test
    public void testInstantCheckoutAndPlacedOrderLoading() throws Exception {
        Order newOrder = new Order();
        newOrder.setCustomerName("Blind Pew");
        newOrder.setCustomerPhone("222");
        newOrder.setCustomerEmail("pew@hispaniola.uk");
        newOrder.setCustomerAddress("Treasure Island");
        newOrder.setProductSku("PPP-333");

        ResponseEntity<CheckoutResult> instantCheckoutResponse = client.postForEntity(
                "/ordering/instant-checkout",
                newOrder,
                CheckoutResult.class);
        Assertions.assertEquals(HttpStatus.OK, instantCheckoutResponse.getStatusCode());

        Order placedOrder = instantCheckoutResponse.getBody().getPlacedOrder();
        Assertions.assertTrue(placedOrder.getOrderId() != 0);

        ResponseEntity<Order[]> getOrdersResponse = client.getForEntity("/ordering/orders", Order[].class);
        Assertions.assertEquals(HttpStatus.OK, getOrdersResponse.getStatusCode());
        Assertions.assertEquals(1, getOrdersResponse.getBody().length);

        ResponseEntity<Order> getOrderResponse = client.getForEntity(
                "/ordering/order/" + placedOrder.getOrderId(),
                Order.class);

        Assertions.assertEquals(HttpStatus.OK, getOrderResponse.getStatusCode());
        Assertions.assertEquals(placedOrder.getOrderId(), getOrderResponse.getBody().getOrderId());
    }

    @Test
    public void testGetUnknownOrder() throws Exception {
        ResponseEntity<Order> response = client.getForEntity("/ordering/orders/9999999", Order.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
