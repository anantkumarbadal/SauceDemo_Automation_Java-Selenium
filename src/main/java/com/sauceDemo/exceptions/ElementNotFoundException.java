package com.sauceDemo.exceptions;


    // Custom exception for when an expected element is not found on the page
    public class ElementNotFoundException extends FrameworkException {

        public ElementNotFoundException(String message) {
            super(message);
        }

        public ElementNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

}
