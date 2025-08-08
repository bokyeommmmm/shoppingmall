package com.hana7.hanaro.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hana7.hanaro.entity.Item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestDTO {

	@NotBlank
	@Max(30)
	private String itemName;

	@NotNull
	private int price;

	@NotNull
	private int quantity;

	private List<MultipartFile> images;

	public Item toEntity(){
		return Item.builder()
			.itemName(itemName)
			.price(price)
			.quantity(quantity)
			.build();
	}

}
