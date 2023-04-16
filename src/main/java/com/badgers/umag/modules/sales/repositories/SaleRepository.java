package com.badgers.umag.modules.sales.repositories;

import com.badgers.umag.modules.reports.models.responces.ReportDto;
import com.badgers.umag.modules.reports.models.responces.ReportRawDto;
import com.badgers.umag.modules.sales.models.responses.SaleReportDto;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.badgers.umag.modules.sales.models.entites.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query(value = "select * from sale s where s.barcode = :barcode " +
            "and s.time <= :toTime and s.time >= :fromTime", nativeQuery = true)
    List<Sale> getAll(
            @Param("barcode") Long barcode,
            @Param("toTime") LocalDateTime toTime,
            @Param("fromTime") LocalDateTime fromTime);

    @Query(value = "select sum(quantity) from sale s where s.time <= :time", nativeQuery = true)
    Long getQuantityOfSalesBefore(@Param("time") LocalDateTime time);

    @Query(
            value = "SELECT s1.*, " +
                    "       SUM(quantity) OVER (PARTITION BY barcode ORDER BY time, id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW ) " +
                    "           + COALESCE((select sum(quantity) from sale where time <= :time and barcode = :barcode), 0) - s1.quantity AS beforesum " +
                    "FROM sale s1 " +
                    "WHERE time >= :time and barcode = :barcode", nativeQuery = true)
    List<SaleReportDto> getReportSalesData(
            @Param("time") LocalDateTime time,
            @Param("barcode") Long barcode);

    @Query(
            value = "SELECT s1.*, " +
                    "       COALESCE(SUM(quantity) OVER (PARTITION BY barcode ORDER BY time, id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW ), 0) " +
                    "           - s1.quantity AS beforesum " +
                    "FROM sale s1 " +
                    "WHERE barcode = :barcode", nativeQuery = true)
    List<SaleReportDto> getReportSalesData(
            @Param("barcode") Long barcode);

    @Query(value = "select sum(margin) " +
            "from sale " +
            "where time >= :fromTime and time <= :toTime and barcode = :barcode", nativeQuery = true)
    Integer getMargin(
            @Param("barcode") Long barcode,
            @Param("toTime") LocalDateTime toTime,
            @Param("fromTime") LocalDateTime fromTime);

    @Query(value = "select distinct barcode from sale", nativeQuery = true)
    List<Long> getBarcodes();

    @Query(value = "select time from sale order by time limit 1", nativeQuery = true)
    LocalDateTime getStartDate();

    @Query(value = "select barcode, sum(quantity) as quantity, sum(quantity * price) as revenue, sum(margin) as netprofit " +
            "from sale " +
            "where time >= :from and time <= :to and barcode = :bar " +
            "group by barcode", nativeQuery = true)
    ReportRawDto getReportInfromation(
            @Param("bar") Long barcode,
            @Param("from") LocalDateTime fromTime,
            @Param("to") LocalDateTime toTime);
}
