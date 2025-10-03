package com.sauceDemo.exceptions;

    // Custom exception for logical errors or unexpected states during a test flow
    public class AutomationFlowException extends FrameworkException {

        public AutomationFlowException(String message) {
            super(message);
        }

        public AutomationFlowException(String message, Throwable cause) {
            super(message, cause);
        }


}
