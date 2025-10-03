package com.sauceDemo.dataBuilders;

import com.sauceDemo.pojoClasses.CheckoutDetails;
import com.sauceDemo.utils.FakerUtils;

// This class implements the Test Data Builder pattern for CheckoutDetails
public class CheckoutDetailsBuilder {

    // Fields that will be used to construct the CheckoutDetails object
    private String firstName;
    private String lastName;
    private String zipCode;

    // Constructor to initialize the builder with default (faked) values
    // This ensures that if you don't explicitly set a field, it still has valid data.
    public CheckoutDetailsBuilder() {
        this.firstName = FakerUtils.generateFirstName();
        this.lastName = FakerUtils.generateLastName();
        this.zipCode = FakerUtils.generateZipCode();
    }

    // --- Fluent Setter Methods (return 'this' for chaining) ---

    public CheckoutDetailsBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this; // Allows method chaining: .withFirstName(...).withLastName(...)
    }

    public CheckoutDetailsBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CheckoutDetailsBuilder withZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    // --- Build Method ---
    // This method constructs and returns the final CheckoutDetails object
    public CheckoutDetails build() {
        return new CheckoutDetails(firstName, lastName, zipCode);
    }

    // Helper method to build an instance with only mandatory fields, using Faker for others
    public static CheckoutDetails createDefault() {
        return new CheckoutDetailsBuilder().build(); // Just build with constructor defaults
    }

    // Example of a specific scenario builder if needed
    public static CheckoutDetails createWithEmptyZipCode() {
        return new CheckoutDetailsBuilder()
                .withZipCode("") // Override default Faker zip code with empty
                .build();
    }


}


/*
Understanding the Test Data Builder Pattern
Before we code, a quick recap:

The Builder pattern is a creational design pattern that allows for the step-by-step construction
of complex objects. When it comes to test data, it provides:

Readability: Instead of long, confusing constructors or sequential setter calls,
you get a fluent, easy-to-read way to create test data objects.

Flexibility: You can define default values for most fields and only specify the ones
relevant to your current test scenario.

Reusability: Common configurations can be encapsulated, and then minor variations
can be applied easily.

Immutability (Optional but Good): Builders often construct immutable objects,
which are safer in multi-threaded environments.
 */
