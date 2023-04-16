package com.badgers.umag.modules.pointers.repositories;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import com.badgers.umag.modules.pointers.models.entities.Pointer;
import com.badgers.umag.modules.pointers.models.responses.PointerGroupByDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointerRepository extends JpaRepository<Pointer, Long> {
    @Query(value = "select * from pointer p where " +
            "p.start_value <= :time and p.sum >= :sum and p.barcode = :code limit 1", nativeQuery = true)
    Optional<Pointer> getPointerByDate(@Param("time") LocalDateTime time, @Param("code") Long code,
                                       @Param("sum") Long sum);
}
