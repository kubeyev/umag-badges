package com.badgers.umag.modules.pointers.service.impl;

import com.badgers.umag.modules.pointers.models.entities.Pointer;
import com.badgers.umag.modules.pointers.repositories.PointerRepository;
import com.badgers.umag.modules.pointers.service.PointerService;
import com.badgers.umag.modules.sales.services.SaleService;
import com.badgers.umag.modules.supplies.services.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointerServiceImpl implements PointerService {

    private final SupplyService supplyService;
    private final PointerRepository repository;

    @Override
    public void createOrUpdateAll() {
        var starterData = supplyService.getPointerInformation();
        var pointers = repository.findAll();
        var pointersMap = pointers.stream().collect(
                Collectors.toMap(e -> e.getBarcode() + e.getEndValue().toString(), Function.identity()));

        List<Pointer> result = new ArrayList<>();

        for (var data : starterData) {
            if (pointersMap.containsKey(data.getBarcode() + data.getMonth().toString())) {
                var pointer = pointersMap.get(data.getBarcode() + data.getMonth().toString());
                pointer.setSum(data.getSum().intValue());

                result.add(pointer);
            } else {
                result.add(Pointer.builder()
                                .barcode(data.getBarcode())
                                .endValue(data.getMonth())
                                .startValue(data.getMonth().minusMonths(1L))
                                .sum(data.getSum().intValue())
                                .build());
            }
        }

        repository.saveAll(result);
    }

    @Override
    public Optional<Pointer> getByStartRange(LocalDateTime time, Long code, Long sum) {
        return repository.getPointerByDate(time, code, sum);
    }
}
