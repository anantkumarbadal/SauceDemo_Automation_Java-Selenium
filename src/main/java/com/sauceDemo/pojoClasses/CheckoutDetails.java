package com.sauceDemo.pojoClasses;

import com.fasterxml.jackson.annotation.JsonProperty; // Import for Jackson annotations

// This class represents the 'checkout_details' object in your JSON
public class CheckoutDetails {

    @JsonProperty("firstName") // Maps JSON field "firstName" to this Java field
    private String firstName;

    @JsonProperty("lastName") // Maps JSON field "lastName" to this Java field
    private String lastName;

    @JsonProperty("zipCode") // Maps JSON field "zipCode" to this Java field
    private String zipCode;

    // Default constructor (required by Jackson for deserialization)
    public CheckoutDetails() {
    }

    // Constructor with all fields (optional, but good for creating objects programmatically)
    public CheckoutDetails(String firstName, String lastName, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
    }

    // --- Getters ---
    // Jackson uses these getters/setters (or direct field access) for mapping

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getZipCode() {
        return zipCode;
    }

    // --- Setters (optional, but good practice if you need to modify objects after creation) ---

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "CheckoutDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
