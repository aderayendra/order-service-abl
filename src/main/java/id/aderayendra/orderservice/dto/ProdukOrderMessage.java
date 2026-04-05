package id.aderayendra.orderservice.dto;

import id.aderayendra.orderservice.model.ProdukOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdukOrderMessage {
    private ProdukOrder order;
    private String status;
    private ProdukDTO product;
}
