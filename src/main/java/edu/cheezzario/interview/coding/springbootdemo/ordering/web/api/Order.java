package edu.cheezzario.interview.coding.springbootdemo.ordering.web.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private long orderId;

    @NotNull(message = "Customer name field is mandatory")
    private String customerName;

    @NotNull(message = "Customer email field has invalid value")
    private String customerPhone;

    @NotNull
    @Email(message = "Customer email field has invalid value")
    private String customerEmail;

    @NotNull
    private String customerAddress;

    @NotNull
    private String productSku;
}
