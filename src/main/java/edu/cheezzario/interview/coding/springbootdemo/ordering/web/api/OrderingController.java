package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import edu.cheezzario.interview.coding.springbootdemo.ordering.data.repo.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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
@Tag(name = "Order Management Controller", description = "Provides order management operations")
public class OrderingController {
    private static final Logger log = LoggerFactory.getLogger(OrderingController.class);

    private OrderRepository orderRepo;

    public OrderingController(OrderRepository orderRepo) {
        log.info("ordering controller instance created");

        this.orderRepo = orderRepo;
    }

    @PostMapping(path = "/instant-checkout", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Instant order checkout, user is not required to be signed-up")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instant checkout request processed successfully"),
            @ApiResponse(responseCode = "400", description = "Instant checkout request input data validation failed")
    })
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
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Listing all existing orders")
    public List<Order> orders() {
        log.info("retrieving all orders");

        return StreamSupport
                .stream(orderRepo.findAll().spliterator(), false)
                .map(this::orderEntityToWebOrder)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/order/{id}")
    @Operation(summary = "Loading order by order id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order loaded"),
            @ApiResponse(responseCode = "404", description = "Order with specified order id is not found")
    })
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
