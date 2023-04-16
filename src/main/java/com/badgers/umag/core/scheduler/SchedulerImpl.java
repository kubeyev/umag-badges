package com.badgers.umag.core.scheduler;

import com.badgers.umag.modules.pointers.service.PointerService;
import com.badgers.umag.modules.sales.services.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerImpl implements Scheduler {

    private final SaleService saleService;
    private final PointerService service;

    @Override
    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void updateSupplyPointers() {
        service.createOrUpdateAll();
    }

    @Override
    @Scheduled(fixedDelay = 300, timeUnit = TimeUnit.SECONDS)
    public void updateAllMargins() {
        saleService.recalculateAllSales();
    }
}
