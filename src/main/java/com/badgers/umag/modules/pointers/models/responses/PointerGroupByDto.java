package com.badgers.umag.modules.pointers.models.responses;

import java.time.LocalDateTime;

public interface PointerGroupByDto {
    Long getBarcode();
    LocalDateTime getMonth();
    Long getSum();
}
