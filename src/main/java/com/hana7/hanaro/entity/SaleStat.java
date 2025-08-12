package com.hana7.hanaro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleStat {
	@Id
	@Column(length = 10)
	private String saledt;

	private int ordercnt;
	private int totamt;

	@OneToMany(mappedBy = "saledt", cascade = CascadeType.ALL)
	private List<SaleItemStat> saleItemStats;

	//	@Override
	//	public String toString() {
	//		return "SaleStat{" +
	//				"saledt='" + saledt + '\'' +
	//				", ordercnt=" + ordercnt +
	//				", totamt=" + totamt +
	//				", saleItemStats=" + (saleItemStats == null ? 0 : saleItemStats.size()) +
	//				'}';
	//	}
}
