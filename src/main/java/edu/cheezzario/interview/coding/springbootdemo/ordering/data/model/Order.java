package edu.cheezzario.interview.coding.springbootdemo.ordering.data.model;

import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "Customer name field is mandatory")
    @Column(name = "customer_name", length = 128)
    private String customerName;

    @NotNull(message = "Customer phone number field is mandatory")
    @Column(name = "customer_phone", length = 32)
    private String customerPhone;

    @NotNull
    @Email(message = "Customer email field has invalid value")
    @Column(name = "customer_email", length = 64)
    private String customerEmail;

    @NotNull
    @Column(name = "customer_address", length = 256)
    private String customerAddress;

    @NotNull
    @Column(name = "product_sku", length = 64)
    private String productSku;

    public Order() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }
}
