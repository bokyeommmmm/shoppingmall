package com.hana7.hanaro.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.dto.ItemRequestDTO;
import com.hana7.hanaro.repository.ItemRepository;
import com.hana7.hanaro.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
	private final ItemRepository itemRepository;
	private final ItemService itemService;

	@PostMapping(value="",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> insertItem(ItemRequestDTO itemRequestDTO) {
		itemService.insertItem(itemRequestDTO);

		return ResponseEntity.ok().build();
	}

}
