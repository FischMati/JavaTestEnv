package com.example;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class InventoryManager {
    private ConcurrentMap<UUID, ProductInventory> products = new ConcurrentHashMap<UUID, ProductInventory>();

    public ConcurrentMap<UUID, ProductInventory> getProducts() {
        return products;
    }

    public void addOneProduct(Product product){
        ProductInventory productInventory = new ProductInventory(UUID.randomUUID(), product, 1);

        products.putIfAbsent(productInventory.getUuid(), productInventory);
    }

    public void addProductInventory(ProductInventory productInventory){        
        products.putIfAbsent(productInventory.getUuid(), productInventory);
    }

    public void increaseProductQuantity(UUID productUuid, int increment){
        products.computeIfPresent(productUuid, (id, p) -> {
            p.increaseQuantity(increment);

            return p;
        });        
    }

    public void decreaseProductQuantity(UUID productUuid, int decrement) throws NegativeQuantityException {
        products.computeIfPresent(productUuid, (id, p) -> {
            p.decreaseQuantity(decrement);

            return p;
        });   
    }

    public List<ProductInventory> getInventory() {
        return Collections.unmodifiableList(products
            .values()
            .stream()
            .collect(Collectors.toList()));
    }

    public void removeProductInventory(UUID productInventoryUuid) throws ProductNotFoundException {
        ProductInventory result = products.remove(productInventoryUuid);

        if(result == null){
            throw new ProductNotFoundException();
        }
    }
}
