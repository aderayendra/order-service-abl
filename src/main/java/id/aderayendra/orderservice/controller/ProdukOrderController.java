package id.aderayendra.orderservice.controller;

import id.aderayendra.orderservice.dto.ProdukOrderResponse;
import id.aderayendra.orderservice.model.ProdukOrder;
import id.aderayendra.orderservice.service.ProdukOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ProdukOrderController {

    private final ProdukOrderService service;

    @GetMapping
    public List<ProdukOrderResponse> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdukOrderResponse> getOrderById(@PathVariable Integer id) {
        return service.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public List<ProdukOrderResponse> getOrdersByProductId(@PathVariable String productId) {
        return service.getOrdersByProductId(productId);
    }

    @PostMapping
    public ProdukOrder createOrder(@RequestBody ProdukOrder order) {
        return service.createOrder(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdukOrder> updateOrder(@PathVariable Integer id, @RequestBody ProdukOrder orderDetails) {
        try {
            return ResponseEntity.ok(service.updateOrder(id, orderDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
