package com.badgers.umag.modules.reports.models.responces;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto implements Serializable {
    private Long barcode;
    private Long quantity;
    private Long revenue;
    private Long netProfit;
}
