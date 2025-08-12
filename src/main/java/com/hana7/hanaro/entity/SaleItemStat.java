package com.hana7.hanaro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemStat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "saledt",
		foreignKey = @ForeignKey(name = "fk_SaleItemStat_saledt_SaleStat",
			foreignKeyDefinition = """
					foreign key (saledt)
					references SaleStat(saledt)
					on delete cascade
				"""))
	private SaleStat saledt;

	@ManyToOne
	@JoinColumn(name = "item_id",
		foreignKey = @ForeignKey(name = "fk_OrderItem_item",
			foreignKeyDefinition = """
					foreign key (item_id) 
					references Item(id)
					on delete cascade
				"""))
	private Item item;

	private int cnt;
	private int amt;

	@Override
	public String toString() {
		return "SaleItemStat{" +
			"id=" + id +
			", saledt=" + saledt.getSaledt() +
			", item=" + item.getItemName() +
			", cnt=" + cnt +
			", amt=" + amt +
			'}';
	}

}
