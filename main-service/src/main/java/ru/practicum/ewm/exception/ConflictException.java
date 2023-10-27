package ru.practicum.ewm.exception;

public class ConflictException extends Exception {
    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }
}
