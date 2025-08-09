package com.hana7.hanaro.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDTO {

	private Long id;

	private String itemName;

	private int price;

	private int quantity;

}
