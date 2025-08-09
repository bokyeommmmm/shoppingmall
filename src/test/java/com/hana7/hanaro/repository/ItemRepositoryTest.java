package com.hana7.hanaro.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.hana7.hanaro.config.QueryDSLConfig;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.ItemImage;

@Import(QueryDSLConfig.class)
class ItemRepositoryTest extends RepositoryTest {
	@Autowired
	ItemRepository itemRepository;

	@Test
	void addItem(){
		//
		Item item = Item.builder()
			.itemName("soju")
			.price(1300)
			.quantity(10)
			.build();
		ItemImage itemImage = ItemImage.builder()
			.orgName("orgName")
			.saveName("saveName")
			.saveDir("saveDir")
			.build();

		item.getImages().add(itemImage);
		//
		Item saved = itemRepository.save(item);

		//
		Assertions.assertThat(saved.getItemName()).isEqualTo("soju");
	}

	@Test
	void addAllItems(){
		//
		List<Item> items = new ArrayList<>();

		Item item1 = Item.builder()
			.itemName("soju")
			.price(1300)
			.quantity(10)
			.build();
		Item item2 = Item.builder()
			.itemName("beer")
			.price(1500)
			.quantity(20)
			.build();
		Item item3 = Item.builder()
			.itemName("maggulli")
			.price(1000)
			.quantity(30)
			.build();

		items.add(item1);
		items.add(item2);
		items.add(item3);

		//
		itemRepository.saveAll(items);
		List<Item> allItems = itemRepository.findAll();
		//
		Assertions.assertThat(allItems.size()).isEqualTo(3);
	}

	@Test
	void findItemWithKeyword(){
		//
		List<Item> items = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 10);

		Item item1 = Item.builder()
			.itemName("soju")
			.price(1300)
			.quantity(10)
			.build();
		Item item2 = Item.builder()
			.itemName("beer")
			.price(1500)
			.quantity(20)
			.build();
		Item item3 = Item.builder()
			.itemName("maggulli")
			.price(1000)
			.quantity(30)
			.build();

		items.add(item1);
		items.add(item2);
		items.add(item3);

		itemRepository.saveAll(items);
//
		Page<Item> findItems = itemRepository.findByItemNameContainsIgnoreCase("soju", pageable);;
		//
		Assertions.assertThat(findItems.getTotalElements()).isEqualTo(1);
	}
}
