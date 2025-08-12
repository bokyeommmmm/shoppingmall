package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.SaleItemStatResponseDTO;
import com.hana7.hanaro.dto.SaleStatResponseDTO;
import com.hana7.hanaro.service.StatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Tag(name = "통계 관리")
public class StatController {

    private final StatService statService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "일별 매출 통계 조회")
    @GetMapping("/sales/daily")
    public ResponseEntity<Page<SaleStatResponseDTO>> getDailySalesStats(
            @ParameterObject @PageableDefault(size = 10, sort = "saledt") Pageable pageable) {
        return ResponseEntity.ok(statService.getDailySalesStats(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "상품별 매출 통계 조회")
    @GetMapping("/sales/items")
    public ResponseEntity<Page<SaleItemStatResponseDTO>> getItemSalesStats(
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(statService.getItemSalesStats(pageable));
    }
}
