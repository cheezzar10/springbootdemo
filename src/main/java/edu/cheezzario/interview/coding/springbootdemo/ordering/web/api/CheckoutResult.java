package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutResult {
    private Order placedOrder;

    private String errorMessage;
}
