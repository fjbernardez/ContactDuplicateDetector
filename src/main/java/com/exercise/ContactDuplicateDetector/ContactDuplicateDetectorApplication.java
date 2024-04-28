package com.exercise.ContactDuplicateDetector;

import com.exercise.ContactDuplicateDetector.model.Contact;
import com.exercise.ContactDuplicateDetector.model.DuplicateResult;
import com.exercise.ContactDuplicateDetector.service.ContactService;
import com.exercise.ContactDuplicateDetector.service.DuplicateDetector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ContactDuplicateDetectorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ContactDuplicateDetectorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<DuplicateResult> allDuplicateResults = Collections.synchronizedList(new ArrayList<>());

        ContactService contactService = new ContactService();
        contactService.loadContacts("src/main/resources/contacts.csv");

        DuplicateDetector duplicateDetector = new DuplicateDetector(
                contactService.getAllContacts(),
                contactService.getEmailIndex(),
                contactService.getAddressIndex(),
                contactService.getZipCodeIndex()
        );

        int numCores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numCores);

        for (Contact contact : contactService.getAllContacts()) {
            executor.submit(() -> {
                List<DuplicateResult> duplicateResults = duplicateDetector.findDuplicates(contact);
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

        writeFile(allDuplicateResults);
    }

    private void writeFile(List<DuplicateResult> results) {
        String path = "src/main/resources/result.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            if (results.isEmpty()) {
                writer.write("No duplicates found.");
            } else {
                writer.write("ContactID Source,ContactID Match,Accuracy\n");
                for (DuplicateResult result : results) {
                    writer.write(result.toString() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
