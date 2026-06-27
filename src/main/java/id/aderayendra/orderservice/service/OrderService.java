package id.aderayendra.orderservice.service;

import id.aderayendra.orderservice.dto.ProdukDTO;
import id.aderayendra.orderservice.dto.OrderMessage;
import id.aderayendra.orderservice.dto.OrderResponse;
import id.aderayendra.orderservice.model.Order;
import id.aderayendra.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    @Value("${auth-service.url:http://auth-service/api/user}")
    private String authServiceUrl;

    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponse> getOrderById(Integer id) {
        return repository.findById(id).map(this::mapToResponse);
    }

    public Order createOrder(Order order, Long userId, String username) {
        validateProductExists(order.getProdukId());
        order.setUserId(userId);
        Order savedOrder = repository.save(order);
        sendOrderMessage(savedOrder, "CREATED", username);
        return savedOrder;
    }

    public Order updateOrder(Integer id, Order orderDetails, Long userId, String username) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this order");
        }

        validateProductExists(orderDetails.getProdukId());

        order.setProdukId(orderDetails.getProdukId());
        order.setJumlah(orderDetails.getJumlah());
        order.setTanggal(orderDetails.getTanggal());
        order.setTotal(orderDetails.getTotal());
        order.setUserId(userId);

        Order updatedOrder = repository.save(order);
        sendOrderMessage(updatedOrder, "UPDATED", username);
        return updatedOrder;
    }

    private void sendOrderMessage(Order order, String status, String username) {
        ProdukDTO product = null;
        try {
            product = restTemplate.getForObject(productServiceUrl + "/" + order.getProdukId(), ProdukDTO.class);
        } catch (Exception e) {
            System.err.println("Failed to fetch product details for messaging. ID: " + order.getProdukId() + ". Error: " + e.getMessage());
        }

        String userEmail = "ervan@pnp.ac.id"; // Fallback
        try {
            id.aderayendra.orderservice.dto.UserDTO user = restTemplate.getForObject(authServiceUrl + "/" + username, id.aderayendra.orderservice.dto.UserDTO.class);
            if (user != null && user.getEmail() != null) {
                userEmail = user.getEmail();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch user details for email. Username: " + username + ". Error: " + e.getMessage());
        }

        OrderMessage message = OrderMessage.builder()
                .order(order)
                .status(status)
                .product(product)
                .email(userEmail)
                .build();
        rabbitTemplate.convertAndSend("order-queue", message);
    }

    private void validateProductExists(String productId) {
        try {
            restTemplate.getForObject(productServiceUrl + "/" + productId, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    public void deleteOrder(Integer id, Long userId, String username) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this order");
        }

        repository.deleteById(id);
        sendOrderMessage(order, "DELETED", username);
    }

    private OrderResponse mapToResponse(Order order) {
        ProdukDTO produk = null;
        try {
            produk = restTemplate.getForObject(productServiceUrl + "/" + order.getProdukId(), ProdukDTO.class);
        } catch (Exception e) {
            // Log error or handle cases where product service is down
            System.err.println("Failed to fetch product details for ID: " + order.getProdukId() + ". Error: " + e.getMessage());
        }

        return OrderResponse.builder()
                .id(order.getId())
                .produkId(order.getProdukId())
                .jumlah(order.getJumlah())
                .tanggal(order.getTanggal())
                .total(order.getTotal())
                .userId(order.getUserId())
                .produk(produk)
                .build();
    }
}
