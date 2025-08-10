package com.hana7.hanaro.exception.NotFound;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException() {
        super("해당 장바구니 상품을 찾을 수 없습니다.");
    }
}
