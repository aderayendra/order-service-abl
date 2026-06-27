package id.aderayendra.orderservice.repository;

import id.aderayendra.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByProdukId(String produkId);
    List<Order> findByUserId(Long userId);
}
