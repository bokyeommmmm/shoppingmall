package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.ItemDetailResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import jakarta.validation.Valid;
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

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "아이템 추가")
	public ResponseEntity<?> insertItem(@Valid ItemRequestDTO itemRequestDTO) {
		log.info("[insertItem] 시작 - name={}, price={}, qty={}, images={}",
			itemRequestDTO.getItemName(),
			itemRequestDTO.getPrice(),
			itemRequestDTO.getQuantity(),
			itemRequestDTO.getImages() == null ? 0 : itemRequestDTO.getImages().size());

		itemService.insertItem(itemRequestDTO);

		log.info("[insertItem] 종료 - name={}", itemRequestDTO.getItemName());
		return ResponseEntity.ok().build();
	}
	@Operation(summary = "아이템 검색")
	@GetMapping("/search")
	public ResponseEntity<Page<ItemResponseDTO>> searchItem(
		@RequestParam("keyword") String keyword,
		@ParameterObject @PageableDefault(size = 10, page = 0, sort = "itemName") Pageable pageable) {

		log.info("[searchItem] 시작 - keyword='{}', page={}, size={}",
			keyword, pageable.getPageNumber(), pageable.getPageSize());

		Page<ItemResponseDTO> result = itemService.findItem(keyword, pageable);

		log.info("[searchItem] 종료 - keyword='{}', total={}", keyword, result.getTotalElements());
		return ResponseEntity.ok(result);
	}

	@Operation(summary = "아이템 상세정보 get")
	@GetMapping("/detail/{id}")
	public ResponseEntity<ItemDetailResponseDTO> getItemDetail(
		@PathVariable Long id,
		HttpServletRequest request) {

		log.info("[getItemDetail] 시작 - itemId={}", id);

		ItemDetailResponseDTO itemDetail = itemService.findItemDetailById(id);
		String baseUrl = String.format("%s://%s:%d",
			request.getScheme(), request.getServerName(), request.getServerPort());

		itemDetail.setImageUrls(
			itemDetail.getImageUrls().stream()
				.map(url -> baseUrl + url)
				.collect(Collectors.toList())
		);

		log.info("[getItemDetail] 종료 - itemId={}, imageCount={}",
			id, itemDetail.getImageUrls() == null ? 0 : itemDetail.getImageUrls().size());
		return ResponseEntity.ok(itemDetail);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "아이템 상세정보 수정")
	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateItem(
		@PathVariable Long id,
		@Valid ItemRequestDTO itemRequestDTO) {

		log.info("[updateItem] 시작 - itemId={}, newName={}, newPrice={}, newQty={}, images={}",
			id,
			itemRequestDTO.getItemName(),
			itemRequestDTO.getPrice(),
			itemRequestDTO.getQuantity(),
			itemRequestDTO.getImages() == null ? 0 : itemRequestDTO.getImages().size());

		itemService.updateItem(id, itemRequestDTO);

		log.info("[updateItem] 종료 - itemId={}", id);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "아이템 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteItem(
		@PathVariable Long id) throws NotFoundException, BadRequestException {

		log.info("[deleteItem] 시작 - itemId={}", id);

		itemService.deleteItem(id);

		log.info("[deleteItem] 종료 - itemId={}", id);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "아이템 수량 변경")
	@PatchMapping(value = "{id}/quantity")
	public ResponseEntity<?> updateItemQuantity(
		@PathVariable Long id,
		@RequestParam @Min(0) int quantity) {

		log.info("[updateItemQuantity] 시작 - itemId={}, quantity={}", id, quantity);

		itemService.modifyItemQuantity(id, quantity);

		log.info("[updateItemQuantity] 종료 - itemId={}, quantity={}", id, quantity);
		return ResponseEntity.ok().build();
	}
}
