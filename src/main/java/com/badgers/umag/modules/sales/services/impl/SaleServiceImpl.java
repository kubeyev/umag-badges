package com.badgers.umag.modules.sales.services.impl;

import com.badgers.umag.core.amq.MQSender;
import com.badgers.umag.modules.pointers.service.PointerService;
import com.badgers.umag.modules.reports.models.responces.ReportDto;
import com.badgers.umag.modules.sales.models.entites.Sale;
import com.badgers.umag.modules.sales.models.requests.SaleRequest;
import com.badgers.umag.modules.sales.models.responses.SaleReportDto;
import com.badgers.umag.modules.supplies.models.requests.RecalculateRequest;
import com.badgers.umag.modules.supplies.services.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.badgers.umag.core.services.Exceptional;
import com.badgers.umag.modules.sales.services.SaleService;
import com.badgers.umag.modules.sales.repositories.SaleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.badgers.umag.core.constants.Messages.ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService, Exceptional {

    private final MQSender sender;
    private final SaleRepository repository;
    private final SupplyService supplyService;
    private final PointerService pointerService;

    @Override
    public Long save(SaleRequest request) {
        var sale = repository.save(Sale.builder()
                .barcode(request.getBarcode())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .time(request.getSaleTime())
                .build());

        sender.send(RecalculateRequest.builder()
                .barcode(sale.getBarcode())
                .startDate(sale.getTime().toString())
                .build());

        return sale.getId();
    }

    @Override
    public List<Sale> getAll(Long barcode, LocalDateTime toTime, LocalDateTime fromTime) {
        return repository.getAll(barcode, toTime, fromTime);
    }

    @Override
    public Sale getById(Long id) {
        return repository.findById(id).orElseThrow(() -> notFound(ITEM_NOT_FOUND));
    }

    @Override
    public void delete(Long id) {
        var sale = getById(id);

        sender.send(RecalculateRequest.builder()
                .barcode(sale.getBarcode())
                .startDate(sale.getTime().toString())
                .build());

        repository.deleteById(id);
    }

    @Override
    public Sale update(Long id, SaleRequest request) {
        Sale existingSale = getById(id);

        var oldDate = existingSale.getTime();

        existingSale.setBarcode(request.getBarcode());
        existingSale.setQuantity(request.getQuantity());
        existingSale.setPrice(request.getPrice());
        existingSale.setTime(request.getSaleTime());

        var sale = repository.save(existingSale);

        sender.send(RecalculateRequest.builder()
                .barcode(existingSale.getBarcode())
                .startDate(oldDate.isAfter(sale.getTime()) ? sale.getTime().toString() : oldDate.toString())
                .build());

        return sale;
    }

    @Override
    public Long getQuantityOfSalesBefore(Sale sale) {
        return repository.getQuantityOfSalesBefore(sale.getTime());
    }

    @Override
    public Long countAll() {
        return repository.count();
    }

    @Override
    public void recalculateAllSalesByBarcode(LocalDateTime time, Long barcode) {
        var sales = repository.getReportSalesData(time, barcode);
        recalculateALlSalesByBarCode(sales);
    }

    private void recalculateALlSalesByBarCode(List<SaleReportDto> sales) {
        List<Sale> results = new ArrayList<>();

        for (var sale : sales) {
            var margin = recalculateAndGetMargin(sale);

            var s = new Sale();
            s.setId(sale.getId());
            s.setBarcode(sale.getBarcode());
            s.setQuantity(sale.getQuantity());
            s.setPrice(sale.getPrice());
            s.setTime(sale.getTime());
            s.setMargin(margin);
            s.setMarginTime(LocalDateTime.now());
            results.add(s);
        }

        repository.saveAll(results);
    }

    public Integer recalculateAndGetMargin(SaleReportDto sale) {
        var pointerOpt = pointerService.getByStartRange(sale.getTime(), sale.getBarcode(), sale.getBeforesum() + sale.getQuantity());
        int margin = 0;
        if (pointerOpt.isPresent()) {
            var pointer = pointerOpt.get();
            var overallItems = sale.getBeforesum() + sale.getQuantity();
            var lowerSupply = supplyService.getLatestSupply(
                    sale.getBeforesum(), sale.getBarcode(), sale.getTime(), pointer.getStartValue());
            var upperSupply = supplyService.getUpperSupply(
                    overallItems, sale.getBarcode(), sale.getTime(), pointer.getStartValue());
            var supplies = supplyService.getBetween(
                    lowerSupply.getId(), upperSupply.getId(), pointer.getBarcode());

            var supplyPreviousSum = lowerSupply.getBeforeSum() - lowerSupply.getQuantity();
            var num = (int) (lowerSupply.getBeforeSum() - supplyPreviousSum);
            var temp = sale.getQuantity();

            for (var supply : supplies) {
                var cur = supply.getQuantity() - num;
                if (cur >= temp) {
                    margin = margin + (sale.getPrice() * temp - temp * supply.getPrice());
                    break;
                } else {
                    temp = temp - cur;
                    margin = margin + (sale.getPrice() * cur - cur * supply.getPrice());
                    num = 0;
                }
            }
        } else {
            throw bad("Please, wait until application is fully set up!");
        }

        return margin;
    }

    @Override
    public Integer getMargins(Long barcode, LocalDateTime toTime, LocalDateTime fromTime) {
        return repository.getMargin(barcode, toTime, fromTime);
    }

    @Async
    @Override
    public void recalculateAllSales() {
        var barcodes = repository.getBarcodes();

        for (var code : barcodes) {
            var sales = repository.getReportSalesData(code);
            recalculateALlSalesByBarCode(sales);
        }
    }

    @Override
    public ReportDto getReports(Long barcode, LocalDateTime toTime, LocalDateTime fromTime) {
        var data = repository.getReportInfromation(barcode, fromTime, toTime);
        return ReportDto.builder()
                .barcode(data.getBarcode())
                .netProfit(data.getNetprofit())
                .revenue(data.getRevenue())
                .quantity(data.getQuantity())
                .build();
    }
}
