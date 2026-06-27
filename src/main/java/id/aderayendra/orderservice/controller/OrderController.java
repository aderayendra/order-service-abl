package id.aderayendra.orderservice.controller;

import id.aderayendra.orderservice.dto.OrderResponse;
import id.aderayendra.orderservice.model.Order;
import id.aderayendra.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal String username) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getCredentials();
        return service.getOrdersByUserId(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        return service.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Order> createOrder(@RequestBody Order order, @AuthenticationPrincipal String username) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getCredentials();
        return ResponseEntity.ok(service.createOrder(order, userId, username));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderDetails, @AuthenticationPrincipal String username) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getCredentials();
        return ResponseEntity.ok(service.updateOrder(id, orderDetails, userId, username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id, @AuthenticationPrincipal String username) {
        Long userId = (Long) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getCredentials();
        service.deleteOrder(id, userId, username);
        return ResponseEntity.noContent().build();
    }
}
