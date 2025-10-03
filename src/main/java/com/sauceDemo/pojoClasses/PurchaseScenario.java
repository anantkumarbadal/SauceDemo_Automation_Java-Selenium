package com.sauceDemo.pojoClasses;


import com.fasterxml.jackson.annotation.JsonProperty; // Import for Jackson annotations
import java.util.List; // For the list of products_to_add

// This class represents each top-level purchase scenario object in your JSON array
public class PurchaseScenario {

    @JsonProperty("scenario_name")
    private String scenarioName; // Snake_case in JSON mapped to camelCase in Java

    @JsonProperty("products_to_add")
    private List<String> productsToAdd; // A list of product names

    @JsonProperty("checkout_details")
    private CheckoutDetails checkoutDetails; // This will hold our CheckoutDetails object

    @JsonProperty("expected_item_total")
    private String expectedItemTotal; // Using String to match JSON "49.98", can parse to double in test

    @JsonProperty("expected_tax")
    private String expectedTax; // Using String

    @JsonProperty("expected_grand_total")
    private String expectedGrandTotal; // Using String

    @JsonProperty("expected_confirmation_message")
    private String expectedConfirmationMessage; // For successful purchase scenarios

    @JsonProperty("expected_error_message")
    private String expectedErrorMessage; // For scenarios where we expect a checkout error

    // Default constructor (required by Jackson for deserialization)
    public PurchaseScenario() {
    }

    // --- Getters for all fields ---
    // These are used by Jackson to read values from your objects and by your tests to access data

    public String getScenarioName() {
        return scenarioName;
    }

    public List<String> getProductsToAdd() {
        return productsToAdd;
    }

    public CheckoutDetails getCheckoutDetails() {
        return checkoutDetails;
    }

    public String getExpectedItemTotal() {
        return expectedItemTotal;
    }

    public String getExpectedTax() {
        return expectedTax;
    }

    public String getExpectedGrandTotal() {
        return expectedGrandTotal;
    }

    public String getExpectedConfirmationMessage() {
        return expectedConfirmationMessage;
    }

    public String getExpectedErrorMessage() {
        return expectedErrorMessage;
    }

    // --- Setters (Optional, but good if you need to create/modify scenarios programmatically) ---

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public void setProductsToAdd(List<String> productsToAdd) {
        this.productsToAdd = productsToAdd;
    }

    public void setCheckoutDetails(CheckoutDetails checkoutDetails) {
        this.checkoutDetails = checkoutDetails;
    }

    public void setExpectedItemTotal(String expectedItemTotal) {
        this.expectedItemTotal = expectedItemTotal;
    }

    public void setExpectedTax(String expectedTax) {
        this.expectedTax = expectedTax;
    }

    public void setExpectedGrandTotal(String expectedGrandTotal) {
        this.expectedGrandTotal = expectedGrandTotal;
    }

    public void setExpectedConfirmationMessage(String expectedConfirmationMessage) {
        this.expectedConfirmationMessage = expectedConfirmationMessage;
    }

    public void setExpectedErrorMessage(String expectedErrorMessage) {
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Override
    public String toString() {
        return "PurchaseScenario{" +
                "scenarioName='" + scenarioName + '\'' +
                ", productsToAdd=" + productsToAdd +
                ", checkoutDetails=" + checkoutDetails +
                ", expectedItemTotal='" + expectedItemTotal + '\'' +
                ", expectedTax='" + expectedTax + '\'' +
                ", expectedGrandTotal='" + expectedGrandTotal + '\'' +
                ", expectedConfirmationMessage='" + expectedConfirmationMessage + '\'' +
                ", expectedErrorMessage='" + expectedErrorMessage + '\'' +
                '}';
    }
}