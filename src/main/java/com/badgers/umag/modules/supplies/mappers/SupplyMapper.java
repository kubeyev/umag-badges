package com.badgers.umag.modules.supplies.mappers;

import com.badgers.umag.modules.supplies.models.entities.Supply;
import com.badgers.umag.modules.supplies.models.responses.SupplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplyMapper {
    @Mapping(target = "supplyTime", source = "time", qualifiedByName = "mapTime")
    SupplyDto map(Supply source);

    @Named("mapTime")
    static String mapTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}
