package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.ItemDetailResponseDTO;
import com.hana7.hanaro.dto.ItemRequestDTO;
import com.hana7.hanaro.dto.ItemResponseDTO;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.ItemImage;
import com.hana7.hanaro.exception.BadRequest.ItemDeleteBadRequestException;
import com.hana7.hanaro.exception.NotFound.ItemNotFoundException;
import com.hana7.hanaro.exception.NotFound.NotFoundException;
import com.hana7.hanaro.exception.NotFound.UserNotFoundException;
import com.hana7.hanaro.repository.ItemImageRepository;
import com.hana7.hanaro.repository.ItemRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //파이널에선 항상 .
@Transactional
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	@Value("${upload.path}")
	private String uploadPath = "src/main/resources";

	public void insertItem(ItemRequestDTO itemRequestDTO) {
		Item item = itemRepository.save(itemRequestDTO.toEntity());

		saveFiles(itemRequestDTO.getImages(),item);
	}
	public Page<ItemResponseDTO> findItem(String keyword, Pageable pageable) {
		Page<Item>items = itemRepository.findByItemNameContainsIgnoreCase(keyword, pageable);

		return items.map(ItemService::toDto);

	}

	public ItemDetailResponseDTO findItemDetailById(Long id) {
		Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
		return toDetailDto(item);
	}

	public void updateItem (Long itemId, ItemRequestDTO itemRequestDTO) {
		Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

		itemRepository.save(modifyItem(item, itemRequestDTO));
	}
	public void deleteItem (Long itemId) {
		if(!itemRepository.existsById(itemId)) {
			throw new UserNotFoundException();
		}
		try {
			itemRepository.deleteById(itemId);
		} catch(Exception e) {
			throw new ItemDeleteBadRequestException();
		}
	}
	private Item modifyItem(Item item, ItemRequestDTO itemRequestDTO) {
		saveFiles(itemRequestDTO.getImages(),item);

		return item.toBuilder()
			.itemName(itemRequestDTO.getItemName())
			.price(itemRequestDTO.getPrice())
			.quantity(itemRequestDTO.getQuantity())
			.build();
	}
	public void modifyItemQuantity(Long itemId, int quantity) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(ItemNotFoundException::new);

		item.setQuantity(quantity);
	}

		private static ItemResponseDTO toDto(Item items) {
		return ItemResponseDTO.builder()
			.id(items.getId())
			.itemName(items.getItemName())
			.price(items.getPrice())
			.quantity(items.getQuantity())
			.build();
	}

	private ItemDetailResponseDTO toDetailDto(Item item) {
		return ItemDetailResponseDTO.builder()
			.id(item.getId())
			.itemName(item.getItemName())
			.price(item.getPrice())
			.imageUrls(item.getImages().stream()
				.map(image -> "/upload/" + image.getSaveDir() + "/" + image.getSaveName())
				.collect(Collectors.toList()))
			.build();
	}

	private void saveFiles(List<MultipartFile> files, Item item) {
		List<ItemImage> itemImages = new ArrayList<>();

		if (files != null) {
			files.forEach(file -> {

				String orgFname = file.getOriginalFilename();
				String uuid = UUID.randomUUID().toString();
				String savedFname = uuid + "_" + orgFname;

				try {
					String savedir = getTodayPath();
					Path uploadDir = Paths.get(uploadPath + File.separator + savedir);
					Path upfilePath = Paths.get(uploadPath + File.separator + savedir + File.separator + savedFname);

					if (!Files.exists(uploadDir)) {
						Files.createDirectories(uploadDir);
					}
					file.transferTo(upfilePath);

					// ItemImage 객체로 저장하기

					itemImages.add(ItemImage.builder()
						.item(item)
						.orgName(orgFname)
						.saveName(savedFname)
						.saveDir(savedir)
						.build());

				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

			List<ItemImage> saved = itemImageRepository.saveAll(itemImages);

			item.getImages().addAll(saved);
			itemRepository.save(item);
		}
	}
	private String getTodayPath() {
		LocalDateTime now = LocalDateTime.now();
		return String.format("%4d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}
}
