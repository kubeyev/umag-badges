package com.badgers.umag.modules.sales.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.badgers.umag.modules.sales.mappers.SaleMapper;
import com.badgers.umag.modules.sales.services.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import com.badgers.umag.modules.sales.models.responses.SaleDto;
import com.badgers.umag.modules.sales.models.requests.SaleRequest;

import java.util.List;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleMapper mapper;
    private final SaleService service;

    @PostMapping
    public Long save(@RequestBody @Valid SaleRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<SaleDto> getAll(
            @RequestParam Long barcode,
            @RequestParam(name = "toTime") @Valid @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toTime,
            @RequestParam(name = "fromTime") @Valid @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromTime) {
        return service.getAll(barcode, toTime, fromTime).stream().map(mapper::map).collect(Collectors.toList());
    }

    @DeleteMapping("/{saleId}")
    public void delete(@PathVariable("saleId") Long id) {
        service.delete(id);
    }

    @PutMapping("/{saleId}")
    public SaleDto update(@PathVariable("saleId") Long id, @RequestBody @Valid SaleRequest request) {
        return mapper.map(service.update(id, request));
    }

    @GetMapping("/{saleId}")
    public SaleDto getById(@PathVariable("saleId") Long id) {
        return mapper.map(service.getById(id));
    }
}
