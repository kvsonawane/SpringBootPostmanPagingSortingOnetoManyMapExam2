package com.example.demo.controller;


import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    // 1. Create Customer
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }
    
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    // 2. Read Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDetails.getFirstName());
                    customer.setLastName(customerDetails.getLastName());
                    customer.setEmail(customerDetails.getEmail());
                    return ResponseEntity.ok(customerRepository.save(customer));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Get All Customers with Pagination
    @GetMapping("/paginated")
    public Page<Customer> getCustomersPaginated(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.findAll(pageable);
    }

    // 6. Get All Customers with Sorting
    @GetMapping("/sorted")
    public Iterable<Customer> getCustomersSorted(@RequestParam String sortBy, @RequestParam String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return customerRepository.findAll(sort);
    }

    // 7. Get All Customers with Pagination and Sorting
    @GetMapping("/paginated-sorted")
    public Page<Customer> getCustomersPaginatedAndSorted(@RequestParam int page, @RequestParam int size,
                                                         @RequestParam String sortBy, @RequestParam String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerRepository.findAll(pageable);
    }
}