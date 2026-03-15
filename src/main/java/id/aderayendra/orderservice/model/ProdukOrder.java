package id.aderayendra.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "`produk-order`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdukOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "produk_id", nullable = false, length = 10)
    private String produkId;

    @Column(nullable = false)
    private Integer jumlah;

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(nullable = false)
    private Double total;
}
