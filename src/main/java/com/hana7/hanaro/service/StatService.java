package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.SaleItemStatResponseDTO;
import com.hana7.hanaro.dto.SaleStatResponseDTO;
import com.hana7.hanaro.repository.SaleItemStatRepository;
import com.hana7.hanaro.repository.SaleStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatService {

    private final SaleStatRepository saleStatRepository;
    private final SaleItemStatRepository saleItemStatRepository;

    public Page<SaleStatResponseDTO> getDailySalesStats(Pageable pageable) {
        return saleStatRepository.findAll(pageable).map(SaleStatResponseDTO::new);
    }

    public Page<SaleItemStatResponseDTO> getItemSalesStats(Pageable pageable) {
        return saleItemStatRepository.findAll(pageable).map(SaleItemStatResponseDTO::new);
    }
}
