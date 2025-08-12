package com.hana7.hanaro.entity;


import com.hana7.hanaro.enums.ORDERSTATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Orders extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "user_id",
		foreignKey = @ForeignKey(name = "fk_Cart_User")
	)
	private User user;

	@Column(name = "orderStatus", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'PAID'")
	private ORDERSTATUS orderStatus = ORDERSTATUS.PAID;

	@Column(name = "totalPrice", nullable = false)
	@ColumnDefault("0")
	private int totalPrice;

	@OneToMany(
		mappedBy = "order",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@Builder.Default
	@ToString.Exclude
	private List<OrderItem> orderItems = new ArrayList<>();;
}
