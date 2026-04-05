package id.aderayendra.orderservice.service;

import id.aderayendra.orderservice.dto.ProdukDTO;
import id.aderayendra.orderservice.dto.ProdukOrderResponse;
import id.aderayendra.orderservice.model.ProdukOrder;
import id.aderayendra.orderservice.repository.ProdukOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdukOrderService {

    private final ProdukOrderRepository repository;
    private final RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    public List<ProdukOrderResponse> getAllOrders() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProdukOrderResponse> getOrderById(Integer id) {
        return repository.findById(id).map(this::mapToResponse);
    }

    public List<ProdukOrderResponse> getOrdersByProductId(String productId) {
        return repository.findByProdukId(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProdukOrder createOrder(ProdukOrder order) {
        validateProductExists(order.getProdukId());
        return repository.save(order);
    }

    public ProdukOrder updateOrder(Integer id, ProdukOrder orderDetails) {
        ProdukOrder order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        validateProductExists(orderDetails.getProdukId());

        order.setProdukId(orderDetails.getProdukId());
        order.setJumlah(orderDetails.getJumlah());
        order.setTanggal(orderDetails.getTanggal());
        order.setTotal(orderDetails.getTotal());

        return repository.save(order);
    }

    private void validateProductExists(String productId) {
        try {
            restTemplate.getForObject(productServiceUrl + "/" + productId, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    public void deleteOrder(Integer id) {
        repository.deleteById(id);
    }

    private ProdukOrderResponse mapToResponse(ProdukOrder order) {
        ProdukDTO produk = null;
        try {
            produk = restTemplate.getForObject(productServiceUrl + "/" + order.getProdukId(), ProdukDTO.class);
        } catch (Exception e) {
            // Log error or handle cases where product service is down
            System.err.println("Failed to fetch product details for ID: " + order.getProdukId() + ". Error: " + e.getMessage());
        }

        return ProdukOrderResponse.builder()
                .id(order.getId())
                .produkId(order.getProdukId())
                .jumlah(order.getJumlah())
                .tanggal(order.getTanggal())
                .total(order.getTotal())
                .produk(produk)
                .build();
    }
}
