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
import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	@Value("${upload.path}")
	private String uploadPath = "src/main/resources";

	public void insertItem(ItemRequestDTO itemRequestDTO) {
		log.info("[insertItem] 시작 - name={}, price={}, qty={}, images={}",
			itemRequestDTO.getItemName(), itemRequestDTO.getPrice(),
			itemRequestDTO.getQuantity(),
			itemRequestDTO.getImages() == null ? 0 : itemRequestDTO.getImages().size());

		Item item = itemRepository.save(itemRequestDTO.toEntity());
		log.info("[insertItem] 아이템 저장 완료 - itemId={}", item.getId());

		saveFiles(itemRequestDTO.getImages(), item);
		log.info("[insertItem] 종료 - itemId={}", item.getId());
	}

	public Page<ItemResponseDTO> findItem(String keyword, Pageable pageable) {
		log.info("[findItem] 시작 - keyword='{}', page={}, size={}",
			keyword, pageable.getPageNumber(), pageable.getPageSize());

		Page<Item> items = itemRepository.findByItemNameContainsIgnoreCase(keyword, pageable);

		if (items.isEmpty()) {
			log.warn("[findItem] 검색 결과 없음 - keyword='{}'", keyword);
			throw new ItemNotFoundException();
		}

		log.info("[findItem] 검색 결과 - totalElements={}", items.getTotalElements());
		return items.map(ItemService::toDto);
	}

	public ItemDetailResponseDTO findItemDetailById(Long id) {
		log.info("[findItemDetailById] 시작 - itemId={}", id);
		Item item = itemRepository.findById(id).orElseThrow(() -> {
			log.warn("[findItemDetailById] 아이템 없음 - itemId={}", id);
			return new ItemNotFoundException();
		});
		ItemDetailResponseDTO dto = toDetailDto(item);
		log.info("[findItemDetailById] 종료 - itemId={}, imageCount={}", id,
			dto.getImageUrls() == null ? 0 : dto.getImageUrls().size());
		return dto;
	}

	public void updateItem (Long itemId, ItemRequestDTO itemRequestDTO) {
		log.info("[updateItem] 시작 - itemId={}", itemId);
		Item item = itemRepository.findById(itemId).orElseThrow(() -> {
			log.warn("[updateItem] 아이템 없음 - itemId={}", itemId);
			return new ItemNotFoundException();
		});

		itemRepository.save(modifyItem(item, itemRequestDTO));
		log.info("[updateItem] 아이템 업데이트 완료 - itemId={}", itemId);
	}

	public void deleteItem (Long itemId) {
		log.info("[deleteItem] 시작 - itemId={}", itemId);
		if(!itemRepository.existsById(itemId)) {
			log.warn("[deleteItem] 아이템 없음 - itemId={}", itemId);
			throw new ItemNotFoundException();
		}
		try {
			itemRepository.deleteById(itemId);
			log.info("[deleteItem] 삭제 완료 - itemId={}", itemId);
		} catch(Exception e) {
			log.error("[deleteItem] 삭제 실패 - itemId={}, error={}", itemId, e.toString());
			throw new ItemDeleteBadRequestException();
		}
	}

	private Item modifyItem(Item item, ItemRequestDTO itemRequestDTO) {
		log.info("[modifyItem] 시작 - itemId={}, newName={}, newPrice={}, newQty={}, newImages={}",
			item.getId(),
			itemRequestDTO.getItemName(),
			itemRequestDTO.getPrice(),
			itemRequestDTO.getQuantity(),
			itemRequestDTO.getImages() == null ? 0 : itemRequestDTO.getImages().size());

		saveFiles(itemRequestDTO.getImages(), item);

		Item modified = item.toBuilder()
			.itemName(itemRequestDTO.getItemName())
			.price(itemRequestDTO.getPrice())
			.quantity(itemRequestDTO.getQuantity())
			.build();

		log.info("[modifyItem] 종료 - itemId={}", item.getId());
		return modified;
	}

	public void modifyItemQuantity(Long itemId, int quantity) {
		log.info("[modifyItemQuantity] 시작 - itemId={}, quantity={}", itemId, quantity);
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> {
				log.warn("[modifyItemQuantity] 아이템 없음 - itemId={}", itemId);
				return new ItemNotFoundException();
			});

		item.setQuantity(quantity);
		log.info("[modifyItemQuantity] 수량 변경 완료 - itemId={}, quantity={}", itemId, quantity);
	}

	private static ItemResponseDTO toDto(Item items) {
		log.debug("[toDto] 변환 - itemId={}", items.getId());
		return ItemResponseDTO.builder()
			.id(items.getId())
			.itemName(items.getItemName())
			.price(items.getPrice())
			.quantity(items.getQuantity())
			.build();
	}

	private ItemDetailResponseDTO toDetailDto(Item item) {
		log.debug("[toDetailDto] 변환 - itemId={}, imageCount={}",
			item.getId(), item.getImages() == null ? 0 : item.getImages().size());

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
		log.info("[saveFiles] 시작 - itemId={}, files={}",
			item.getId(), files == null ? 0 : files.size());

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
						log.info("[saveFiles] 업로드 디렉토리 생성 - {}", uploadDir);
					}

					log.info("[saveFiles] 파일 저장 시도 - org='{}', save='{}', path='{}'",
						orgFname, savedFname, upfilePath);

					file.transferTo(upfilePath);

					itemImages.add(ItemImage.builder()
						.item(item)
						.orgName(orgFname)
						.saveName(savedFname)
						.saveDir(savedir)
						.build());

					log.info("[saveFiles] 파일 저장 완료 - {}", savedFname);
				} catch (IOException e) {
					log.error("[saveFiles] 파일 저장 실패 - org='{}', error={}", orgFname, e.toString());
					throw new RuntimeException(e);
				}
			});

			List<ItemImage> saved = itemImageRepository.saveAll(itemImages);
			log.info("[saveFiles] 이미지 엔티티 저장 완료 - count={}", saved.size());

			item.getImages().addAll(saved);
			itemRepository.save(item);
			log.info("[saveFiles] 아이템-이미지 연결 저장 완료 - itemId={}, totalImages={}",
				item.getId(), item.getImages().size());
		} else {
			log.info("[saveFiles] 저장할 파일 없음 - itemId={}", item.getId());
		}
		log.info("[saveFiles] 종료 - itemId={}", item.getId());
	}

	private String getTodayPath() {
		LocalDateTime now = LocalDateTime.now();
		String path = String.format("%4d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		log.debug("[getTodayPath] todayPath={}", path);
		return path;
	}
}
