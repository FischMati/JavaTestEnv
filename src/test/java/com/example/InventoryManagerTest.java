package com.example;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InventoryManagerTest {
    private InventoryManager inventoryManager;
    private Product product;
    private ProductInventory inventory;

    @BeforeEach() 
    public void initialize() {
        inventoryManager = new InventoryManager();
        product = new Product(UUID.randomUUID(), "test");
        inventory = new ProductInventory(UUID.randomUUID(), product, 2);
    }

    @Test
    void testShouldOnlyAddProductInventoryOnce() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            inventoryManager.addProductInventory(inventory);
        });


        Thread t2 = new Thread(() -> {
            inventoryManager.addProductInventory(inventory);
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        long count = inventoryManager   
            .getInventory()
            .stream()
            .filter(pi -> pi.getUuid().equals(inventory.getUuid()))
            .count();

        Assertions.assertEquals(1, count);
    }

    @Test
    void testShouldIncrementInventoryConsistently() throws InterruptedException {
        inventoryManager.addProductInventory(inventory);


        Thread t1 = new Thread(() -> {
            inventoryManager.increaseProductQuantity(inventory.getUuid(), 2);
        });

        Thread t2 = new Thread(() -> {
            inventoryManager.increaseProductQuantity(inventory.getUuid(), 2);
        });

        Thread t3 = new Thread(() -> {
            inventoryManager.increaseProductQuantity(inventory.getUuid(), 2);
        });

        Thread t4 = new Thread(() -> {
            inventoryManager.increaseProductQuantity(inventory.getUuid(), 2);
        });
        
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        
        Assertions.assertEquals(10, inventory.getQuantity());
    }

    @Test
    void testShouldDecrementInventoryConsistently() throws InterruptedException {
        inventoryManager.addProductInventory(inventory);


        Thread t1 = new Thread(() -> {
            inventoryManager.decreaseProductQuantity(inventory.getUuid(), 2);
        });

        Thread t2 = new Thread(() -> {
            inventoryManager.increaseProductQuantity(inventory.getUuid(), 2);
        });

        Thread t3 = new Thread(() -> {
            inventoryManager.decreaseProductQuantity(inventory.getUuid(), 2);
        });

        Thread t4 = new Thread(() -> {
            inventoryManager.increaseProductQuantity(inventory.getUuid(), 5);
        });
        
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        
        Assertions.assertEquals(5, inventory.getQuantity());
    }
}
