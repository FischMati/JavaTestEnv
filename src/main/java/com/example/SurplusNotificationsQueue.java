package com.example;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SurplusNotificationsQueue {
    private final BlockingQueue<UUID> queue = new LinkedBlockingQueue<>();

    public BlockingQueue<UUID> getQueue() {
        return queue;
    }

    public void addNotification(UUID inventoryUuid) {
        queue.offer(inventoryUuid);
    }
}
