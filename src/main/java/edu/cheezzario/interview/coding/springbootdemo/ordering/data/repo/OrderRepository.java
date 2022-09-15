package edu.cheezzario.interview.coding.springbootdemo.ordering.data.repo;

import edu.cheezzario.interview.coding.springbootdemo.ordering.data.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
