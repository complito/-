package ru.oop;

public class ConfigPropertiesException extends RuntimeException {
    private final String message;

    public ConfigPropertiesException(String message) { this.message = message; }

    public String getMessage() { return message; }
}