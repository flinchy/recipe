package com.chisom.buyrecipes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "carts")
@SQLRestriction("deleted_at IS NULL") // Soft delete filter
public class Cart extends BaseEntity {

    @Column(name = "total_in_cents")
    private BigDecimal totalIncents;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    public void addToTotalIncents(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");
        if (amount.signum() < 0) throw new IllegalArgumentException("Amount must be non-negative");

        BigDecimal current = Optional.ofNullable(this.totalIncents)
                .orElse(BigDecimal.ZERO);
        this.totalIncents = current.add(amount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void subtractFromTotalIncents(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount to subtract must be non-negative");
        }

        BigDecimal current = Optional.ofNullable(this.totalIncents)
                .orElse(BigDecimal.ZERO);
        BigDecimal newTotal = current.subtract(amount);

        if (newTotal.signum() < 0) {
            throw new IllegalArgumentException(
                    "Cannot subtract %s from total %s resulting total would be negative".formatted(amount, current)
            );
        }

        this.totalIncents = newTotal.setScale(2, RoundingMode.HALF_UP);
    }
}
