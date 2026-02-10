package com.pfe.order.domain;

import java.math.BigDecimal;

public class OrderItem {
    private String productId;
    private int quantity;
    private BigDecimal price;
    // Production specific details
    private String quality;
    private boolean hasBorder;

    public OrderItem() {
    }

    public OrderItem(String productId, int quantity, BigDecimal price, String quality, boolean hasBorder) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.quality = quality;
        this.hasBorder = hasBorder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }
}
