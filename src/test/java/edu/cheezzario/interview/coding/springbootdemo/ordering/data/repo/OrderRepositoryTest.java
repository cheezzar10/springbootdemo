package edu.cheezzario.interview.coding.springbootdemo.ordering.data.repo;

import edu.cheezzario.interview.coding.springbootdemo.ordering.data.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testValidOrderCrud() {
        Order newOrder = new Order();
        newOrder.setCustomerName("Blind Pew");
        newOrder.setCustomerPhone("222");
        newOrder.setCustomerEmail("pew@hispaniola.uk");
        newOrder.setCustomerAddress("Treasure Island");
        newOrder.setProductSku("PPP-333");

        orderRepository.save(newOrder);
        Assertions.assertNotNull(newOrder.getId());

        List<Order> allOrders = StreamSupport
                .stream(orderRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Assertions.assertTrue(allOrders.size() == 1);

        Optional<Order> existingOrder = orderRepository.findById(newOrder.getId());
        Assertions.assertTrue(existingOrder.isPresent());
        Assertions.assertEquals(newOrder.getCustomerAddress(), existingOrder.get().getCustomerAddress());
    }

    @Test
    public void testInvalidOrderFailedValidation() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Order invalidOrder = new Order();
            invalidOrder.setCustomerName("John Silver");
            invalidOrder.setCustomerEmail("garbage");
            invalidOrder.setCustomerAddress("Treasure Island");

            orderRepository.save(invalidOrder);

            // forcing flush
            orderRepository.findAll();
        });
    }
}
