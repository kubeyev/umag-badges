package com.badgers.umag.modules.supplies.controllers;

import com.badgers.umag.modules.supplies.mappers.SupplyMapper;
import com.badgers.umag.modules.supplies.models.requests.SupplyRequest;
import com.badgers.umag.modules.supplies.models.responses.SupplyDto;
import com.badgers.umag.modules.supplies.services.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplies")
public class SupplyController {

    private final SupplyMapper mapper;
    private final SupplyService service;

    @PostMapping
    public Long save(@RequestBody @Valid SupplyRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<SupplyDto> getAll(
            @RequestParam Long barcode,
            @RequestParam(name = "toTime") @Valid @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toTime,
            @RequestParam(name = "fromTime") @Valid @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromTime) {
        return service.getAll(barcode, toTime, fromTime).stream().map(mapper::map).collect(Collectors.toList());
    }

    @DeleteMapping("/{supplyId}")
    public void delete(@PathVariable("supplyId") Long id) {
        service.delete(id);
    }

    @PutMapping("/{supplyId}")
    public SupplyDto update(@PathVariable("supplyId") Long id, @RequestBody @Valid SupplyRequest request) {
        return mapper.map(service.update(id, request));
    }

    @GetMapping("/{supplyId}")
    public SupplyDto getById(@PathVariable("supplyId") Long id) {
        return mapper.map(service.getById(id));
    }
}
