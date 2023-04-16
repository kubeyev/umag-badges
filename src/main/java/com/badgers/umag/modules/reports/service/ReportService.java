package com.badgers.umag.modules.reports.service;

import com.badgers.umag.modules.reports.models.responces.ReportDto;

import java.time.LocalDateTime;

public interface ReportService {
    ReportDto getReports(Long barcode, LocalDateTime toTime, LocalDateTime fromTime);
}
