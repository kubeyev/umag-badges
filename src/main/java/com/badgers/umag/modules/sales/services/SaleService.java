package com.badgers.umag.modules.sales.services;

import com.badgers.umag.modules.reports.models.responces.ReportDto;
import com.badgers.umag.modules.sales.models.entites.Sale;
import com.badgers.umag.modules.sales.models.requests.SaleRequest;
import com.badgers.umag.modules.sales.models.responses.SaleReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    Long save(SaleRequest request);

    List<Sale> getAll(Long barcode, LocalDateTime toTime, LocalDateTime fromTime);

    Sale update(Long id, SaleRequest request);

    Sale getById(Long id);

    void delete(Long id);

    Long getQuantityOfSalesBefore(Sale sale);

    Long countAll();

    void recalculateAllSalesByBarcode(LocalDateTime time, Long barcode);

    Integer recalculateAndGetMargin(SaleReportDto sale);

    Integer getMargins(Long barcode, LocalDateTime toTime, LocalDateTime fromTime);

    void recalculateAllSales();

    ReportDto getReports(Long barcode, LocalDateTime toTime, LocalDateTime fromTime);
}
