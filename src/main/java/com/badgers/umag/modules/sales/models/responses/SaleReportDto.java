package com.badgers.umag.modules.sales.models.responses;

import java.time.LocalDateTime;

public interface SaleReportDto {

    Long getId();
    Long getBarcode();
    Integer getQuantity();
    Integer getPrice();
    LocalDateTime getTime();
    Integer getMargin();
    LocalDateTime getMarginDate();
    Long getBeforesum();
}
