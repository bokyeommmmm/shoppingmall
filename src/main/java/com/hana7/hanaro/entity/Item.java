package com.hana7.hanaro.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ser.Serializers;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Item extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30, name = "itemName")
	private String itemName;

	@Column(nullable = false, name = "price")
	private int price;

	@Column(nullable = false, name = "quantity")
	private int quantity;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	@ToString.Exclude //tostring loop 방지.
	private List<ItemImage> images = new ArrayList<>();


}
