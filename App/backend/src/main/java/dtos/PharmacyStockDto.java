package dtos;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PharmacyStockDto {
    private Long id;
    private Long pharmacyId;
    private Long drugId;
    private Integer quantity;
    private BigDecimal price;
    private Integer reservationDelayMinutes;
}