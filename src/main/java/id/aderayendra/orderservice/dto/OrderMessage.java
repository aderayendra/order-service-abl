package id.aderayendra.orderservice.dto;

import id.aderayendra.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Order order;
    private String status;
    private ProdukDTO product;
    private String email;
}
