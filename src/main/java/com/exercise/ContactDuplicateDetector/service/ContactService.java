package com.exercise.ContactDuplicateDetector.service;

import com.exercise.ContactDuplicateDetector.model.Contact;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactService {

    private List<Contact> allContacts = new ArrayList<>();
    private Map<String, List<Contact>> emailIndex = new HashMap<>();
    private Map<String, List<Contact>> addressIndex = new HashMap<>();
    private Map<String, List<Contact>> zipCodeIndex = new HashMap<>();

    public void loadContacts(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                int contactId = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                String email = parts[3].trim();
                String zipCode = parts[4].trim();
                String address = parts[5].trim();

                Contact contact = new Contact(contactId, firstName, lastName, email, zipCode, address);
                addContact(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addContact(Contact contact) {
        allContacts.add(contact);

        if (!contact.email().isEmpty()) {
            emailIndex.computeIfAbsent(contact.email(), k -> new ArrayList<>()).add(contact);
        }

        if (!contact.address().isEmpty()) {
            addressIndex.computeIfAbsent(contact.address(), k -> new ArrayList<>()).add(contact);
        }

        if (!contact.zipCode().isEmpty()) {
            zipCodeIndex.computeIfAbsent(contact.zipCode(), k -> new ArrayList<>()).add(contact);
        }
    }

    public void printAllContacts() {
        emailIndex.values().forEach(System.out::println);
    }

    public Map<String, List<Contact>> getEmailIndex() {
        return emailIndex;
    }

    public Map<String, List<Contact>> getAddressIndex() {
        return addressIndex;
    }

    public Map<String, List<Contact>> getZipCodeIndex() {
        return zipCodeIndex;
    }

    public List<Contact> getAllContacts() {
        return allContacts;
    }
}
