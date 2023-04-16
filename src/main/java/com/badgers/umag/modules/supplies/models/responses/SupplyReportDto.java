package com.badgers.umag.modules.supplies.models.responses;

import java.time.LocalDateTime;

public interface SupplyReportDto {
    Long getId();
    Long getBarcode();
    Integer getQuantity();
    Integer getPrice();
    LocalDateTime getTime();
    Long getBeforeSum();
}
