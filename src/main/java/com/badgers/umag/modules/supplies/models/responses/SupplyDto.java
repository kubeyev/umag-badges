package com.badgers.umag.modules.supplies.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyDto {
    private Long id;
    private Long barcode;
    private Integer price;
    private Integer quantity;
    private String supplyTime;
}
