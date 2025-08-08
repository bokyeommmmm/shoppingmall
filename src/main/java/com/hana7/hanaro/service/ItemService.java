package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.ItemRequestDTO;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.ItemImage;
import com.hana7.hanaro.repository.ItemImageRepository;
import com.hana7.hanaro.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	@Value("${upload.path}")
	private String uploadPath = "src/main/resources";

	public void insertItem(ItemRequestDTO itemRequestDTO) {
		Item item = itemRepository.save(itemRequestDTO.toEntity());

		saveFiles(itemRequestDTO.getImages(),item);
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

			itemImageRepository.saveAll(itemImages);
		}
	}
	private String getTodayPath() {
		LocalDateTime now = LocalDateTime.now();
		return String.format("%4d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}
}
