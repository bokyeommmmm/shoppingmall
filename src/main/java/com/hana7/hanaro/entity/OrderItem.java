package com.hana7.hanaro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderItem extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn( name = "order_id", foreignKey = @ForeignKey(name = "fk_OrderItem_Orders"))
	private Orders order;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn( name = "item_id", foreignKey = @ForeignKey(name = "fk_OrderItem_Item"))
	private Item item;

	@Column(nullable = false,name="amount")
	@Min(0)
	private int amount;
}
