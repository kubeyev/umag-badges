package com.badgers.umag.modules.reports.service;

import com.badgers.umag.modules.reports.models.responces.ReportDto;
import com.badgers.umag.modules.sales.models.entites.Sale;
import com.badgers.umag.modules.sales.services.SaleService;
import com.badgers.umag.modules.supplies.repositories.SupplyRepository;
import com.badgers.umag.modules.supplies.services.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SaleService saleService;
    private final SupplyService supplyService;

    @Override
    public ReportDto getReports(Long barcode, LocalDateTime toTime, LocalDateTime fromTime) {
        return saleService.getReports(barcode, toTime, fromTime);
    }
}
