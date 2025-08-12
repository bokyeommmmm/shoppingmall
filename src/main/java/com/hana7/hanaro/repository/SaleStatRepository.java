package com.hana7.hanaro.repository;

import com.hana7.hanaro.entity.SaleStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleStatRepository extends JpaRepository<SaleStat, String> {
}