package com.sauceDemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper; // Core Jackson class for JSON processing
import com.fasterxml.jackson.core.type.TypeReference; // For deserializing lists of objects
import com.sauceDemo.pojoClasses.PurchaseScenario; // Import your PurchaseScenario POJO

import java.io.File;
import java.io.IOException;
import java.net.URL; // For getting resource path
import java.util.List;

public class JsonDataReader {

    /**
     * Reads a JSON file from the classpath resources and maps its content
     * to a List of PurchaseScenario objects.
     *
     * @param fileName The name of the JSON file (e.g., "purchase_scenarios.json")
     * expected to be in src/test/resources or src/main/resources.
     * @return A List of PurchaseScenario objects, or null if an error occurs.
     */
    public static List<PurchaseScenario> getPurchaseScenarios(String fileName) {
        ObjectMapper mapper = new ObjectMapper(); // Jackson's main object for JSON operations
        List<PurchaseScenario> scenarios = null;

        try {
            // Get the URL for the resource file from the classpath
            // This is robust for both Maven/Gradle build paths and IDE execution
            URL resource = JsonDataReader.class.getClassLoader().getResource("testdata/" + fileName);

            if (resource == null) {
                throw new IOException("Resource not found: testdata/" + fileName + ". Make sure it's in src/test/resources/testdata/");
            }

            File jsonFile = new File(resource.getFile());

            // Read JSON from file and map to a List of PurchaseScenario objects
            // TypeReference is crucial for deserializing a List of generic types
            scenarios = mapper.readValue(jsonFile, new TypeReference<List<PurchaseScenario>>() {});
            System.out.println("Successfully loaded " + scenarios.size() + " purchase scenarios from " + fileName);

        } catch (IOException e) {
            System.err.println("Error reading or mapping JSON file '" + fileName + "': " + e.getMessage());
            e.printStackTrace();
            // Depending on your error handling strategy, you might re-throw the exception
            // or return an empty list. Returning null makes it clear something went wrong.
        }
        return scenarios;
    }

    // You could add other generic methods here if you have different POJOs and JSON files
    // public static <T> List<T> readJsonList(String fileName, Class<T> type) { ... }
}