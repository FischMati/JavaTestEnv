package com.example;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.Executors;

public class NotificationServiceTest {
    @Mock
    private InventoryManager inventoryManager;

    @Mock
    private ScheduledExecutorService executorService;

    private NotificationService notificationService;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);

        notificationService = new NotificationService(inventoryManager, executorService);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(executorService, inventoryManager);
    }


    @Test
    void testShouldSetUpNotifications() {
        notificationService.setUpLowStockNotifications();

        verify(executorService).scheduleWithFixedDelay(Mockito.any(), eq(0L), eq(10L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testShouldTriggerLowStockCheckOnStart() {
        when(inventoryManager.getProducts()).thenReturn(new ConcurrentHashMap<UUID, ProductInventory>());
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        notificationService.setUpLowStockNotifications();

        verify(executorService).scheduleWithFixedDelay(runnableCaptor.capture(), eq(0L), eq(10L), eq(TimeUnit.MINUTES));
        
        runnableCaptor.getValue().run();

        verify(inventoryManager).getProducts();
    }


    @Test
    void testShouldTriggerLowStockCheckOnDelay() throws InterruptedException {
        when(inventoryManager.getProducts()).thenReturn(new ConcurrentHashMap<UUID, ProductInventory>());
        ScheduledExecutorService realExecutorService = Executors.newScheduledThreadPool(1);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        when(executorService.scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class))).thenAnswer(args -> {
            return realExecutorService.scheduleWithFixedDelay(args.getArgument(0), 0L, 10L, TimeUnit.MILLISECONDS);
        });

        notificationService.setUpLowStockNotifications();

        verify(executorService).scheduleWithFixedDelay(runnableCaptor.capture(), eq(0L), eq(10L), eq(TimeUnit.MINUTES));
        
        Thread.sleep(15);  // 15 milisegundos para asegurar al menos dos ejecuciones

        verify(inventoryManager, atLeast(2)).getProducts();

        realExecutorService.shutdown();
    }

}
