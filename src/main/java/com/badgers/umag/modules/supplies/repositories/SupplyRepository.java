package com.badgers.umag.modules.supplies.repositories;

import com.badgers.umag.modules.pointers.models.responses.PointerGroupByDto;
import com.badgers.umag.modules.supplies.models.responses.SupplyReportDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.badgers.umag.modules.supplies.models.entities.Supply;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    @Query(value = "select * from supply s where s.barcode = :barcode " +
            "and s.time <= :toTime and s.time >= :fromTime", nativeQuery = true)
    List<Supply> getAll(
            @Param("barcode") Long barcode,
            @Param("toTime") LocalDateTime toTime,
            @Param("fromTime") LocalDateTime fromTime);

    @Query(value = "select s1.barcode, month, " +
            "    (select sum(quantity) from supply s where " +
            "         s.barcode = s1.barcode and s.time <= month) as sum " +
            "from supply s1, date_trunc('month', s1.time + INTERVAL '1 month') as month " +
            "group by barcode, month order by month desc", nativeQuery = true)
    List<PointerGroupByDto> getPointerInformation();

    @Query(value = "WITH cte AS ( " +
            "    SELECT s.*, " +
            "           SUM(quantity) OVER (PARTITION BY barcode ORDER BY time, id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW ) " +
            "               + COALESCE((select sum(quantity) from sale where time <= :time and barcode = :barcode), 0) AS beforesum " +
            "    from supply s " +
            "    where s.time >= :startValue and s.barcode = :barcode and s.time <= :time " +
            ") " +
            "SELECT * " +
            "FROM cte " +
            "WHERE beforesum >= :sum order by cte.time, cte.id desc limit 1", nativeQuery = true)
    SupplyReportDto getLatestSupply(
            @Param("sum") Long beforeSum,
            @Param("barcode") Long barcode,
            @Param("time") LocalDateTime time,
            @Param("startValue") LocalDateTime startValue);

    @Query(value = "WITH cte AS ( " +
            "    SELECT s.*, " +
            "           SUM(quantity) OVER (PARTITION BY barcode ORDER BY time, id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW ) " +
            "               + COALESCE((select sum(quantity) from sale where time <= :time and barcode = :barcode), 0) AS beforesum " +
            "    from supply s " +
            "    where s.time >= :startValue and s.barcode = :barcode and s.time <= :time " +
            ") " +
            "SELECT * " +
            "FROM cte " +
            "WHERE beforesum >= :sum order by cte.time, cte.id limit 1", nativeQuery = true)
    SupplyReportDto getUpperSupply(
            @Param("sum") Long sum,
            @Param("barcode") Long barcode,
            @Param("time") LocalDateTime time,
            @Param("startValue") LocalDateTime startValue);

    @Query(value = "SELECT * " +
            "FROM supply " +
            "WHERE barcode = :bar AND time >= ( " +
            "    SELECT time " +
            "    FROM supply " +
            "    WHERE id = :first " +
            ") AND time <= ( " +
            "    SELECT time " +
            "    FROM supply " +
            "    WHERE id = :second " +
            ") " +
            "ORDER BY time, id", nativeQuery = true)
    List<Supply> getSuppliesBetween(@Param("first") Long first, @Param("second") Long second, @Param("bar") Long bar);
}
