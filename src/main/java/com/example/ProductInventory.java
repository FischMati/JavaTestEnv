package com.example;

import java.util.UUID;

public class ProductInventory {
    private final UUID uuid;
    

    private final Product product;
    private int quantity = 0;

    public ProductInventory(UUID uuid, Product product, int quantity) {
        this.uuid = uuid;
        this.product = product;
        this.quantity = quantity;
    }

    public Product product() {
        return product;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int increment) {
        quantity = quantity + increment;
    }

    public void decreaseQuantity(int decrement) throws NegativeQuantityException {
        quantity = quantity - decrement;

        if (quantity < 0) {
            throw new NegativeQuantityException();
        }
    }
}
