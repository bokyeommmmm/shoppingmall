package com.hana7.hanaro.repository;

import com.hana7.hanaro.config.QueryDSLConfig;
import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.enums.USERROLE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QueryDSLConfig.class)
class CartRepositoryTest extends RepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUser() {
        // given
        User user = User.builder()
                .userName("testuser")
                .email("test@test.com")
                .password("password")
                .role(USERROLE.ROLE_USER)
                .build();
        userRepository.save(user);

        Cart cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);

        // when
        Optional<Cart> foundCart = cartRepository.findByUser(user);

        // then
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getUser().getUserName()).isEqualTo("testuser");
    }
}
