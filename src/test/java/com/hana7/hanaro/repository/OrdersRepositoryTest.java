package com.hana7.hanaro.repository;

import com.hana7.hanaro.config.QueryDSLConfig;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.OrderItem;
import com.hana7.hanaro.entity.Orders;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.enums.ORDERSTATUS;
import com.hana7.hanaro.enums.USERROLE;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QueryDSLConfig.class)
class OrdersRepositoryTest extends RepositoryTest {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByUser() {
        // given
        User user = User.builder()
            .userName("testuser")
            .email("test1@test.com")
            .password("password")
            .role(USERROLE.ROLE_USER)
            .build();
        userRepository.save(user);

        Item item = Item.builder()
            .itemName("test item")
            .price(1000)
            .quantity(10)
            .build();
        itemRepository.save(item);

        Orders order = Orders.builder()
            .user(user)
            .orderStatus(ORDERSTATUS.PAID)
            .orderItems(new ArrayList<>())
            .build();

        OrderItem orderItem = OrderItem.builder()
            .order(order)
            .item(item)
            .amount(1)
            .build();
        order.getOrderItems().add(orderItem);

        ordersRepository.save(order);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Orders> foundOrders = ordersRepository.findByUser(user, pageable);

        // then
        assertThat(foundOrders).isNotNull();
        assertThat(foundOrders.getTotalElements()).isEqualTo(1);
        assertThat(foundOrders.getContent().get(0).getUser().getUserName()).isEqualTo("testuser");
    }
}
