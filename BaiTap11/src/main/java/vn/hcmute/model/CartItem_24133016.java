package vn.hcmute.model;

import java.math.BigDecimal;
import vn.hcmute.entity.Video_24133016;

public class CartItem_24133016 {
    private final Video_24133016 product;
    private final int quantity;

    public CartItem_24133016(Video_24133016 product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Video_24133016 getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSubtotal() {
        BigDecimal price = product.getPrice() == null ? BigDecimal.ZERO : product.getPrice();
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
