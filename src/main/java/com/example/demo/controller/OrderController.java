package com.example.demo.controller;


import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // 1. Create Order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }
    
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 2. Read Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Update Order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setOrderDate(orderDetails.getOrderDate());
                    order.setAmount(orderDetails.getAmount());
                    return ResponseEntity.ok(orderRepository.save(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Get All Orders with Pagination
    @GetMapping("/paginated")
    public Page<Order> getOrdersPaginated(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    // 6. Get All Orders with Sorting
    @GetMapping("/sorted")
    public Iterable<Order> getOrdersSorted(@RequestParam String sortBy, @RequestParam String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return orderRepository.findAll(sort);
    }

    // 7. Get All Orders with Pagination and Sorting
    @GetMapping("/paginated-sorted")
    public Page<Order> getOrdersPaginatedAndSorted(@RequestParam int page, @RequestParam int size,
                                                   @RequestParam String sortBy, @RequestParam String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable);
    }
}