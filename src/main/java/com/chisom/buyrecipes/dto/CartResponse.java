package com.chisom.buyrecipes.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(Long id, BigDecimal total, List<CartItemResponse> cartItem) {
}
