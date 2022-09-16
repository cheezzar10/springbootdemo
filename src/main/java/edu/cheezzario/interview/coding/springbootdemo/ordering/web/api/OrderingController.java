package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import edu.cheezzario.interview.coding.springbootdemo.ordering.data.repo.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path="/ordering", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderingController {
    private static final Logger log = LoggerFactory.getLogger(OrderingController.class);

    private OrderRepository orderRepo;

    public OrderingController(OrderRepository orderRepo) {
        log.info("ordering controller instance created");

        this.orderRepo = orderRepo;
    }

    @PostMapping(path = "/instant-checkout", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CheckoutResult> instantCheckout(@Valid @RequestBody Order order, BindingResult validation) {
        log.info("processing instant checkout request: {}", order);

        if (validation.hasErrors()) {
            String errorMessage = validation.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining("\n"));
            return new ResponseEntity<>(new CheckoutResult(null, errorMessage), HttpStatus.BAD_REQUEST);
        }

        Order placedOrder = saveOrder(order);
        return new ResponseEntity<>(new CheckoutResult(placedOrder, null), HttpStatus.OK);
    }

    private Order saveOrder(Order order) {
        var orderEntity = new edu.cheezzario.interview.coding.springbootdemo.ordering.data.model.Order();

        orderEntity.setCustomerName(order.getCustomerName());
        orderEntity.setCustomerPhone(order.getCustomerPhone());
        orderEntity.setCustomerEmail(order.getCustomerEmail());
        orderEntity.setCustomerAddress(order.getCustomerAddress());
        orderEntity.setProductSku(order.getProductSku());

        orderRepo.save(orderEntity);

        return orderEntityToWebOrder(orderEntity);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/orders")
    public List<Order> orders() {
        log.info("retrieving all orders");

        return StreamSupport
                .stream(orderRepo.findAll().spliterator(), false)
                .map(this::orderEntityToWebOrder)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> order(@PathVariable("id") Long id) {
        log.info("retrieving order with id: {}", id);

        return orderRepo.findById(id)
                .map(this::orderEntityToWebOrder)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    private Order orderEntityToWebOrder(edu.cheezzario.interview.coding.springbootdemo.ordering.data.model.Order orderEntity) {
        return new Order(
                orderEntity.getId(),
                orderEntity.getCustomerName(),
                orderEntity.getCustomerPhone(),
                orderEntity.getCustomerEmail(),
                orderEntity.getCustomerAddress(),
                orderEntity.getProductSku());
    }
}
