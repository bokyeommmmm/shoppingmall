package com.hana7.hanaro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "orders",
		foreignKey = @ForeignKey(name = "fk_OrderItem_Orders")
	)
	private Orders order;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "item",
		foreignKey = @ForeignKey(name = "fk_OrderItem_Item")
	)
	private Item item;

	@Column(nullable = false, name = "amount")
	private int amount;

	@Column(nullable = false, name = "totalPrice")
	private int totalPrice;
}
