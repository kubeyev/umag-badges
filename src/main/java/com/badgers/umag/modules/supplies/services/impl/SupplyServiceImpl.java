package com.badgers.umag.modules.supplies.services.impl;

import com.badgers.umag.core.amq.MQSender;
import com.badgers.umag.modules.pointers.models.responses.PointerGroupByDto;
import com.badgers.umag.modules.supplies.models.requests.RecalculateRequest;
import com.badgers.umag.modules.supplies.models.responses.SupplyReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.badgers.umag.core.services.Exceptional;
import com.badgers.umag.modules.supplies.services.SupplyService;
import com.badgers.umag.modules.supplies.models.entities.Supply;
import com.badgers.umag.modules.supplies.repositories.SupplyRepository;
import com.badgers.umag.modules.supplies.models.requests.SupplyRequest;

import java.time.LocalDateTime;
import java.util.List;

import static com.badgers.umag.core.constants.Messages.ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService, Exceptional {

    private final MQSender sender;
    private final SupplyRepository repository;

    @Override
    public Long save(SupplyRequest request) {
        var supply = repository.save(Supply.builder()
                .barcode(request.getBarcode())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .time(request.getSupplyTime())
                .build());

        sender.send(RecalculateRequest.builder()
                .barcode(supply.getBarcode())
                .startDate(supply.getTime().toString())
                .build());

        return supply.getId();
    }

    @Override
    public List<Supply> getAll(Long barcode, LocalDateTime toTime, LocalDateTime fromTime) {
        var result = repository.getAll(barcode, toTime, fromTime);
        return result;
    }

    @Override
    public Supply getById(Long id) {
        return repository.findById(id).orElseThrow(() -> notFound(ITEM_NOT_FOUND));
    }

    @Override
    public void delete(Long id) {
        var supply = getById(id);

        sender.send(RecalculateRequest.builder()
                .barcode(supply.getBarcode())
                .startDate(supply.getTime().toString())
                .build());

        repository.deleteById(id);
    }

    @Override
    public Long countAll() {
        return repository.count();
    }

    @Override
    public List<PointerGroupByDto> getPointerInformation() {
        return repository.getPointerInformation();
    }

    @Override
    public Supply update(Long id, SupplyRequest request) {
        Supply existingSupply = getById(id);

        var oldDate = existingSupply.getTime();

        existingSupply.setBarcode(request.getBarcode());
        existingSupply.setQuantity(request.getQuantity());
        existingSupply.setPrice(request.getPrice());
        existingSupply.setTime(request.getSupplyTime());

        var supply = repository.save(existingSupply);

        sender.send(RecalculateRequest.builder()
                .barcode(existingSupply.getBarcode())
                .startDate(oldDate.isAfter(supply.getTime()) ? supply.getTime().toString() : oldDate.toString())
                .build());

        return supply;
    }

    @Override
    public SupplyReportDto getLatestSupply(Long beforeSum, Long barcode, LocalDateTime time, LocalDateTime startValue) {
        return repository.getLatestSupply(beforeSum, barcode, time, startValue);
    }

    @Override
    public SupplyReportDto getUpperSupply(long l, Long barcode, LocalDateTime time, LocalDateTime endValue) {
        return repository.getUpperSupply(l, barcode, time, endValue);
    }

    @Override
    public List<Supply> getBetween(Long first, Long second, Long barcode) {
        return repository.getSuppliesBetween(first, second, barcode);
    }
}
