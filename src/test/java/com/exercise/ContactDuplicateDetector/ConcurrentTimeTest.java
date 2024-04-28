package com.exercise.ContactDuplicateDetector;

import com.exercise.ContactDuplicateDetector.model.DuplicateResult;
import com.exercise.ContactDuplicateDetector.service.ContactService;
import com.exercise.ContactDuplicateDetector.service.DuplicateDetector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConcurrentTimeTest {

    List<DuplicateResult> allDuplicateResults = Collections.synchronizedList(new ArrayList<>());
    private final Integer times = 10000;

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

        while (timesCopy > 0) {
            allDuplicateResults.clear();
            duplicateDetector.execute(allDuplicateResults);
            timesCopy--;
        }

        double endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime) / times;

        System.out.println("executionTime: " + executionTime); // 0.6150
    }
}
