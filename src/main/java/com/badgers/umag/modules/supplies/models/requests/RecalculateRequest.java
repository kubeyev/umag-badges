package com.badgers.umag.modules.supplies.models.requests;

import com.badgers.umag.core.configs.LocalDateTimeDeserializer;
import com.badgers.umag.core.configs.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecalculateRequest implements Serializable {

    private static final long newSerialVersionUID = 1190536278266811217L;
    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("barcode")
    private Long barcode;
}
