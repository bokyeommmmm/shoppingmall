package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.ItemDetailResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.dto.ItemRequestDTO;
import com.hana7.hanaro.dto.ItemResponseDTO;
import com.hana7.hanaro.exception.NotFound.NotFoundException;
import com.hana7.hanaro.repository.ItemRepository;
import com.hana7.hanaro.service.ItemService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Log4j2
public class ItemController {
	private final ItemRepository itemRepository;
	private final ItemService itemService;

	@PostMapping(value="",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> insertItem(@Validated ItemRequestDTO itemRequestDTO) {
		itemService.insertItem(itemRequestDTO);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/search")
	public ResponseEntity<Page<ItemResponseDTO>>searchItem(@RequestParam("keyword") String keyword,
	@ParameterObject @PageableDefault(size = 10, page = 0, sort = "itemName") Pageable pageable) {
		return ResponseEntity.ok(itemService.findItem(keyword,pageable));
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<ItemDetailResponseDTO> getItemDetail(@PathVariable Long id, HttpServletRequest request) {
		ItemDetailResponseDTO itemDetail = itemService.findItemDetailById(id);
		String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		itemDetail.setImageUrls(
			itemDetail.getImageUrls().stream()
				.map(url -> baseUrl + url)
				.collect(Collectors.toList())
		);
		return ResponseEntity.ok(itemDetail);
	}

	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateItem(@PathVariable Long id ,@Validated ItemRequestDTO itemRequestDTO) {
		itemService.updateItem(id, itemRequestDTO);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteItem(@PathVariable Long id) throws NotFoundException, BadRequestException {
	itemService.deleteItem(id);
		return ResponseEntity.ok().build();

	}

	@PatchMapping(value = "{id}/quantity")
	public ResponseEntity<?> updateItemQuantity(
		@PathVariable Long id,
		@RequestParam @Min(0) int quantity
	) {
		itemService.modifyItemQuantity(id, quantity);
		return ResponseEntity.ok().build();
	}
}
