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
public class ItemImage extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="orgName",nullable = false)
	private String orgName;

	@Column(name="saveName",nullable = false)
	private String saveName;

	@Column(name="saveDir",nullable = false)
	private String saveDir;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="item",
		foreignKey = @ForeignKey(name="fk_ItemImage_Item"))
	private Item item;
}
