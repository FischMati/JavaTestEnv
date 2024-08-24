package com.example;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NotificationService {
    private final InventoryManager inventoryManager;
    private final ScheduledExecutorService executorService;
    private final Logger logger = Logger.getLogger(NotificationService.class.getName());

    public NotificationService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    public NotificationService(InventoryManager inventoryManager, ScheduledExecutorService executorService) {
        this.inventoryManager = inventoryManager;
        this.executorService = executorService;
    }

    public void setUpLowStockNotifications() {
        executorService.scheduleWithFixedDelay(() -> {
            List<ProductInventory> lowStockProductInventories = inventoryManager
                .getProducts()
                .values()
                .stream()
                .filter(p -> p.getQuantity() <= 2)
                .collect(Collectors.toUnmodifiableList());

            lowStockProductInventories.forEach(pi -> {
              logger.warning("Low stock detected for product inventory with UUID " + pi.getUuid());  
            });
        },0, 10, TimeUnit.MINUTES);
    }

    public void shutDownNotifications() {
        executorService.shutdown();
    }
}
