package com.hana7.hanaro.dto;

import com.hana7.hanaro.entity.SaleItemStat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemStatResponseDTO {
    private Integer id;
    private String itemName;
    private int cnt;
    private int amt;

    public SaleItemStatResponseDTO(SaleItemStat saleItemStat) {
        this.id = saleItemStat.getId();
        this.itemName = saleItemStat.getItem().getItemName();
        this.cnt = saleItemStat.getCnt();
        this.amt = saleItemStat.getAmt();
    }
}
