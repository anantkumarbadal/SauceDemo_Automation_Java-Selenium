package com.sauceDemo.exceptions;

public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class ElementNotInteractableException extends FrameworkException {
        public ElementNotInteractableException(String message) {
            super("Element not interactable: " + message);
        }
    }

    public static class TimeoutException extends FrameworkException {
        public TimeoutException(String message) {
            super("Timeout occurred: " + message);
        }
    }

    public static class ConfigurationException extends FrameworkException {
        public ConfigurationException(String message) {
            super("Configuration error: " + message);
        }
    }
}
