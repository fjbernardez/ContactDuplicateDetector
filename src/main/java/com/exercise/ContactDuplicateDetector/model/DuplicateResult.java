package com.exercise.ContactDuplicateDetector.model;

public record DuplicateResult(
        int sourceContactId,
        int matchedContactId,
        String accuracy) {

    @Override
    public String toString() {
        return sourceContactId + "," + matchedContactId + "," + accuracy;
    }
}
