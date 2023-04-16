package com.badgers.umag.modules.reports.controllers;

import com.badgers.umag.modules.reports.models.responces.ReportDto;
import com.badgers.umag.modules.reports.service.ReportService;
import com.badgers.umag.modules.sales.models.responses.SaleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    @GetMapping
    public ReportDto getAll(
            @RequestParam Long barcode,
            @RequestParam(name = "toTime") @Valid @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toTime,
            @RequestParam(name = "fromTime") @Valid @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromTime) {
        return service.getReports(barcode, toTime, fromTime);
    }
}
