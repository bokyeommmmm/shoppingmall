package com.hana7.hanaro.dto;

import com.hana7.hanaro.entity.SaleStat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleStatResponseDTO {
    private String saledt;
    private int ordercnt;
    private int totamt;
    private List<SaleItemStatResponseDTO> saleItemStats;

    public SaleStatResponseDTO(SaleStat saleStat) {
        this.saledt = saleStat.getSaledt();
        this.ordercnt = saleStat.getOrdercnt();
        this.totamt = saleStat.getTotamt();
        this.saleItemStats = saleStat.getSaleItemStats().stream()
                .map(SaleItemStatResponseDTO::new)
                .collect(Collectors.toList());
    }
}
