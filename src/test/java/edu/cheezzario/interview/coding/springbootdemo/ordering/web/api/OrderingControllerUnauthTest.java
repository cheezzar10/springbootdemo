package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//@AutoConfigureMockMvc
public class OrderingControllerUnauthTest {
//    private MockMvc mockMvc;

//    @Autowired
//    private WebTestClient client;

    @Autowired
    // .withBasicAuth()
    private TestRestTemplate client;

    /*
    @Test
    public void testInstantCheckout() throws Exception {
        mockMvc.perform(
                post("/ordering/instant-checkout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
     */

   /*
    @Test
    public void testInstantCheckout() throws Exception {
        Order order = new Order();
        order.setCustomerName("Billy Bones");

        client
                .post()
                .uri("/ordering/instant-checkout")
                .bodyValue(order)
                .exchange()
                .expectStatus()
                .isOk();

    }
    */

    @Test
    public void testFindAllOrders() throws Exception {
        ResponseEntity<String> response = client.getForEntity("/ordering/orders", String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
