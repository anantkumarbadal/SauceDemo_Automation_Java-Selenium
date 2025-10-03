package com.sauceDemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            System.out.println("ConfigLoader: Successfully loaded properties from " + CONFIG_FILE_PATH);
        } catch (IOException e) {
            System.err.println("ERROR: Could not load config.properties file. Please ensure it's at: " + CONFIG_FILE_PATH);
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration file. Check path and permissions.", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("WARNING: Property '" + key + "' not found in config.properties.");
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null && value.matches("\\d+")) {
            return Integer.parseInt(value);
        }
        System.err.println("WARNING: Property '" + key + "' is not a valid integer. Using default: " + defaultValue);
        return defaultValue;
    }
}