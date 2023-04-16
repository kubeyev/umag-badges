package com.badgers.umag.modules.pointers.service;

import com.badgers.umag.modules.pointers.models.entities.Pointer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PointerService {
    void createOrUpdateAll();

    Optional<Pointer> getByStartRange(LocalDateTime time, Long code, Long sum);

}
