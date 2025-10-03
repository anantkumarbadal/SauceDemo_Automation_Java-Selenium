package com.sauceDemo.utils;

import com.github.javafaker.Faker;


    public class FakerUtils {

        private static Faker faker = new Faker();

        public static String generateFirstName() {
            return faker.name().firstName();
        }

        public static String generateLastName() {
            return faker.name().lastName();
        }

        public static String generateZipCode() {

            // use regexify to ensure a 5-digit number.
            return faker.regexify("[0-9]{5}");
            // Alternatively, use faker.address().zipCode() if the application accepts varied formats.
            // return faker.address().zipCode();
        }

        public static String generateEmail() {
            return faker.internet().emailAddress();
        }

        public static String generatePhoneNumber() {
            return faker.phoneNumber().phoneNumber();
        }

        // You can add more specific data generation methods as needed,
        // e.g., generateCreditCardNumber(), generateStreetAddress(), etc.


}
