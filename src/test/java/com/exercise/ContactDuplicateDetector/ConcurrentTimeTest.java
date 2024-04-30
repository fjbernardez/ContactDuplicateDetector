package com.exercise.ContactDuplicateDetector;

import com.exercise.ContactDuplicateDetector.model.DuplicateResult;
import com.exercise.ContactDuplicateDetector.service.ContactService;
import com.exercise.ContactDuplicateDetector.service.DuplicateDetector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentTimeTest {

    List<DuplicateResult> allDuplicateResults = Collections.synchronizedList(new ArrayList<>());
    private final Integer times = 100000;

    @Test
    void concurrentTime() {

        Integer timesCopy = this.times;

        ContactService contactService = new ContactService();
        contactService.loadContacts("src/main/resources/contacts.csv");

        DuplicateDetector duplicateDetector = new DuplicateDetector(
                contactService.getAllContacts(),
                contactService.getEmailIndex(),
                contactService.getAddressIndex(),
                contactService.getZipCodeIndex()
        );

        double startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


        while (timesCopy > 0) {
            allDuplicateResults.clear();
            duplicateDetector.execute(allDuplicateResults, executor);
            timesCopy--;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        double endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime) / times;

        System.out.println("executionTime: " + executionTime); // 0.6150
    }
}
