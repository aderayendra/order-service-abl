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
                .map(order -> mapToResponse(order, false))
                .collect(Collectors.toList());
    }

    public Optional<ProdukOrderResponse> getOrderById(Integer id) {
        return repository.findById(id).map(order -> mapToResponse(order, false));
    }

    public List<ProdukOrderResponse> getOrdersByProductId(Integer productId) {
        return repository.findByProdukId(productId).stream()
                .map(order -> mapToResponse(order, false))
                .collect(Collectors.toList());
    }

    public List<ProdukOrderResponse> getOrdersByProductIdWithProduct(Integer productId) {
        return repository.findByProdukId(productId).stream()
                .map(order -> mapToResponse(order, true))
                .collect(Collectors.toList());
    }

    public ProdukOrder createOrder(ProdukOrder order) {
        return repository.save(order);
    }

    public ProdukOrder updateOrder(Integer id, ProdukOrder orderDetails) {
        ProdukOrder order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setProdukId(orderDetails.getProdukId());
        order.setJumlah(orderDetails.getJumlah());
        order.setTanggal(orderDetails.getTanggal());
        order.setTotal(orderDetails.getTotal());

        return repository.save(order);
    }

    public void deleteOrder(Integer id) {
        repository.deleteById(id);
    }

    private ProdukOrderResponse mapToResponse(ProdukOrder order, boolean includeProduct) {
        ProdukDTO produk = null;
        if (includeProduct) {
            try {
                produk = restTemplate.getForObject(productServiceUrl + "/" + order.getProdukId(), ProdukDTO.class);
            } catch (Exception e) {
                // Log error or handle cases where product service is down
                System.err.println("Failed to fetch product details for ID: " + order.getProdukId() + ". Error: " + e.getMessage());
            }
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
