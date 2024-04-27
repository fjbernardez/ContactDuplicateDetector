package com.exercise.ContactDuplicateDetector.service;

import com.exercise.ContactDuplicateDetector.model.Contact;
import com.exercise.ContactDuplicateDetector.model.DuplicateResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuplicateDetectorTest {
    @Test
    public void testFindDuplicatesByEmail() {

        Map<String, List<Contact>> contactsByEmail = new HashMap<>();
        Contact contact1 = new Contact(
                1,
                "John",
                "Doe",
                "example@example.com",
                "12345",
                "123 Main St"
        );

        Contact contact2 = new Contact(
                2,
                "Jane",
                "Smith",
                "example@example.com",
                "12345",
                "124 Main St");

        contactsByEmail.put("example@example.com", Arrays.asList(contact1, contact2));

        DuplicateDetector duplicateDetector = new DuplicateDetector(null, contactsByEmail, new HashMap<>(), new HashMap<>());


        List<DuplicateResult> results = duplicateDetector.findDuplicates(contact1);
        assertEquals(1, results.size());
        assertEquals(new DuplicateResult(1, 2, "High"), results.get(0));
    }


    @Test
    public void testFindDuplicatesByAddress() {

        Map<String, List<Contact>> contactsByAddress = new HashMap<>();
        Contact contact1 = new Contact(1,
                "John",
                "Doe",
                "john.doe@example.com",
                "12345",
                "123 Main St");

        Contact contact2 = new Contact(2,
                "Jane",
                "Doe",
                "jane.smith@example.org",
                "12345",
                "123 Main St");

        contactsByAddress.put("123 Main St", Arrays.asList(contact1, contact2));

        DuplicateDetector duplicateDetector = new DuplicateDetector(null, new HashMap<>(), contactsByAddress, new HashMap<>());

        List<DuplicateResult> results = duplicateDetector.findDuplicates(contact1);
        assertEquals(1, results.size());
        assertEquals(new DuplicateResult(1, 2, "Medium"), results.get(0));
    }

    @Test
    public void testNoDuplicatesFound() {

        Contact contact1 = new Contact(1,
                "John",
                "Doe",
                "john.doe@example.com",
                "876",
                "586 Fes St");

        Contact contact2 = new Contact(2,
                "Jane",
                "Josh",
                "jane.smith@example.org",
                "12345",
                "123 Main St");

        Map<String, List<Contact>> contactsByEmail = new HashMap<>();
        contactsByEmail.put("john.doe@example.com", List.of(contact1));
        contactsByEmail.put("jane.smith@example.org", List.of(contact2));

        Map<String, List<Contact>> contactsByAddress = new HashMap<>();
        contactsByAddress.put("586 Fes St", List.of(contact1));
        contactsByAddress.put("123 Main St", List.of(contact2));

        Map<String, List<Contact>> contactsByZipCode = new HashMap<>();
        contactsByAddress.put("876", List.of(contact1));
        contactsByAddress.put("12345", List.of(contact2));

        DuplicateDetector duplicateDetector = new DuplicateDetector(null, contactsByEmail, contactsByAddress, contactsByZipCode);

        List<DuplicateResult> results = duplicateDetector.findDuplicates(contact1);
        assertEquals(0, results.size());
    }
}

