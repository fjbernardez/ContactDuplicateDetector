package com.exercise.ContactDuplicateDetector.model;

public record Contact(
        int contactId,
        String firstName,
        String lastName,
        String email,
        String zipCode,
        String address) {

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
