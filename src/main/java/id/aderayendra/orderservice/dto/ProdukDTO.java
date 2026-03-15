package id.aderayendra.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdukDTO {
    private String id;
    private String nama;
    private String kategori;
    private Integer harga;
    private Integer stok;
}
