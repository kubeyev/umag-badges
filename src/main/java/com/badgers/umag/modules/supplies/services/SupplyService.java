package com.badgers.umag.modules.supplies.services;

import com.badgers.umag.modules.pointers.models.responses.PointerGroupByDto;
import com.badgers.umag.modules.supplies.models.entities.Supply;
import com.badgers.umag.modules.supplies.models.requests.SupplyRequest;
import com.badgers.umag.modules.supplies.models.responses.SupplyReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SupplyService {
    Long save(SupplyRequest request);

    List<Supply> getAll(Long barcode, LocalDateTime toTime, LocalDateTime fromTime);

    Supply update(Long id, SupplyRequest request);

    Supply getById(Long id);

    void delete(Long id);

    Long countAll();

    List<PointerGroupByDto> getPointerInformation();

    SupplyReportDto getLatestSupply(Long beforeSum, Long barcode, LocalDateTime time, LocalDateTime startValue);

    SupplyReportDto getUpperSupply(long l, Long barcode, LocalDateTime time, LocalDateTime endValue);

    List<Supply> getBetween(Long first, Long second, Long barcode);
}
