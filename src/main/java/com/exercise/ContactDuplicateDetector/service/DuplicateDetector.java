package com.exercise.ContactDuplicateDetector.service;

import com.exercise.ContactDuplicateDetector.model.Contact;
import com.exercise.ContactDuplicateDetector.model.DuplicateResult;
import com.exercise.ContactDuplicateDetector.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DuplicateDetector {

    private final List<Contact> allContacts;
    private final Map<String, List<Contact>> contactsByEmail;
    private final Map<String, List<Contact>> contactsByAddress;
    private final Map<String, List<Contact>> contactsByZipCode;

    public DuplicateDetector(
            List<Contact> allContacts,
            Map<String, List<Contact>> contactsByEmail,
            Map<String, List<Contact>> contactsByAddress,
            Map<String, List<Contact>> contactsByZipCode) {
        this.allContacts = allContacts;
        this.contactsByEmail = contactsByEmail;
        this.contactsByAddress = contactsByAddress;
        this.contactsByZipCode = contactsByZipCode;
    }

    // for test proposal
    public void execute(List<DuplicateResult> allDuplicateResults) {
        int numCores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numCores);

        for (Contact contact : this.allContacts) {
            executor.submit(() -> {
                List<DuplicateResult> duplicateResults = this.findDuplicates(contact);
                if (!duplicateResults.isEmpty()) {
                    allDuplicateResults.addAll(duplicateResults);
                }
            });

        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public List<DuplicateResult> findDuplicates(Contact contact) {

        Predicate<Contact> notSameContact = c -> c.contactId() != contact.contactId();

        // Check by email - High probability
        List<Contact> sameEmailContacts = contactsByEmail.getOrDefault(contact.email(), new ArrayList<>());
        if (sameEmailContacts.size() > 1) {
            return sameEmailContacts.stream()
                    .filter(notSameContact)
                    .map(c -> new DuplicateResult(contact.contactId(), c.contactId(), "High"))
                    .collect(Collectors.toList());
        }

        // Check by address - Medium probability
        List<Contact> sameAddressContacts = contactsByAddress.getOrDefault(contact.address(), new ArrayList<>());
        if (sameAddressContacts.size() > 1) {
            return sameAddressContacts.stream()
                    .filter(notSameContact)
                    .filter(c -> StringUtils.levenshteinDistance(contact.lastName(), c.lastName()) <= 2)
                    .map(c -> new DuplicateResult(contact.contactId(), c.contactId(), "Medium"))
                    .collect(Collectors.toList());
        }

        // Check by zip code - Low probability
        List<Contact> sameZipCodeContacts = contactsByZipCode.getOrDefault(contact.zipCode(), new ArrayList<>());
        if (sameZipCodeContacts.size() > 1) {
            return sameZipCodeContacts.stream()
                    .filter(notSameContact)
                    .filter(c -> !c.email().equals(contact.email()) && StringUtils.levenshteinDistance(contact.lastName(), c.lastName()) <= 2)
                    .map(c -> new DuplicateResult(contact.contactId(), c.contactId(), "Low"))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
