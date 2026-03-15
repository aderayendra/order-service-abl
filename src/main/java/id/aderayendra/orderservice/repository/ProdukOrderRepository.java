package id.aderayendra.orderservice.repository;

import id.aderayendra.orderservice.model.ProdukOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdukOrderRepository extends JpaRepository<ProdukOrder, Integer> {
}
