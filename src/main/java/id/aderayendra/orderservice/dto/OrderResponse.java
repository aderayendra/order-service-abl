package id.aderayendra.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Integer id;
    private String produkId;
    private Integer jumlah;
    private LocalDate tanggal;
    private Double total;
    private Long userId;
    private ProdukDTO produk;
}
