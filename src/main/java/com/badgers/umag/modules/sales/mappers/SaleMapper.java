package com.badgers.umag.modules.sales.mappers;

import com.badgers.umag.modules.sales.models.entites.Sale;
import com.badgers.umag.modules.sales.models.responses.SaleDto;
import com.badgers.umag.modules.supplies.models.entities.Supply;
import com.badgers.umag.modules.supplies.models.responses.SupplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SaleMapper {
    @Mapping(target = "saleTime", source = "time", qualifiedByName = "mapTime")
    SaleDto map(Sale source);

    @Named("mapTime")
    static String mapTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}
